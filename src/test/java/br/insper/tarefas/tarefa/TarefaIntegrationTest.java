package br.insper.tarefas.tarefa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Teste de integração pedido na Pergunta 3: rota de criação (POST), subindo o
// contexto Spring inteiro (controller -> service -> observers -> H2 em memória).
@SpringBootTest
@AutoConfigureMockMvc
class TarefaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criar_retornaCreatedComTarefaPersistida() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Preparar prova");
        tarefa.setDescricao("Revisar Observer e Spring Boot");
        tarefa.setPrioridade(Prioridade.ALTA);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Preparar prova"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.prioridade").value("ALTA"));
    }
}
