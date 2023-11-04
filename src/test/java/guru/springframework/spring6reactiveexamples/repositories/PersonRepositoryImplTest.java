package guru.springframework.spring6reactiveexamples.repositories;

import guru.springframework.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {
    PersonRepository personRepository = new PersonRepositoryImpl();
    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = this.personRepository.getById(1);

        // it is not preffered to get entity from Mono this way
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
        personMono.map(person -> {
           return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println(firstName);
        });
    }
}