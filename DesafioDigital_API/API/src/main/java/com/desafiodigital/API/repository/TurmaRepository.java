package com.desafiodigital.API.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.desafiodigital.API.models.Turma;
import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Integer> { 
    List<Turma> findByNome(String nome);
}
