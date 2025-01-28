package com.bancode.sangue.api.controller;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.repository.CandidatoRepository;
import com.bancode.sangue.api.services.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private CandidatoService candidatoService;

    @PostMapping
    public ResponseEntity<String> salvarCandidato(@RequestBody Candidato candidato) {
        candidatoService.salvarCandidato(candidato);
        return ResponseEntity.ok("Candidato salvo com sucesso!");
    }

    @PostMapping("/lista-de-candidatos")
    public ResponseEntity<String> salvarCandidatos(@RequestBody List<Candidato> candidatos) {
        candidatoService.salvarListaDeCandidatos(candidatos);
        return ResponseEntity.ok("Candidatos salvos com sucesso!");
    }

    @GetMapping("/candidatos-por-estado")
    public ResponseEntity<Map<String, Long>> getCandidatosPorEstado() {
        List<Candidato> candidatos = candidatoRepository.findAll();
        Map<String, Long> candidatosPorEstado = candidatos.stream()
                .collect(Collectors.groupingBy(Candidato::getEstado, Collectors.counting()));

        return ResponseEntity.ok(candidatosPorEstado);
    }

    @GetMapping("/imc-por-faixa-etaria")
    public ResponseEntity<Map<String, Double>> getIMCPorFaixaEtaria() {
        List<Candidato> candidatos = candidatoRepository.findAll();

        Map<String, Double> imcPorFaixa = candidatos.stream()
                .filter(c -> Integer.toString(c.getIdade()) != null && c.getAltura() != null && c.getPeso() != null)
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
    public ResponseEntity<Map<String, Double>> getMediaIdadePorTipoSanguineo() {
        Map<String, Double> mediaIdadePorTipo = candidatoService.obterMediaIdadePorTipoSanguineo();
        return ResponseEntity.ok(mediaIdadePorTipo);
    }

    @GetMapping("/possiveis-doadores")
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