package com.eiba.System_Finances.service;

import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PortalAuthServiceTest {

    @Mock
    AlunoRepository alunoRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    PortalAuthService portalAuthService;

    @Test
    void autenticar_senhaSemPrefixoCSC() {
        // Arrange - nenhum mock necessário, o método retorna antes de consultar o banco

        // Act
        String resultado = portalAuthService.autenticar("00000000001", "senha-errada");

        // Assert
        assertNull(resultado);
    }

    @Test
    void autenticar_dataInvalidaNaSenha() {
        // Arrange - nenhum mock necessário

        // Act
        String resultado = portalAuthService.autenticar("00000000001", "CSC@99999999");

        // Assert
        assertNull(resultado);
    }

    @Test
    void autenticar_cpfNaoEncontrado() {
        // Arrange - mock retorna vazio para qualquer CPF
        when(alunoRepository.findByCpf(any())).thenReturn(Optional.empty());

        // Act
        String resultado = portalAuthService.autenticar("00000000099", "CSC@15032024");

        // Assert
        assertNull(resultado);
    }

    @Test
    void autenticar_dataNascimentoDivergente() {
        // Arrange - aluno existe, mas nasceu em outra data
        Aluno aluno = new Aluno();
        aluno.setData_nascimento(LocalDate.of(2000, 1, 1)); // 01/01/2000

        when(alunoRepository.findByCpf(any())).thenReturn(Optional.of(aluno));

        // Act - senha diz 15/03/2024, mas o aluno nasceu em 01/01/2000
        String resultado = portalAuthService.autenticar("00000000001", "CSC@15032024");

        // Assert
        assertNull(resultado);
    }

    @Test
    void autenticar_credenciaisValidas() {
        // Arrange
        Aluno aluno = new Aluno();
        aluno.setData_nascimento(LocalDate.of(2024, 3, 15));

        when(alunoRepository.findByCpf(any())).thenReturn(Optional.of(aluno));
        when(tokenService.gerarTokenAluno(aluno)).thenReturn("token-gerado");

        // Act
        String resultado = portalAuthService.autenticar("00000000001", "CSC@15032024");

        // Assert
        assertNotNull(resultado);
        assertEquals("token-gerado", resultado);
    }

    @Test
    void autenticar_cpfNulo(){
        String resultado = portalAuthService.autenticar(null,  "CSC@15032024");
        assertNull(resultado);
    }
}
