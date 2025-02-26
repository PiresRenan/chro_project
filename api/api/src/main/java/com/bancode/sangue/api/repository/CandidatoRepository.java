package com.bancode.sangue.api.repository;

import com.bancode.sangue.api.models.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

    @Query("SELECT c.tipo_sanguineo AS tipo, AVG(c.idade) AS mediaIdade " +
            "FROM Candidato c " +
            "WHERE c.idade IS NOT NULL AND c.tipo_sanguineo IS NOT NULL " +
            "GROUP BY c.tipo_sanguineo")
    List<Map<String, Object>> calcularMediaIdadePorTipoSanguineo();

    Page<Candidato> findByNomeIgnoreCase(String nome, Pageable pageable);

    Page<Candidato> findByEmailIgnoreCase(String email, Pageable pageable);

    Page<Candidato> findByNomeIgnoreCaseOrEmailIgnoreCase(String nome, String email, Pageable pageable);

}
