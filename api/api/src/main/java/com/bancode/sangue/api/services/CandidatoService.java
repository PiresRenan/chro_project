package com.bancode.sangue.api.services;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    public void salvarCandidato(Candidato candidato) {
        candidato.setCpf(candidato.getCpf().replaceAll("\\D", "")); // Exemplo de ajuste
        candidatoRepository.save(candidato);
    }
    public void salvarListaDeCandidatos(List<Candidato> candidatos) {
        for (Candidato candidato : candidatos) {
            candidato.setCpf(candidato.getCpf().replaceAll("\\D", ""));
        }
        candidatoRepository.saveAll(candidatos);
    }

    public Map<String, Double> obterMediaIdadePorTipoSanguineo() {
        List<Map<String, Object>> resultados = candidatoRepository.calcularMediaIdadePorTipoSanguineo();

        Map<String, Double> mediaIdadePorTipo = new HashMap<>();
        for (Map<String, Object> resultado : resultados) {
            String tipoSanguineo = (String) resultado.get("tipo");
            Double mediaIdade = (Double) resultado.get("mediaIdade");

            if (tipoSanguineo != null) {
                mediaIdadePorTipo.put(tipoSanguineo, mediaIdade);
            }
        }

        return mediaIdadePorTipo;
    }
}
