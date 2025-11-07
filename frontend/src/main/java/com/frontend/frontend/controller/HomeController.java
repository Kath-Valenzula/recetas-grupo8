package com.frontend.frontend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.frontend.frontend.TokenStore;
import com.frontend.frontend.model.RecetaDTO;
import com.frontend.frontend.service.RecetaService;


@Controller
public class HomeController {

    private TokenStore tokenStore;
    @Autowired
    private RecetaService recetaService;

    public HomeController(TokenStore tokenStore) {
        super();
        this.tokenStore = tokenStore;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<RecetaDTO> recetasPopulares = recetaService.buscarRecetas(null, null, null, null, null, true);
        List<RecetaDTO> recetasRecientes = recetaService.buscarRecetas(null, null, null, null, null, null);
        
        model.addAttribute("recetasPopulares", recetasPopulares);
        model.addAttribute("recetasRecientes", recetasRecientes);

        return "home";
    }
    
    @GetMapping("/")
    public String inicio(Model model) {
        List<RecetaDTO> recetasPopulares = recetaService.buscarRecetas(null, null, null, null, null, true);
        List<RecetaDTO> recetasRecientes = recetaService.buscarRecetas(null, null, null, null, null, null);
        
        model.addAttribute("recetasPopulares", recetasPopulares);
        model.addAttribute("recetasRecientes", recetasRecientes);

        return "home";
    }

    @PostMapping("/logout")
    public String logout() {
        tokenStore.setToken(null);
        return "redirect:/login";
    }
    
    
}
