package com.app.jsonparser.repo;

import com.app.jsonparser.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long> {
}
