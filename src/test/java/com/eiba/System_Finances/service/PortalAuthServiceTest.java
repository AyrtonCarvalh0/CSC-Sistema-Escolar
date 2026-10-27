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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortalAuthServiceTest {

    private static final String CPF_VALIDO        = "12345678901";
    private static final String SENHA_VALIDA       = "CSC@15032010";
    private static final LocalDate DATA_NASCIMENTO = LocalDate.of(2010, 3, 15);

    @Mock AlunoRepository alunoRepository;
    @Mock TokenService tokenService;
    @InjectMocks PortalAuthService portalAuthService;

    // ── fixtures ─────────────────────────────────────────────────────────────

    private Aluno alunoComDataCorreta() {
        Aluno aluno = new Aluno();
        aluno.setId(UUID.randomUUID());
        aluno.setData_nascimento(DATA_NASCIMENTO);
        return aluno;
    }

    // ── testes ────────────────────────────────────────────────────────────────

    @Test
    void autenticar_senhaNula_retornaNull() {
        assertNull(portalAuthService.autenticar(CPF_VALIDO, null));
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void autenticar_senhaSemPrefixoCSC_retornaNull() {
        assertNull(portalAuthService.autenticar(CPF_VALIDO, "15032010"));
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void autenticar_dataInvalidaNaSenha_retornaNull() {
        // "CSC@" + data impossível → DateTimeParseException capturado internamente
        assertNull(portalAuthService.autenticar(CPF_VALIDO, "CSC@99999999"));
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void autenticar_cpfNulo_retornaNull() {
        assertNull(portalAuthService.autenticar(null, SENHA_VALIDA));
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void autenticar_cpfEmBranco_retornaNull() {
        assertNull(portalAuthService.autenticar("   ", SENHA_VALIDA));
        verifyNoInteractions(alunoRepository);
    }

    @Test
    void autenticar_cpfNaoEncontrado_retornaNull() {
        // Arrange
        when(alunoRepository.findByCpf(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertNull(portalAuthService.autenticar(CPF_VALIDO, SENHA_VALIDA));
    }

    @Test
    void autenticar_dataNascimentoDivergente_retornaNull() {
        // Arrange
        Aluno aluno = new Aluno();
        aluno.setData_nascimento(LocalDate.of(2000, 1, 1)); // diferente da senha
        when(alunoRepository.findByCpf(any())).thenReturn(Optional.of(aluno));

        // Act & Assert
        assertNull(portalAuthService.autenticar(CPF_VALIDO, SENHA_VALIDA));
        verifyNoInteractions(tokenService);
    }

    @Test
    void autenticar_credenciaisValidas_retornaToken() {
        // Arrange
        Aluno aluno = alunoComDataCorreta();
        when(alunoRepository.findByCpf(any())).thenReturn(Optional.of(aluno));
        when(tokenService.gerarTokenAluno(aluno)).thenReturn("jwt-token-gerado");

        // Act
        String resultado = portalAuthService.autenticar(CPF_VALIDO, SENHA_VALIDA);

        // Assert
        assertAll(
            () -> assertNotNull(resultado),
            () -> assertEquals("jwt-token-gerado", resultado)
        );
        verify(tokenService, times(1)).gerarTokenAluno(aluno);
    }

    @Test
    void autenticar_cpfComMascara_limpaEBuscaCorretamente() {
        // Arrange — CPF formatado: deve remover pontos e traço antes de buscar
        Aluno aluno = alunoComDataCorreta();
        when(alunoRepository.findByCpf("12345678901")).thenReturn(Optional.of(aluno));
        when(tokenService.gerarTokenAluno(aluno)).thenReturn("token");

        // Act & Assert
        assertNotNull(portalAuthService.autenticar("123.456.789-01", SENHA_VALIDA));
        verify(alunoRepository).findByCpf("12345678901");
    }
}
