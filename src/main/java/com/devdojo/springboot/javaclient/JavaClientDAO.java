package com.devdojo.springboot.javaclient;

import com.devdojo.springboot.handles.RestResponseExceptionHandler;
import com.devdojo.springboot.model.PageableResponse;
import com.devdojo.springboot.model.StudentEntity;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class JavaClientDAO {

    private RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/user/students")
            .basicAuthentication("bruce.wayne", "batman")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
            .rootUri("http://localhost:8080/v1/admin/students")
            .basicAuthentication("clark.kent", "superman")
            .errorHandler(new RestResponseExceptionHandler())
            .build();

    public StudentEntity findById (Long id) {
//        ResponseEntity<StudentEntity> forEntity = restTemplate.getForEntity("/{id}", StudentEntity.class, id);
        return restTemplate.getForObject("/{id}", StudentEntity.class, id);
    }

    public List<StudentEntity> listAll () {
//        StudentEntity[] studentsEntity = restTemplate.getForObject("/", StudentEntity[].class);
//        ResponseEntity<List<StudentEntity>> exchange = restTemplate
//                .exchange("/", HttpMethod.GET, null, new ParameterizedTypeReference<List<StudentEntity>>() {});

        ResponseEntity<PageableResponse<StudentEntity>> exchange = restTemplate
                .exchange("/?sort=id,asc&sort=name,desc", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<StudentEntity>>() {
                });

        return exchange.getBody().getContent();
    }

    public StudentEntity save(StudentEntity studentEntity) {
//        StudentEntity studentEntityPostForObject = restTemplateAdmin.postForObject("/", studentEntity, StudentEntity.class);
//        ResponseEntity<StudentEntity> studentEntityResponseEntity = restTemplateAdmin.postForEntity("/", studentEntity, StudentEntity.class);

        ResponseEntity<StudentEntity> exchangePost = restTemplateAdmin
                .exchange("/", HttpMethod.POST, new HttpEntity<>(studentEntity, createHeaders()), StudentEntity.class);

        return exchangePost.getBody();
    }

    public void update (StudentEntity studentEntity) {
        restTemplateAdmin.put("/", studentEntity);
    }

    public void delete (Long id) {
        restTemplateAdmin.delete("/{id}", id);
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
