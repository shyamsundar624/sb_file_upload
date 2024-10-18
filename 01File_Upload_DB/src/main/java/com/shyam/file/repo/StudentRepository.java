package com.shyam.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyam.file.entity.Student;

public interface StudentRepository extends JpaRepository<Student,Long>{

}
