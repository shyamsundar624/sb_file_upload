package com.shyam.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyam.file.entity.File;

public interface FileRepository extends JpaRepository<File, Long>{

}
