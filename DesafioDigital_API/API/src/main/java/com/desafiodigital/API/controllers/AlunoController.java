package com.desafiodigital.API.controllers;

import com.desafiodigital.API.models.Aluno;
import com.desafiodigital.API.models.Turma;
import com.desafiodigital.API.repositories.AlunoRepository;
import com.desafiodigital.API.repositories.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    // Listar todos os alunos
    @GetMapping
    public List<Aluno> listarAlunos() {
        return alunoRepository.findAll();
    }

    // Buscar aluno por id
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable int id) {
        return alunoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar alunos pelo nome e turma
    @GetMapping("/buscar")
    public List<Aluno> buscarAlunos(@RequestParam String nome, @RequestParam Integer turmaId) {
        return alunoRepository.findByNomeAndTurmaId(nome, turmaId);
    }

    // Criar novo aluno
    @PostMapping
    public ResponseEntity<?> criarAluno(@RequestBody Aluno aluno) {
        try {
            if (aluno.getTurma() == null || aluno.getTurma().getId() == null) {
                return ResponseEntity.badRequest().body("Turma não informada");
            }

            var turmaOptional = turmaRepository.findById(aluno.getTurma().getId());
            if (turmaOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Turma não encontrada");
            }

            aluno.setTurma(turmaOptional.get());
            Aluno salvo = alunoRepository.save(aluno);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Atualizar aluno existente
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable int id,
                                                @RequestBody Aluno alunoAtualizado,
                                                @RequestParam Integer turmaId) {
        return alunoRepository.findById(id)
                .map(alunoExistente -> {
                    alunoExistente.setNome(alunoAtualizado.getNome());

                    // Se turma existir, associa
                    turmaRepository.findById(turmaId).ifPresent(alunoExistente::setTurma);

                    Aluno salvo = alunoRepository.save(alunoExistente);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar aluno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable int id) {
        if (!alunoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alunoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
