package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import application.model.Tarefa;
import application.repository.TarefaRepository;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    @GetMapping
    public Iterable<Tarefa> list() {
        return tarefaRepository.findAll();
    }

    @PostMapping
    public Tarefa insert(@RequestBody Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    @GetMapping("/{id}")
    public Tarefa details(@PathVariable long id) {
        Optional<Tarefa> resultado = tarefaRepository.findById(id);
        if(resultado.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tarefa não encontrada"
            );
        }
        return resultado.get();
    }

    @PutMapping("/{id}")
    public Tarefa put(
        @PathVariable long id,
        @RequestBody Tarefa novaTarefa) {
        Optional<Tarefa> resultado = tarefaRepository.findById(id);

        if(resultado.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tarefa não encontrada"
            );
        }

        if(novaTarefa.getDescricao().isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Tarefa não possui descricao"
            );
        }

        resultado.get().setDescricao(novaTarefa.getDescricao());
        resultado.get().setConcluida(novaTarefa.getConcluida());
        return tarefaRepository.save(resultado.get());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        if(!tarefaRepository.existsById(id)){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tarefa não encontrada"
            );
        }
        tarefaRepository.deleteById(id);
    }

}
