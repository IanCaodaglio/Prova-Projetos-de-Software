package br.insper.tarefas.observer;

import br.insper.tarefas.tarefa.Tarefa;

import java.time.LocalDateTime;

/** O que o Observable (TarefaService) entrega para cada Observer. */
public record TarefaEvento(Operacao operacao, Tarefa tarefa, LocalDateTime momento) {
}
