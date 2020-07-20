package com.devdojo.springboot;

import com.devdojo.springboot.model.StudentEntity;
import com.devdojo.springboot.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void createShouldPersistData() {
        StudentEntity studentEntity = new StudentEntity("André", "d3z4013@gmail.com");
        studentRepository.save(studentEntity);

        Assertions.assertThat(studentEntity.getId()).isNotNull();
        Assertions.assertThat(studentEntity.getName()).isEqualTo("André");
        Assertions.assertThat(studentEntity.getEmail()).isEqualTo("d3z4013@gmail.com");
    }

    @Test
    public void deleteShouldRemoveData() {
        StudentEntity studentEntity = new StudentEntity("André", "d3z4013@gmail.com");
        studentRepository.save(studentEntity);
        studentRepository.delete(studentEntity);

        Assertions.assertThat(studentRepository.findById(studentEntity.getId())).isNotNull();
    }

    @Test
    public void updateShouldUpdateData() {
        StudentEntity studentEntity = new StudentEntity("André", "d3z4013@gmail.com");
        studentRepository.save(studentEntity);

        Assertions.assertThat(studentEntity.getId()).isNotNull();
        Assertions.assertThat(studentEntity.getName()).isEqualTo("André");
        Assertions.assertThat(studentEntity.getEmail()).isEqualTo("d3z4013@gmail.com");

        studentEntity.setName("D3z40");
        studentEntity.setEmail("d3z4013@gmail.com.br");
        studentRepository.save(studentEntity);
        Optional<StudentEntity> studentEntityOptional = studentRepository.findById(studentEntity.getId());

        Assertions.assertThat(studentEntityOptional.get().getId()).isNotNull();
        Assertions.assertThat(studentEntityOptional.get().getName()).isEqualTo("D3z40");
        Assertions.assertThat(studentEntityOptional.get().getEmail()).isEqualTo("d3z4013@gmail.com.br");
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCaseContainingData() {
        StudentEntity studentEntity1 = new StudentEntity("KVRA", "kvra@gmail.com");
        StudentEntity studentEntity2 = new StudentEntity("Kvra", "kvra@gmail.com");
        studentRepository.save(studentEntity1);
        studentRepository.save(studentEntity2);

        Optional<List<StudentEntity>> listStdent = studentRepository.findByNameIgnoreCaseContaining("kvra");
        List<StudentEntity> studentEntities = listStdent.get();

        Assertions.assertThat(studentEntities.size()).isEqualTo(2);
    }

    @Test
    public void whenNotEmptyName_thenNoConstraintViolations () {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentRepository.save(new StudentEntity("", "jana.costa@gmail.com")));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("This field can't be empty"));
    }

    @Test
    public void whenNotEmptyEmail_thenNoConstraintViolations () {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentRepository.save(new StudentEntity("Janaina", "")));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("This field can't be empty"));
    }

    @Test
    public void whenValidEmail_thenNoConstraintViolations () {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentRepository.save(new StudentEntity("Janaina", "jana.gmail.com")));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("E-mail have to be valid"));
    }
}
