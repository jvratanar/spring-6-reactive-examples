package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {
    PersonRepository personRepository = new PersonRepositoryImpl();
    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = this.personRepository.getById(1);

        // it is not preferred to get entity from Mono this way
        // as it is blocking
        Person person = personMono.block();
        System.out.println(
                person.toString()
        );
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = this.personRepository.getById(1);
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = this.personRepository.getById(1);
        personMono.map(Person::getFirstName).subscribe(firstName -> {
            System.out.println(firstName);
        });
    }

    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = this.personRepository.findAll();

        // It is not preferred to get entity from
        // Flux this was as it is blocking
        Person person = personFlux.blockFirst();
        System.out.println(
                person.toString()
        );
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = this.personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = this.personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(firstName -> {
            System.out.println(firstName);
        });
    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = this.personRepository.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();

        listMono.subscribe(listPerson -> {
            listPerson.forEach(person -> {
                System.out.println(person.getFirstName());
            });
        });
    }

    @Test
    void testFilterOnName() {
        this.personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testGetById() {
        Mono<Person> fionaMono = this.personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .next();

        fionaMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> Objects.equals(person.getId(), id)).single()
                .doOnError(throwable -> {
                    System.out.println("Error occurred in flux");
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        }, throwable -> {
            System.out.println("Error occurred in the mono");
            System.out.println(throwable.toString());
        });
    }
}