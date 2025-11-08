param([string]$RepoRoot = "$repo")

# ===== 0) Preparación =====
$ErrorActionPreference = "Stop"
if (-not (Test-Path $RepoRoot)) { throw ("No existe RepoRoot: {0}" -f $RepoRoot) }

# PowerShell para mvnw.cmd
$psDir = "$env:WINDIR\System32\WindowsPowerShell\v1.0"
if (Test-Path "$psDir\powershell.exe") { $env:PATH = "$psDir;$env:PATH" }

# Mata procesos que ponen candados
Get-Process -Name "OneDrive","java","javaw","mvn","node","Code","code","vscode" -ErrorAction SilentlyContinue | `
  Stop-Process -Force -ErrorAction SilentlyContinue

# ===== 1) Estructura y prerequisitos =====
Set-Location $RepoRoot
$stamp = Get-Date -Format "yyyyMMdd_HHmmss"
$reportDir = Join-Path $RepoRoot "build_reports"
New-Item -ItemType Directory -Force -Path $reportDir | Out-Null
$rep = Join-Path $reportDir "report_$stamp.md"

function W($s){ Add-Content -Path $rep -Value $s }

W ('# Build Doctor Report ({0})' -f $stamp)
W ''
W 'Repo: `$RepoRoot`'

# Detecta submódulos
$demo = Join-Path $RepoRoot "demo"
$fe   = Join-Path $RepoRoot "frontend"
$hasDemo = Test-Path (Join-Path $demo "mvnw.cmd")
$hasFE   = Test-Path (Join-Path $fe   "mvnw.cmd")
W ('* Backend (demo): {0}*' -f $hasDemo)
W ('* Frontend (frontend): {0}*' -f $hasFE)
W ''

# Verifica Git/Java
function SafeVer($cmd,$args){
  try { & $cmd $args 2>&1 | Select-Object -First 1 } catch { return "$cmd no disponible" }
}
W ('``git --version`` => {0}' -f (SafeVer git '--version'))
W ('``java -version`` => {0}' -f (SafeVer java '-version'))
W ''

# ===== 2) Fixes seguros previos =====
# Desactivar Flyway y SQL init si no están y existe demo
if ($hasDemo) {
  $appProps = Join-Path $demo "src\main\resources\application.properties"
  $appBak   = "$appProps.bak_$stamp"
  $needsLine = $false
  $desired = @(
    "spring.flyway.enabled=false",
    "spring.sql.init.mode=never"
  )
  if (-not (Test-Path $appProps)) {
    New-Item -ItemType Directory -Force -Path (Split-Path $appProps) | Out-Null
    Set-Content -Path $appProps -Value ($desired -join [Environment]::NewLine) -Encoding UTF8
    W '- Se creó **application.properties** con desactivación de Flyway/SQL init.'
  } else {
    $txt = Get-Content -LiteralPath $appProps -Raw
    $orig = $txt
    foreach($line in $desired){
      if ($txt -notmatch [regex]::Escape($line)) { $txt += "`r`n$line" ; $needsLine = $true }
    }
    if ($needsLine) {
      Copy-Item $appProps $appBak -Force
      Set-Content -Path $appProps -Value $txt -Encoding UTF8
      W '- Se forzó desactivar Flyway/SQL init (backup: `$(Split-Path $appBak -Leaf)`).'
    } else {
      W '- Flyway/SQL init ya configurados.'
    }
  }
}

# Limpieza robusta de target/ y .vscode/
function Force-Delete([string]$path){
  if (-not (Test-Path $path)) { return }
  attrib -r -s -h /s /d "$path\*" 2>$null
  takeown /F "$path" /R /D Y 2>$null | Out-Null
  icacls "$path" /grant "$($env:USERNAME):(F)" /T 2>$null | Out-Null
  Remove-Item -LiteralPath $path -Recurse -Force -ErrorAction SilentlyContinue
  if (Test-Path $path) {
    $empty = Join-Path $env:TEMP ("empty_dir_"+[guid]::NewGuid())
    New-Item -ItemType Directory -Path $empty | Out-Null
    robocopy "$empty" "$path" /MIR /NFL /NDL /NJH /NJS /NC /NS | Out-Null
    Remove-Item -LiteralPath $path -Recurse -Force -ErrorAction SilentlyContinue
    Remove-Item -LiteralPath $empty -Recurse -Force -ErrorAction SilentlyContinue
  }
}

foreach($d in @(
  Join-Path $demo "target",
  Join-Path $fe   "target",
  Join-Path $demo ".vscode",
  Join-Path $fe   ".vscode"
)){ Force-Delete $d }

# ===== 3) Compilación y captura de warnings =====
$demoLog = Join-Path $reportDir "demo_build_$stamp.log"
$feLog   = Join-Path $reportDir "frontend_build_$stamp.log"

if ($hasDemo){
  Set-Location $demo
  W '## Backend build'
  & .\mvnw.cmd -v | Tee-Object -FilePath $demoLog
  & .\mvnw.cmd clean package -DskipTests -Dmaven.compiler.showWarnings=true -Dmaven.compiler.showDeprecation=true `
    2>&1 | Tee-Object -FilePath $demoLog -Append
  $warns = Select-String -Path $demoLog -Pattern 'WARNING|deprecated|deprecation|unchecked|ERROR' -SimpleMatch
  W ''
  W ('**demo WARN/ERR encontrados:** {0}' -f $warns.Count)
  if ($warns) {
    $warnLines = $warns | ForEach-Object { $_.Line } | Select-Object -First 80
    $warnBody = $warnLines -join [Environment]::NewLine
    W ('```text' + [Environment]::NewLine + $warnBody + [Environment]::NewLine + '```')
  }
}

if ($hasFE){
  Set-Location $fe
  W '## Frontend build'
  & .\mvnw.cmd -v | Tee-Object -FilePath $feLog
  & .\mvnw.cmd clean package -DskipTests -Dmaven.compiler.showWarnings=true -Dmaven.compiler.showDeprecation=true `
    2>&1 | Tee-Object -FilePath $feLog -Append
  $warnsFe = Select-String -Path $feLog -Pattern 'WARNING|deprecated|deprecation|unchecked|ERROR' -SimpleMatch
  W ''
  W ('**frontend WARN/ERR encontrados:** {0}' -f $warnsFe.Count)
  if ($warnsFe) {
    $warnLinesFe = $warnsFe | ForEach-Object { $_.Line } | Select-Object -First 80
    $warnBodyFe = $warnLinesFe -join [Environment]::NewLine
    W ('```text' + [Environment]::NewLine + $warnBodyFe + [Environment]::NewLine + '```')
  }
}

# ===== 4) Arranque de diagnóstico (backend) =====
if ($hasDemo){
  Set-Location $demo
  $bootLog = Join-Path $reportDir "demo_run_$stamp.log"
  W '## Arranque Spring Boot (diagnóstico, 25s máx.)'
  $p = Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run -Dspring-boot.run.profiles=default" `
        -RedirectStandardOutput $bootLog -RedirectStandardError $bootLog -PassThru -WindowStyle Hidden
  Start-Sleep -Seconds 25
  try { $p | Stop-Process -Force -ErrorAction SilentlyContinue } catch {}
  $started = Select-String -Path $bootLog -Pattern "Started .* in|Tomcat started on port" -SimpleMatch
  if ($started) {
    W '- Señales de arranque detectadas (OK).'
  } else {
    W '- No se detectó arranque completo en 25s (revisar log).'
  }
  W ''
  W '**Extracto de log:**'
  $tail = Get-Content -Path $bootLog -Tail 120
  $tailBlock = $tail -join [Environment]::NewLine
  W ('```text' + [Environment]::NewLine + $tailBlock + [Environment]::NewLine + '```')
}

# ===== 5) Sugerencias automáticas (no destructivas) =====
W '## Sugerencias'
W '- **Flyway loop**: ya se desactivó con ``spring.flyway.enabled=false`` y ``spring.sql.init.mode=never``.'
W '- Si persisten errores, ejecutar: ``mvn -e -X clean package`` y revisar el log completo del reporte.'
W '- Para warnings ``unchecked``/``deprecation``, evaluar ``@SuppressWarnings`` o actualizar APIs (revisión caso a caso).'

W ''
W '_Archivos del reporte:_'
W ('- ' + [IO.Path]::GetFileName($demoLog))
if ($hasFE) { W ('- ' + [IO.Path]::GetFileName($feLog)) }
if ($hasDemo) { W ('- ' + [IO.Path]::GetFileName($bootLog)) }

Write-Host "Reporte generado: $rep"
