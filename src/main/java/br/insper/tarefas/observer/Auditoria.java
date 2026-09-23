package br.insper.tarefas.observer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Operacao operacao;

    // "timestamp" é palavra reservada em alguns bancos; por isso o nome dataHora
    @Column(nullable = false)
    private LocalDateTime dataHora;

    private Long tarefaId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Operacao getOperacao() { return operacao; }
    public void setOperacao(Operacao operacao) { this.operacao = operacao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Long getTarefaId() { return tarefaId; }
    public void setTarefaId(Long tarefaId) { this.tarefaId = tarefaId; }
}
