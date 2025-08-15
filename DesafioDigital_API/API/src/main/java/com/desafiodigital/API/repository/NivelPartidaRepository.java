package com.desafiodigital.API.repositories;

import com.desafiodigital.API.models.NivelPartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelPartidaRepository extends JpaRepository<NivelPartida, Integer> {
}
