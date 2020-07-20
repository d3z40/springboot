package com.devdojo.springboot.endpoint;

import com.devdojo.springboot.error.CustomeError;
import com.devdojo.springboot.model.Student;
import com.devdojo.springboot.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("studentso")
public class StudentEndPoint {

    @Autowired
    private DateUtil dateUtil;

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    private ResponseEntity<?> listAll() {
        //System.out.println("-------" + dateUtil.formatLocalDateTimeToDataseStyle(LocalDateTime.now()));

        return new ResponseEntity<>(Student.studentList, HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @GetMapping(path = "/{id}")
    private ResponseEntity<?> getStudentById(@PathVariable("id") int id) {
        Student student = new Student();
        student.setId(id);

        int index = Student.studentList.indexOf(student);
        if (index == -1)
            return new ResponseEntity<>(new CustomeError("Student not found"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(Student.studentList.get(index), HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.POST)
    @PostMapping
    private ResponseEntity<?> save(@RequestBody Student student) {
        Student.studentList.add(student);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping
    private ResponseEntity<?> delete(@RequestBody Student student) {
        Student.studentList.remove(student);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(method = RequestMethod.PUT)
    @PutMapping
    private ResponseEntity<?> update(@RequestBody Student student) {
        Student.studentList.remove(student);
        Student.studentList.add(student);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }
}