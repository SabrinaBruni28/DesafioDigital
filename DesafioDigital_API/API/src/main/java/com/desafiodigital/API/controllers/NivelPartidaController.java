package com.desafiodigital.API.controllers;

import com.desafiodigital.API.models.NivelPartida;
import com.desafiodigital.API.repositories.NivelPartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nivel-partidas")
public class NivelPartidaController {

    @Autowired
    private NivelPartidaRepository nivelPartidaRepository;

    @GetMapping
    public List<NivelPartida> listarTodos() {
        return nivelPartidaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelPartida> buscarPorId(@PathVariable int id) {
        return nivelPartidaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NivelPartida> criar(@RequestBody NivelPartida nivelPartida) {
        NivelPartida salvo = nivelPartidaRepository.save(nivelPartida);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelPartida> atualizar(@PathVariable int id, @RequestBody NivelPartida nivelAtualizado) {
        return nivelPartidaRepository.findById(id)
                .map(nivelExistente -> {
                    nivelExistente.setFase(nivelAtualizado.getFase());
                    nivelExistente.setNivel(nivelAtualizado.getNivel());
                    nivelExistente.setTempo(nivelAtualizado.getTempo());
                    nivelExistente.setQtdErros(nivelAtualizado.getQtdErros());
                    nivelExistente.setPontuacao(nivelAtualizado.getPontuacao());
                    NivelPartida salvo = nivelPartidaRepository.save(nivelExistente);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable int id) {
        if (!nivelPartidaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        nivelPartidaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
