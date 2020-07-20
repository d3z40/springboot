package com.devdojo.springboot.endpoint;

import com.devdojo.springboot.error.ResourceNotFoundException;
import com.devdojo.springboot.model.StudentEntity;
import com.devdojo.springboot.repository.StudentRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1")
public class StudentEntityEndPoint {

    @Autowired
    private StudentRepository studentDAO;

//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                name = "Authorization",
//                    value = "Bearer token",
//                    required = true,
//                    dataType = "string",
//                    paramType = "header"
//            )
//    })
    @GetMapping(path = "/user/students")
    @ApiOperation(value = "Return a list with all Students", response = StudentEntity[].class)
    public ResponseEntity<Page<StudentEntity>> listAll(Pageable pageable) {
        return new ResponseEntity<>(studentDAO.findAll(pageable), HttpStatus.OK);
    }

//    @GetMapping(path = "/user/students/{id}")
//    public ResponseEntity<Optional<StudentEntity>> getStudentById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
//        System.out.println(userDetails.getUsername());
//
//        verifyStudentExists(id);
//
//        return new ResponseEntity<>(studentDAO.findById(id), HttpStatus.OK);
//    }

    @GetMapping(path = "/user/students/{id}")
    public ResponseEntity<Optional<StudentEntity>> getStudentById(@PathVariable("id") Long id, Authentication authentication) {
        System.out.println(authentication);

        verifyStudentExists(id);

        return new ResponseEntity<>(studentDAO.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/user/students/findByName/{name}")
    public ResponseEntity<Optional<List<StudentEntity>>> getStudentByName(@PathVariable("name") String name) {

        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/students")
    @Transactional(rollbackFor = Exception.class) //Obrigado informar quando for excessão do tipo checked
    public ResponseEntity<StudentEntity> save(@Validated @RequestBody StudentEntity studentEntity) {

        return new ResponseEntity<>(studentDAO.save(studentEntity), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/students/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<HttpStatus>> deleteById(@PathVariable Long id) {
        verifyStudentExists(id);

        studentDAO.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/students")
    public ResponseEntity<Optional<StudentEntity>> update(@RequestBody StudentEntity newStudentEntity) {
        verifyStudentExists(newStudentEntity.getId());

        return studentDAO.findById(newStudentEntity.getId())
                .map(studentEntity -> {
                    studentEntity.setName(newStudentEntity.getName());
                    studentEntity.setEmail(newStudentEntity.getEmail());
                    return new ResponseEntity(studentDAO.save(studentEntity), HttpStatus.OK);
                }).orElse(ResponseEntity.notFound().build());
    }

    public void verifyStudentExists(Long id) {
        Optional<StudentEntity> studentEntity = studentDAO.findById(id);

        if (!studentEntity.isPresent()) {
            throw new ResourceNotFoundException("Student not found for ID: " + id + " - by d3z40");
        }
    }
}
