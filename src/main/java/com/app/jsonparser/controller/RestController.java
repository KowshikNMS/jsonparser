package com.app.jsonparser.controller;

import com.app.jsonparser.dto.StudentInfoDTO;
import com.app.jsonparser.entity.StudentInfo;
import com.app.jsonparser.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/api/print")
    public String printCheckMsg() {
        return "Hello kowshik";
    }

    @PostMapping("/student/add")
    public void addStudentInfo(@RequestBody StudentInfoDTO studentInfoDTO) {
        studentService.addStudentInfo(studentInfoDTO);
    }

    @GetMapping("/student/get/{studentId}")
    public StudentInfoDTO getStudentById(@PathVariable long studentId) {
        return studentService.getStudentInfoById(studentId);
    }

    @GetMapping("/students/getAll")
    public List<StudentInfoDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/students/addFromFile")
    public List<StudentInfo> addStudentsFromCsvFile(@RequestParam("file")MultipartFile file) {
        return studentService.addStudentsFromCsv(file);
    }

    @PostMapping("/students/addFromJson")
    public List<StudentInfo> addStudentsFromJson(@RequestParam("file") MultipartFile file) throws Exception {
        return studentService.saveStudentsFromJsonFile(file);
    }

}
