package com.bancode.sangue.api;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.repository.CandidatoRepository;
import com.bancode.sangue.api.services.CandidatoService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @InjectMocks
    private CandidatoService candidatoService;

    private List<Candidato> candidatos;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        candidatos = objectMapper.readValue(new File("src/main/resources/candidatos.json"),
                new TypeReference<List<Candidato>>() {});
    }

    @Test
    void testListarTodos() {
        when(candidatoRepository.findAll()).thenReturn(candidatos);
        List<Candidato> resultado = candidatoService.listarTodos();
        assertEquals(candidatos.size(), resultado.size());
        verify(candidatoRepository, times(1)).findAll();
    }

    @Test
    void testSalvarCandidato() {
        Candidato candidato = candidatos.get(0);
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);
        candidatoService.salvarCandidato(candidato);
        verify(candidatoRepository, times(1)).save(candidato);
    }

    @Test
    void testSalvarListaDeCandidatos() {
        candidatoService.salvarListaDeCandidatos(candidatos);
        verify(candidatoRepository, times(1)).saveAll(candidatos);
    }

    @Test
    void testListarComFiltros() {
        when(candidatoRepository.findAll()).thenReturn(candidatos);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Candidato> resultado = candidatoService.listarComFiltros("Milena", "mmilenaanalupires@keffin.com.br", pageable);
        assertFalse(resultado.getContent().isEmpty());
        assertEquals("Milena Analu Pires", resultado.getContent().get(0).getNome());
    }
}
