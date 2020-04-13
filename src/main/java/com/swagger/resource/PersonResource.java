package com.swagger.resource;

import com.swagger.entity.Person;
import com.swagger.repository.PersonRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class PersonResource {

    @Autowired
    private PersonRepository personRepository;


    @ApiOperation(value = "Retorna uma lista de pessoas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a lista de pessoas"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok(personRepository.findAll());
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Person> findById(@PathVariable("id") long id) {

        Optional<Person> person = personRepository.findById(id);

        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping(produces = "application/json", consumes = "application/json")
    public Person create(@Valid @RequestBody Person person) {
        return personRepository.save(person);
    }


    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Person> update(@PathVariable("id") long id, @Valid @RequestBody Person newPerson) {

        Optional<Person> oldPerson = personRepository.findById(id);

        if (oldPerson.isPresent()) {
            Person person = oldPerson.get();
            person.setName(newPerson.getName());
            personRepository.save(person);
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
