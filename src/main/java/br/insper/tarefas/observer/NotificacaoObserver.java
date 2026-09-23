package br.insper.tarefas.observer;

import br.insper.tarefas.tarefa.Prioridade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Observer 2: alerta no log quando uma tarefa de prioridade ALTA é criada. */
@Component
public class NotificacaoObserver implements TarefaObserver {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoObserver.class);

    @Override
    public void atualizar(TarefaEvento evento) {
        if (evento.operacao() == Operacao.CREATE
                && evento.tarefa().getPrioridade() == Prioridade.ALTA) {
            log.warn("ALERTA: tarefa de prioridade ALTA criada - id={}, titulo='{}'",
                    evento.tarefa().getId(), evento.tarefa().getTitulo());
        }
    }
}
