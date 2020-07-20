package com.devdojo.springboot.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Student {

    private int id;
    private String name;
    public static List<Student> studentList;

    static {
        studentRepository();
    }

    public Student() {}

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void studentRepository() {
        studentList = new ArrayList<Student>(Arrays.asList(new Student(1, "Maria"),
                new Student(2, "Joana"),
                new Student(3, "Mariana"),
                new Student(4, "Joao")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return getId() == student.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
