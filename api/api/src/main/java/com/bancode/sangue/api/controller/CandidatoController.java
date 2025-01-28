package com.bancode.sangue.api.controller;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.repository.CandidatoRepository;
import com.bancode.sangue.api.services.CandidatoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidatos")
@Tag(name = "Candidatos", description = "Gerenciamento de candidatos à doação de sangue")
public class CandidatoController {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private CandidatoService candidatoService;

    @PostMapping
    @Operation(summary = "Salvar um candidato", description = "Adiciona um novo candidato ao banco de dados.")
    public ResponseEntity<String> salvarCandidato(@RequestBody Candidato candidato) {
        candidatoService.salvarCandidato(candidato);
        return ResponseEntity.ok("Candidato salvo com sucesso!");
    }

    @GetMapping
    @Operation(summary = "Listar candidatos", description = "Retorna uma lista com todos os candidatos cadastrados.")
    public ResponseEntity<List<Candidato>> listarCandidatos() {
        List<Candidato> candidatos = candidatoService.listarTodos();
        return ResponseEntity.ok(candidatos);
    }

    @PostMapping("/lista-de-candidatos")
    @Operation(summary = "Salvar uma lista de candidatos", description = "Adiciona vários candidatos de uma vez ao banco de dados.")
    public ResponseEntity<String> salvarCandidatos(@RequestBody List<Candidato> candidatos) {
        candidatoService.salvarListaDeCandidatos(candidatos);
        return ResponseEntity.ok("Candidatos salvos com sucesso!");
    }

    @GetMapping("/filtros")
    @Operation(summary = "Filtrar candidatos", description = "Lista candidatos por nome e email, com paginação.")
    public Page<Candidato> listarCandidatos(
            @RequestParam(required = false) @Parameter(description = "Nome do candidato") String nome,
            @RequestParam(required = false) @Parameter(description = "Email do candidato") String email,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return candidatoService.listarComFiltros(nome, email, pageable);
    }

    @GetMapping("/candidatos-por-estado")
    @Operation(summary = "Candidatos por estado", description = "Retorna a quantidade de candidatos por estado.")
    public ResponseEntity<Map<String, Long>> getCandidatosPorEstado() {
        List<Candidato> candidatos = candidatoRepository.findAll();
        Map<String, Long> candidatosPorEstado = candidatos.stream()
                .collect(Collectors.groupingBy(Candidato::getEstado, Collectors.counting()));

        return ResponseEntity.ok(candidatosPorEstado);
    }

    @GetMapping("/imc-por-faixa-etaria")
    @Operation(summary = "IMC por faixa etária", description = "Calcula a média do IMC dos candidatos em diferentes faixas etárias.")
    public ResponseEntity<Map<String, Double>> getIMCPorFaixaEtaria() {
        List<Candidato> candidatos = candidatoRepository.findAll();

        Map<String, Double> imcPorFaixa = candidatos.stream()
                .filter(c -> c.getAltura() != null && c.getPeso() != null)
                .collect(Collectors.groupingBy(
                        c -> {
                            int faixa = c.getIdade() / 10;
                            return faixa * 10 + "-" + (faixa * 10 + 9);
                        },
                        Collectors.averagingDouble(c -> c.getPeso() / (c.getAltura() * c.getAltura()))
                ));

        return ResponseEntity.ok(imcPorFaixa);
    }

    @GetMapping("/percentual-obesos-por-sexo")
    @Operation(summary = "Percentual de obesos por sexo", description = "Calcula o percentual de obesos de acordo com o sexo.")
    public ResponseEntity<Map<String, Double>> getPercentualObesosPorSexo() {
        List<Candidato> candidatos = candidatoRepository.findAll();

        Map<String, Double> percentualObesos = candidatos.stream()
                .filter(c -> c.getAltura() != null && c.getPeso() != null)
                .collect(Collectors.groupingBy(
                        Candidato::getSexo,
                        Collectors.averagingDouble(c -> {
                            double imc = c.getPeso() / (c.getAltura() * c.getAltura());
                            return imc > 30 ? 1.0 : 0.0;
                        })
                ));

        return ResponseEntity.ok(percentualObesos);
    }

    @GetMapping("/media-idade-por-tipo-sanguineo")
    @Operation(summary = "Média de idade por tipo sanguíneo", description = "Retorna a média de idade dos candidatos por tipo sanguíneo.")
    public ResponseEntity<Map<String, Double>> getMediaIdadePorTipoSanguineo() {
        Map<String, Double> mediaIdadePorTipo = candidatoService.obterMediaIdadePorTipoSanguineo();
        return ResponseEntity.ok(mediaIdadePorTipo);
    }

    @GetMapping("/possiveis-doadores")
    @Operation(summary = "Possíveis doadores", description = "Retorna a quantidade de possíveis doadores por tipo sanguíneo.")
    public ResponseEntity<Map<String, Long>> getPossiveisDoadores() {
        List<Candidato> candidatos = candidatoRepository.findAll();

        Map<String, Long> doadoresPorTipo = candidatos.stream()
                .filter(c -> c.getIdade() >= 16 && c.getIdade() <= 69 && c.getPeso() > 50)
                .collect(Collectors.groupingBy(
                        Candidato::getTipo_sanguineo,
                        Collectors.counting()
                ));

        return ResponseEntity.ok(doadoresPorTipo);
    }
}