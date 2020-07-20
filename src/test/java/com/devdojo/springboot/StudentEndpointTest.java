package com.devdojo.springboot;

import com.devdojo.springboot.model.StudentEntity;
import com.devdojo.springboot.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(StudentEndpointTest.Config.class) //Não precisa anotar quando é uma Inner Class
public class StudentEndpointTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().basicAuthentication("clark.kent", "superman");
        }
    }

    @BeforeEach
    public void setup() {
        StudentEntity studentEntity = new StudentEntity(1L, "Lara Croft", "laracroft@gmail.com");
        BDDMockito.when(studentRepository.findById(studentEntity.getId())).thenReturn(Optional.of(studentEntity));
    }

    @Test
    public void listStudentWhenUserNameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        this.testRestTemplate = this.testRestTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/v1/user/students/", String.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void getStudentByIdWhenUserNameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        this.testRestTemplate = this.testRestTemplate.withBasicAuth("1", "1");
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/v1/user/students/1", String.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void listStudentWhenUserNameAndPasswordAreCorrectShouldReturnStatusCode200() {
        List<StudentEntity> studentEntities = Arrays.asList(new StudentEntity(1L, "Lara Croft", "laracroft@gmail.com"),
                new StudentEntity(2L, "Super Man", "superman@gmail.com"));
        BDDMockito.when(studentRepository.findAll()).thenReturn(studentEntities);

        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/v1/user/students/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentByIdWhenUserNameAndPasswordAreCorrectShouldReturnStatusCode200() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/v1/user/students/{id}", String.class, 1L);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentByIdWhenUserNameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/v1/user/students/{id}", String.class, 2020);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 2L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    @WithMockUser(username = "Nao eh necessario", password = "Nao eh necessario", roles = {"USER", "ADMIN"})
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsWithMockMvcShouldReturnStatusCode404() throws Exception {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}", 2L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "Nao eh necessario", password = "Nao eh necessario", roles = {"USER"})
    public void deleteWhenUserHasDoesNotHaveRoleAdminWithMockMvcShouldReturnStatusCode403() throws Exception {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() {
        StudentEntity studentEntity = new StudentEntity(1L, null, "laracroft@gmail.com");
        BDDMockito.when(studentRepository.save(studentEntity)).thenReturn(studentEntity);

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/v1/admin/students/", studentEntity, String.class);

        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(responseEntity.getBody()).contains("fieldMessage", "This field can't be empty");
    }

    @Test
    public void createShouldPersistDateAndReturnStatusCode201() {
        StudentEntity studentEntity = new StudentEntity(1L, "Lara Croft", "laracroft@gmail.com");
        BDDMockito.when(studentRepository.save(studentEntity)).thenReturn(studentEntity);

        ResponseEntity<StudentEntity> responseEntity = testRestTemplate.postForEntity("/v1/admin/students/", studentEntity, StudentEntity.class);

        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
    }
}