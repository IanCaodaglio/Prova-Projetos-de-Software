package br.insper.tarefas.tarefa;

import br.insper.tarefas.observer.Operacao;
import br.insper.tarefas.observer.TarefaEvento;
import br.insper.tarefas.observer.TarefaObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository repository;

    @Mock
    private TarefaObserver observer;

    private TarefaService service;

    @BeforeEach
    void setUp() {
        service = new TarefaService(repository, List.of(observer));
    }

    @Test
    void criar_semStatus_defineTodoENotificaObservers() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Estudar");
        tarefa.setPrioridade(Prioridade.MEDIA);

        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> {
            Tarefa t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        Tarefa resultado = service.criar(tarefa);

        assertThat(resultado.getStatus()).isEqualTo(Status.TODO);
        assertThat(resultado.getDataCriacao()).isNotNull();

        ArgumentCaptor<TarefaEvento> captor = ArgumentCaptor.forClass(TarefaEvento.class);
        verify(observer).atualizar(captor.capture());
        assertThat(captor.getValue().operacao()).isEqualTo(Operacao.CREATE);
        assertThat(captor.getValue().tarefa()).isEqualTo(resultado);
    }

    @Test
    void criar_comStatus_mantemStatusInformado() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Revisar PR");
        tarefa.setPrioridade(Prioridade.BAIXA);
        tarefa.setStatus(Status.DOING);

        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        Tarefa resultado = service.criar(tarefa);

        assertThat(resultado.getStatus()).isEqualTo(Status.DOING);
    }

    @Test
    void listar_retornaTodasAsTarefas() {
        when(repository.findAll()).thenReturn(List.of(new Tarefa(), new Tarefa()));

        List<Tarefa> resultado = service.listar();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void buscarPorId_existente_retornaTarefa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

        Tarefa resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_inexistente_lancaExcecao() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void excluir_existente_deletaENotificaObservers() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

        service.excluir(1L);

        verify(repository).deleteById(1L);

        ArgumentCaptor<TarefaEvento> captor = ArgumentCaptor.forClass(TarefaEvento.class);
        verify(observer).atualizar(captor.capture());
        assertThat(captor.getValue().operacao()).isEqualTo(Operacao.DELETE);
    }

    @Test
    void excluir_inexistente_lancaExcecaoENaoDeleta() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(99L))
                .isInstanceOf(ResponseStatusException.class);

        verify(repository, never()).deleteById(anyLong());
    }
}
