package com.devdojo.springboot.repository;

import com.devdojo.springboot.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    Optional<List<StudentEntity>> findByNameIgnoreCaseContaining(String name);
}
