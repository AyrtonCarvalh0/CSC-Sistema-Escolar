package com.eiba.System_Finances.controller;

import com.eiba.System_Finances.DTO.portal.PortalLoginRequestDTO;
import com.eiba.System_Finances.entity.Aluno;
import com.eiba.System_Finances.entity.Pagamento;
import com.eiba.System_Finances.entity.Responsavel;
import com.eiba.System_Finances.entity.Turma;
import com.eiba.System_Finances.repository.AlunoRepository;
import com.eiba.System_Finances.repository.AvisoRepository;
import com.eiba.System_Finances.repository.BoletimRepository;
import com.eiba.System_Finances.repository.PagamentoRepository;
import com.eiba.System_Finances.service.PortalAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortalControllerTest {

    @Mock PortalAuthService portalAuthService;
    @Mock AlunoRepository alunoRepository;
    @Mock PagamentoRepository pagamentoRepository;
    @Mock AvisoRepository avisoRepository;
    @Mock BoletimRepository boletimRepository;

    @InjectMocks PortalController portalController;

    // ── fixtures ──────────────────────────────────────────────────────────────

    private Aluno alunoMinimo() {
        Aluno aluno = new Aluno();
        aluno.setId(UUID.randomUUID());
        aluno.setNome("Maria Souza");
        aluno.setCpf("11111111111");
        aluno.setData_nascimento(LocalDate.of(2010, 6, 20));
        return aluno;
    }

    private Aluno alunoCompleto() {
        Turma turma = new Turma();
        turma.setId(UUID.randomUUID());
        turma.setNome("3º Ano A");

        Responsavel responsavel = new Responsavel();
        responsavel.setName("Carlos Souza");

        Aluno aluno = alunoMinimo();
        aluno.setTurma(turma);
        aluno.setResponsavel(responsavel);
        return aluno;
    }

    private Pagamento pagamentoPendente(Aluno aluno) {
        Pagamento p = new Pagamento();
        p.setId(UUID.randomUUID());
        p.setAluno(aluno);
        p.setMes("outubro/2026");
        p.setValor(350.0);
        p.setPago(false);
        return p;
    }

    // ── login ────────────────────────────────────────────────────────────────

    @Test
    void login_credenciaisInvalidas_retorna401() {
        // Arrange
        when(portalAuthService.autenticar(any(), any())).thenReturn(null);
        var request = new PortalLoginRequestDTO("00000000000", "senha-errada");

        // Act
        ResponseEntity<?> response = portalController.login(request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void login_credenciaisValidas_retornaTokenNoBody() {
        // Arrange
        when(portalAuthService.autenticar("12345678901", "CSC@20062010"))
            .thenReturn("jwt-valido");
        var request = new PortalLoginRequestDTO("12345678901", "CSC@20062010");

        // Act
        ResponseEntity<?> response = portalController.login(request);

        // Assert
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertNotNull(response.getBody())
        );
    }

    // ── meus-dados ───────────────────────────────────────────────────────────

    @Test
    void meusDados_alunoSemTurmaESemResponsavel_camposSaoNulos() {
        // Arrange
        Aluno aluno = alunoMinimo(); // turma e responsavel nulos

        // Act
        var response = portalController.meusDados(aluno);

        // Assert
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertNull(response.getBody().turma()),
            () -> assertNull(response.getBody().responsavel())
        );
    }

    @Test
    void meusDados_alunoCompleto_retornaTodosOsCampos() {
        // Arrange
        Aluno aluno = alunoCompleto();

        // Act
        var response = portalController.meusDados(aluno);
        var body = response.getBody();

        // Assert
        assertAll(
            () -> assertEquals(aluno.getId(), body.id()),
            () -> assertEquals("Maria Souza", body.nome()),
            () -> assertEquals("11111111111", body.cpf()),
            () -> assertEquals("3º Ano A", body.turma()),
            () -> assertEquals("Carlos Souza", body.responsavel())
        );
    }

    // ── pendências ───────────────────────────────────────────────────────────

    @Test
    void pendencias_semPagamentosEmAberto_retornaListaVazia() {
        // Arrange
        Aluno aluno = alunoMinimo();
        when(pagamentoRepository.findByAlunoAndPago(aluno, false)).thenReturn(List.of());

        // Act
        var response = portalController.pendencias(aluno);

        // Assert
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertTrue(response.getBody().isEmpty())
        );
    }

    @Test
    void pendencias_comPagamentosEmAberto_retornaListaMapeada() {
        // Arrange
        Aluno aluno = alunoMinimo();
        Pagamento pendente = pagamentoPendente(aluno);
        when(pagamentoRepository.findByAlunoAndPago(aluno, false))
            .thenReturn(List.of(pendente));

        // Act
        var response = portalController.pendencias(aluno);
        var body = response.getBody();

        // Assert
        assertAll(
            () -> assertEquals(1, body.size()),
            () -> assertEquals("outubro/2026", body.get(0).mes()),
            () -> assertEquals(350.0, body.get(0).valor())
        );
    }

    // ── avisos ───────────────────────────────────────────────────────────────

    @Test
    void avisos_alunoSemTurma_buscaComTurmaIdNulo() {
        // Arrange
        Aluno aluno = alunoMinimo(); // turma == null
        when(avisoRepository.findAvisosVisiveis(eq(aluno.getId()), isNull(), any(LocalDateTime.class)))
            .thenReturn(List.of());

        // Act
        var response = portalController.avisos(aluno);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(avisoRepository).findAvisosVisiveis(eq(aluno.getId()), isNull(), any(LocalDateTime.class));
    }

    @Test
    void avisos_alunoComTurma_buscaComTurmaId() {
        // Arrange
        Aluno aluno = alunoCompleto();
        UUID turmaId = aluno.getTurma().getId();
        when(avisoRepository.findAvisosVisiveis(eq(aluno.getId()), eq(turmaId), any(LocalDateTime.class)))
            .thenReturn(List.of());

        // Act
        portalController.avisos(aluno);

        // Assert
        verify(avisoRepository).findAvisosVisiveis(eq(aluno.getId()), eq(turmaId), any(LocalDateTime.class));
    }

    // ── boletim ──────────────────────────────────────────────────────────────

    @Test
    void boletim_semBimestre_buscaTodoOAno() {
        // Arrange
        Aluno aluno = alunoMinimo();
        int anoAtual = LocalDateTime.now().getYear();
        when(boletimRepository.findByAluno_IdAndAnoOrderByBimestreAscDisciplinaAsc(aluno.getId(), anoAtual))
            .thenReturn(List.of());

        // Act
        portalController.boletim(aluno, null, null);

        // Assert
        verify(boletimRepository).findByAluno_IdAndAnoOrderByBimestreAscDisciplinaAsc(aluno.getId(), anoAtual);
        verifyNoMoreInteractions(boletimRepository);
    }

    @Test
    void boletim_comBimestre_buscaBimestreEspecifico() {
        // Arrange
        Aluno aluno = alunoMinimo();
        when(boletimRepository.findByAluno_IdAndBimestreAndAnoOrderByDisciplinaAsc(aluno.getId(), 2, 2026))
            .thenReturn(List.of());

        // Act
        portalController.boletim(aluno, 2, 2026);

        // Assert
        verify(boletimRepository).findByAluno_IdAndBimestreAndAnoOrderByDisciplinaAsc(aluno.getId(), 2, 2026);
        verifyNoMoreInteractions(boletimRepository);
    }
}
