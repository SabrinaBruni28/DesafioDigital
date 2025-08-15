package com.desafiodigital.API.controllers;

import com.desafiodigital.API.models.Partida;
import com.desafiodigital.API.repositories.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    @Autowired
    private PartidaRepository partidaRepository;

    @GetMapping
    public List<Partida> listarPartidas() {
        return partidaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscarPartida(@PathVariable int id) {
        return partidaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Partida> criarPartida(@RequestBody Partida partida) {
        Partida salva = partidaRepository.save(partida);
        return ResponseEntity.status(201).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> atualizarPartida(@PathVariable int id, @RequestBody Partida partidaAtualizada) {
        return partidaRepository.findById(id)
                .map(partidaExistente -> {
                    partidaExistente.setData(partidaAtualizada.getData());
                    Partida salva = partidaRepository.save(partidaExistente);
                    return ResponseEntity.ok(salva);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPartida(@PathVariable int id) {
        if (!partidaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        partidaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
