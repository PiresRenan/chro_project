package com.bancode.sangue.api.services;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.repository.CandidatoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CandidatoService {

    private static final Logger logger = LoggerFactory.getLogger(CandidatoService.class);

    @Autowired
    private CandidatoRepository candidatoRepository;

    public CandidatoService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }

    public List<Candidato> listarTodos() {
        logger.info("Buscando todos os candidatos cadastrados.");
        List<Candidato> candidatos = candidatoRepository.findAll();
        logger.info("{} candidatos encontrados.", candidatos.size());
        return candidatos;
    }

    public void salvarCandidato(Candidato candidato) {
        logger.info("Salvando candidato: {}", candidato);
        candidato.setCpf(candidato.getCpf().replaceAll("\\D", ""));
        candidatoRepository.save(candidato);
        logger.info("Candidato salvo com sucesso: {}", candidato.getId());
    }

    public void salvarListaDeCandidatos(List<Candidato> candidatos) {
        logger.info("Salvando lista de candidatos. Quantidade: {}", candidatos.size());
        for (Candidato candidato : candidatos) {
            candidato.setCpf(candidato.getCpf().replaceAll("\\D", ""));
        }
        candidatoRepository.saveAll(candidatos);
        logger.info("Lista de candidatos salva com sucesso.");
    }

    public Map<String, Double> obterMediaIdadePorTipoSanguineo() {
        logger.info("Calculando média de idade por tipo sanguíneo.");
        List<Map<String, Object>> resultados = candidatoRepository.calcularMediaIdadePorTipoSanguineo();

        Map<String, Double> mediaIdadePorTipo = new HashMap<>();
        for (Map<String, Object> resultado : resultados) {
            String tipoSanguineo = (String) resultado.get("tipo");
            Double mediaIdade = (Double) resultado.get("mediaIdade");

            if (tipoSanguineo != null) {
                mediaIdadePorTipo.put(tipoSanguineo, mediaIdade);
            }
        }
        logger.info("Média de idade por tipo sanguíneo calculada: {}", mediaIdadePorTipo);
        return mediaIdadePorTipo;
    }

    public Page<Candidato> listarComFiltros(String nome, String email, Pageable pageable) {
        logger.info("Buscando candidatos com filtros - Nome: {}, Email: {}", nome, email);
        List<Candidato> candidatos = candidatoRepository.findAll();

        List<Candidato> candidatosFiltrados = candidatos.stream()
                .filter(candidato -> {
                    boolean filtroNome = (nome == null || nome.isBlank()) ||
                            candidato.getNome().toLowerCase().contains(nome.toLowerCase());
                    boolean filtroEmail = (email == null || email.isBlank()) ||
                            candidato.getEmail().toLowerCase().contains(email.toLowerCase());
                    return filtroNome && filtroEmail;
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), candidatosFiltrados.size());
        List<Candidato> paginados = candidatosFiltrados.subList(start, end);

        logger.info("{} candidatos encontrados após filtragem.", candidatosFiltrados.size());
        return new PageImpl<>(paginados, pageable, candidatosFiltrados.size());
    }

}
