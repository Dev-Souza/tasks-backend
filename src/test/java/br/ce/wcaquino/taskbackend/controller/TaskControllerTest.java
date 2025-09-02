package br.ce.wcaquino.taskbackend.controller;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskRepo todoRepo;

    @InjectMocks
    private TaskController controller;

    @Test
    public void naoDeveSalvarTarefaSemDescricao() {
        Task todo = new Task();
        todo.setDueDate(LocalDate.now());

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.save(todo));
        assertEquals("Fill the task description", exception.getMessage());
    }

    @Test
    public void naoDeveSalvarTarefaSemData() {
        Task todo = new Task();
        todo.setTask("Descrição");

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.save(todo));
        assertEquals("Fill the due date", exception.getMessage());
    }

    @Test
    public void naoDeveSalvarTarefaComDataDoPassado() {
        Task todo = new Task();
        todo.setTask("Descrição");
        todo.setDueDate(LocalDate.of(2010, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.save(todo));
        assertEquals("Due date must not be in past", exception.getMessage());
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws ValidationException {
        Task todo = new Task();
        todo.setTask("Descrição da tarefa");
        todo.setDueDate(LocalDate.now().plusDays(1));

        controller.save(todo);

        // Verifica se o repositório foi chamado para salvar a tarefa
        verify(todoRepo).save(todo);
    }
}