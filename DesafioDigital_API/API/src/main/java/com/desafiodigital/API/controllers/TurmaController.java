package com.desafiodigital.API.controllers;

import com.desafiodigital.API.models.Turma;
import com.desafiodigital.API.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;

    // Listar todas as turmas
    @GetMapping
    public List<Turma> listarTurmas() {
        return turmaRepository.findAll();
    }

    // Buscar turma por id
    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarTurma(@PathVariable int id) {
        return turmaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar turmas pelo nome
    @GetMapping("/buscar")
    public List<Turma> buscarTurmas(@RequestParam String nome) {
        return turmaRepository.findByNome(nome);
    }

    // Criar nova turma
    @PostMapping
    public ResponseEntity<Turma> criarTurma(@RequestBody Turma turma) {
        Turma salva = turmaRepository.save(turma);
        return ResponseEntity.ok(salva);
    }

    // Atualizar turma existente
    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizarTurma(@PathVariable int id, @RequestBody Turma turmaAtualizada) {
        return turmaRepository.findById(id)
                .map(turmaExistente -> {
                    turmaExistente.setNome(turmaAtualizada.getNome());
                    Turma salva = turmaRepository.save(turmaExistente);
                    return ResponseEntity.ok(salva);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar turma
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTurma(@PathVariable int id) {
        if (!turmaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        turmaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
