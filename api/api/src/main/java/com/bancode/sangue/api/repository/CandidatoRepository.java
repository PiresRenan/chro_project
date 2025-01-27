package com.bancode.sangue.api.repository;

import com.bancode.sangue.api.models.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
}
