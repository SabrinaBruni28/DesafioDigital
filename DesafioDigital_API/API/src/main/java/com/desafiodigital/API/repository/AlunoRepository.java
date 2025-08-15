package com.desafiodigital.API.repositories;

import java.util.List;
import com.desafiodigital.API.models.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    List<Aluno> findByNomeAndTurmaId(String nome, Integer turmaId);
}
