package com.app.jsonparser.controller;

import com.app.jsonparser.dto.StudentDTO;
import com.app.jsonparser.dto.SubjectDTO;
import com.app.jsonparser.dto.SubjectRemoveReqDTO;
import com.app.jsonparser.entity.Student;
import com.app.jsonparser.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class StudentRestController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/api/print")
    public String printCheckMsg() {
        return "Hello kowshik";
    }

    @PostMapping("/student/add")
    public void addStudentInfo(@RequestBody StudentDTO studentInfoDTO) {
        studentService.addStudentInfo(studentInfoDTO);
    }

    @GetMapping("/student/get/{studentId}")
    public StudentDTO getStudentById(@PathVariable long studentId) {
        return studentService.getStudentInfoById(studentId);
    }

    @GetMapping("/students/getAll")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/students/addFromFile")
    public List<Student> addStudentsFromCsvFile(@RequestParam("file")MultipartFile file) {
        return studentService.addStudentsFromCsv(file);
    }

    @PostMapping("/students/addFromJson")
    public List<Student> addStudentsFromJson(@RequestParam("file") MultipartFile file) throws Exception {
        return studentService.saveStudentsFromJsonFile(file);
    }

    @PostMapping("/student/subject/add/{studentId}")
    public void addNewSubjectForStudent(@PathVariable long studentId, @RequestBody SubjectDTO subjectDTO) {
        studentService.addSubjectForStudent(studentId, subjectDTO);
    }

    @PostMapping("/subject/remove")
    public void removeSubject(@RequestBody SubjectRemoveReqDTO dto) {
        studentService.removeSubject(dto);
    }

}
