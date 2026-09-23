package br.insper.tarefas.observer;

/**
 * Padrão Observer: toda classe que quer reagir a eventos de Tarefa implementa esta interface.
 * O Spring injeta TODAS as implementações (@Component) no TarefaService (o Observable).
 * Para adicionar uma nova reação, basta criar outro @Component - o service não muda.
 */
public interface TarefaObserver {
    void atualizar(TarefaEvento evento);
}
