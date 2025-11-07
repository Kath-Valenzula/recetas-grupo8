  function mostrarModal() {
    const modal = new bootstrap.Modal(document.getElementById('loginModal'));
    modal.show();
  }

    function cerrarModal() {
      document.getElementById('loginModal').style.display = 'none';
    }
    window.onclick = function(event) {
      const modal = document.getElementById('loginModal');
      if (event.target === modal) modal.style.display = 'none';
    }