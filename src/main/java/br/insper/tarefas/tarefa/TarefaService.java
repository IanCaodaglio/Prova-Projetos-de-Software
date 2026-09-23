package br.insper.tarefas.tarefa;

import br.insper.tarefas.observer.Operacao;
import br.insper.tarefas.observer.TarefaEvento;
import br.insper.tarefas.observer.TarefaObserver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository repository;
    private final List<TarefaObserver> observers;

    public TarefaService(TarefaRepository repository, List<TarefaObserver> observers) {
        this.repository = repository;
        this.observers = observers;
    }

    public Tarefa criar(Tarefa tarefa) {
        if (tarefa.getStatus() == null) {
            tarefa.setStatus(Status.TODO);
        }
        tarefa.setDataCriacao(LocalDateTime.now());
        Tarefa salva = repository.save(tarefa);
        notificar(Operacao.CREATE, salva);
        return salva;
    }

    public List<Tarefa> listar() {
        return repository.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    public void excluir(Long id) {
        Tarefa tarefa = buscarPorId(id);
        repository.deleteById(id);
        notificar(Operacao.DELETE, tarefa);
    }

    private void notificar(Operacao operacao, Tarefa tarefa) {
        TarefaEvento evento = new TarefaEvento(operacao, tarefa, LocalDateTime.now());
        for (TarefaObserver observer : observers) {
            observer.atualizar(evento);
        }
    }
}
