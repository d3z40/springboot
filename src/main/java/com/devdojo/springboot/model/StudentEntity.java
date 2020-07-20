package com.devdojo.springboot.model;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity(name = "student")
public class StudentEntity extends AbstractEntity {

    @NotEmpty(message = "This field can't be empty")
    private String name;

    @NotEmpty(message = "This field can't be empty")
    @Email(message = "E-mail have to be valid")
    private String email;

    public StudentEntity(@NotEmpty(message = "This field can't be empty") String name,
                         @NotEmpty(message = "This field can't be empty") @Email String email) {
        this.name = name;
        this.email = email;
    }

    public StudentEntity(Long id,
                         @NotEmpty(message = "This field can't be empty") String name,
                         @NotEmpty(message = "This field can't be empty") @Email String email) {
        super.setId(id);
        this.name = name;
        this.email = email;
    }

    public StudentEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "id='" + super.getId() + '\'' +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
