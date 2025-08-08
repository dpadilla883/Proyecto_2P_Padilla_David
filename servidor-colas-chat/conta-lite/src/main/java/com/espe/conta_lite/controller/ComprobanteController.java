/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.espe.conta_lite.controller;



import com.espe.conta_lite.domain.ComprobanteCab;
import com.espe.conta_lite.repo.ComprobanteCabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
@RequiredArgsConstructor
public class ComprobanteController {

    private final ComprobanteCabRepository repo;

    @GetMapping
    public List<ComprobanteCab> listar() { return repo.findAll(); }
}
