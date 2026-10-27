package com.eiba.System_Finances.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "csc-secret-test");
    }

    // ── fixtures ─────────────────────────────────────────────────────────────

    private Aluno alunoComTurma() {
        Turma turma = new Turma();
        turma.setId(UUID.randomUUID());

        Aluno aluno = new Aluno();
        aluno.setId(UUID.randomUUID());
        aluno.setNome("João da Silva");
        aluno.setTurma(turma);
        return aluno;
    }

    // ── gerarTokenAluno ───────────────────────────────────────────────────────

    @Test
    void gerarTokenAluno_retornaTokenNaoNulo() {
        Aluno aluno = alunoComTurma();
        assertNotNull(tokenService.gerarTokenAluno(aluno));
    }

    @Test
    void gerarTokenAluno_tokenContemClaimTipoAluno() {
        // Arrange
        Aluno aluno = alunoComTurma();

        // Act
        String token = tokenService.gerarTokenAluno(aluno);
        DecodedJWT decoded = tokenService.decodificarToken(token);

        // Assert
        assertEquals("ALUNO", decoded.getClaim("tipo").asString());
    }

    @Test
    void gerarTokenAluno_tokenContemRoleAluno() {
        // Arrange
        Aluno aluno = alunoComTurma();

        // Act
        String token = tokenService.gerarTokenAluno(aluno);
        DecodedJWT decoded = tokenService.decodificarToken(token);

        // Assert
        assertEquals("ROLE_ALUNO", decoded.getClaim("role").asString());
    }

    @Test
    void gerarTokenAluno_subjectEhIdDoAluno() {
        // Arrange
        Aluno aluno = alunoComTurma();

        // Act
        String token = tokenService.gerarTokenAluno(aluno);
        DecodedJWT decoded = tokenService.decodificarToken(token);

        // Assert
        assertEquals(aluno.getId().toString(), decoded.getSubject());
    }

    // ── decodificarToken ──────────────────────────────────────────────────────

    @Test
    void decodificarToken_tokenInvalido_retornaNull() {
        assertNull(tokenService.decodificarToken("token.invalido.assinatura"));
    }

    @Test
    void decodificarToken_tokenExpirado_retornaNull() {
        // Token gerado com secret diferente do configurado → assinatura inválida
        String tokenComSecretErrado =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiJ0ZXN0ZSJ9" +
            ".assinatura-errada";

        assertNull(tokenService.decodificarToken(tokenComSecretErrado));
    }
}
