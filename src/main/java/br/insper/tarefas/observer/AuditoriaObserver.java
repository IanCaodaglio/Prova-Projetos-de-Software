package br.insper.tarefas.observer;

import org.springframework.stereotype.Component;

/** Observer 1: persiste cada CREATE/DELETE na tabela auditoria. */
@Component
public class AuditoriaObserver implements TarefaObserver {

    private final AuditoriaRepository repository;

    public AuditoriaObserver(AuditoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void atualizar(TarefaEvento evento) {
        Auditoria auditoria = new Auditoria();
        auditoria.setOperacao(evento.operacao());
        auditoria.setDataHora(evento.momento());
        auditoria.setTarefaId(evento.tarefa().getId());
        repository.save(auditoria);
    }
}
