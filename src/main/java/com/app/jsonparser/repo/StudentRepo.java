package com.app.jsonparser.repo;

import com.app.jsonparser.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<StudentInfo, Long> {
}
