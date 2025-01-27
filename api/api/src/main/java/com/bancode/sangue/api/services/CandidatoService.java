package com.bancode.sangue.api.services;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    public void salvarCandidatos(List<Candidato> candidatos) {
        // Adicionar validações, se necessário
        for (Candidato candidato : candidatos) {
            // Validar e ajustar campos específicos (ex.: remover espaços, normalizar dados)
            candidato.setCpf(candidato.getCpf().replaceAll("\\D", "")); // Remove caracteres não numéricos do CPF
        }
        candidatoRepository.saveAll(candidatos);
    }
}
