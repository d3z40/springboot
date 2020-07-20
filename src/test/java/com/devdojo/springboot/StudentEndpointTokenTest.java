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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
public class StudentEndpointTokenTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    private HttpEntity<Void> userHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @BeforeEach
    public void configUserHeaders() {
        //{"username":"bruce.wayne","password":"batman"}
        String jsonAcesso = "{\"username\":\"bruce.wayne\",\"password\":\"batman\"}";

        HttpHeaders headers = testRestTemplate.postForEntity("/login", jsonAcesso, String.class).getHeaders();
        this.userHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void configAdminHeaders() {
        //{"username":"clark.kent","password":"superman"}
        String jsonAcesso = "{\"username\":\"clark.kent\",\"password\":\"superman\"}";

        HttpHeaders headers = testRestTemplate.postForEntity("/login", jsonAcesso, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void configWrongHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "0123456789");
        this.wrongHeader = new HttpEntity<>(headers);
    }

    @BeforeEach
    public void setup() {
        StudentEntity studentEntity = new StudentEntity(1L, "Lara Croft", "laracroft@gmail.com");
        BDDMockito.when(studentRepository.findById(studentEntity.getId())).thenReturn(Optional.of(studentEntity));
    }

    @Test
    public void listStudentWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = this.testRestTemplate.exchange("/v1/user/students/", HttpMethod.GET, this.wrongHeader, String.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getStudentByIdWhenTokenIsIncorrectShouldReturnStatusCode403() {
        ResponseEntity<String> response = this.testRestTemplate.exchange("/v1/user/students/1", HttpMethod.GET, this.wrongHeader, String.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void listStudentWhenTokenIsCorrectShouldReturnStatusCode200() {
        ResponseEntity<String> response = this.testRestTemplate.exchange("/v1/user/students/1", HttpMethod.GET, this.userHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentByIdWhenTokenIsCorrectShouldReturnStatusCode200() {
        ResponseEntity<String> response = this.testRestTemplate.exchange("/v1/user/students/1", HttpMethod.GET, this.userHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getStudentByIdWhenTokenIsCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
        ResponseEntity<String> response = this.testRestTemplate.exchange("/v1/user/students/2", HttpMethod.GET, this.userHeader, String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, this.adminHeader, String.class, 1L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() {
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> exchange = testRestTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, this.adminHeader, String.class, 2L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsWithMockMvcShouldReturnStatusCode404() throws Exception {
        String token = this.adminHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}", 2L)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteWhenUserHasDoesNotHaveRoleAdminWithMockMvcShouldReturnStatusCode403() throws Exception {
        String token = this.userHeader.getHeaders().get("Authorization").get(0);
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}", 1L)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() {
        StudentEntity studentEntity = new StudentEntity(1L, null, "laracroft@gmail.com");
        BDDMockito.when(studentRepository.save(studentEntity)).thenReturn(studentEntity);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                "/v1/admin/students/",
                HttpMethod.POST,
                new HttpEntity<>(studentEntity, this.adminHeader.getHeaders()),
                String.class);

        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertThat(responseEntity.getBody()).contains("fieldMessage", "This field can't be empty");
    }

    @Test
    public void createShouldPersistDateAndReturnStatusCode201() {
        StudentEntity studentEntity = new StudentEntity(1L, "Lara Croft", "laracroft@gmail.com");
        BDDMockito.when(studentRepository.save(studentEntity)).thenReturn(studentEntity);

        ResponseEntity<StudentEntity> responseEntity = testRestTemplate.exchange(
                "/v1/admin/students/",
                HttpMethod.POST,
                new HttpEntity<>(studentEntity, this.adminHeader.getHeaders()),
                StudentEntity.class);

        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        Assertions.assertThat(responseEntity.getBody().getId()).isNotNull();
    }
}