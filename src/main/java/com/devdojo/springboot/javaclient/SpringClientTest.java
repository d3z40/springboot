package com.devdojo.springboot.javaclient;

import com.devdojo.springboot.model.StudentEntity;

public class SpringClientTest {

    public static void main(String[] args) {
        JavaClientDAO clientDAO = new JavaClientDAO();
//        System.out.println(clientDAO.findById(1L));
//        System.out.println(clientDAO.listAll());

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setName("Soldier Ryan");
        studentEntity.setEmail("ryan_soldier@gmail.com");

        studentEntity = clientDAO.save(studentEntity);

        System.out.println("Student added: " + studentEntity);

        studentEntity.setName("Soldier Ryan Conan");
        studentEntity.setEmail("ryan__conan_soldier@gmail.com");

        clientDAO.update(studentEntity);

        System.out.println("Student updated: " + clientDAO.findById(studentEntity.getId()));

        clientDAO.delete(studentEntity.getId());

        System.out.println("Student deleted: " + clientDAO.findById(studentEntity.getId()));
    }
}