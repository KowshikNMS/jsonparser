package com.app.jsonparser.service;

import com.app.jsonparser.dto.StudentDTO;
import com.app.jsonparser.dto.SubjectDTO;
import com.app.jsonparser.dto.SubjectRemoveReqDTO;
import com.app.jsonparser.entity.Student;
import com.app.jsonparser.entity.Subject;
import com.app.jsonparser.repo.StudentRepo;
import com.app.jsonparser.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private SubjectRepo subjectRepo;

    public void addStudentInfo(StudentDTO studentDTO) {
        Student student = new Student();

        student.setName(studentDTO.name);
        student.setDept(studentDTO.dept);
        student.setCity(studentDTO.city);
        student.setMobileNum(studentDTO.mobileNum);

        List<Subject> subjects = new ArrayList<>();
        for (SubjectDTO subjectDTO : studentDTO.subjectDTOs) {
            Subject subject = new Subject();
            subject.setSubjectName(subjectDTO.subjectName);
            subject.setMarks(subjectDTO.marks);
            subject.setStudent(student);

            subjects.add(subject);
        }

        student.getSubjects().addAll(subjects);

        studentRepo.save(student);
    }

    public StudentDTO getStudentInfoById(long studentId) {
        Student studentInfo = studentRepo.findById(studentId).orElse(null);

        if (studentInfo == null) {
            System.out.println("No student info found for id : " + studentId);
            return null;
        }

        return convertToDTO(studentInfo);
    }

    public List<StudentDTO> getAllStudents() {
        List<StudentDTO> studentInfoDTOS = new ArrayList<>();

        List<Student> studentInfoList = studentRepo.findAll();

        for (Student studentInfo : studentInfoList) {
            studentInfoDTOS.add(convertToDTO(studentInfo));
        }

        return studentInfoDTOS;
    }

    public StudentDTO convertToDTO(Student student) {
        StudentDTO studentInfoDTO = new StudentDTO();

        studentInfoDTO.id = student.getId();
        studentInfoDTO.name = student.getName();
        studentInfoDTO.dept = student.getDept();
        studentInfoDTO.city = student.getCity();
        studentInfoDTO.mobileNum = student.getMobileNum();

        studentInfoDTO.subjectDTOs = new ArrayList<>();

        for (Subject subject : student.getSubjects()) {
            SubjectDTO subjectDTO = new SubjectDTO();

            subjectDTO.id = subject.getId();
            subjectDTO.subjectName = subject.getSubjectName();
            subjectDTO.marks = subject.getMarks();

            studentInfoDTO.subjectDTOs.add(subjectDTO);
        }

        return studentInfoDTO;
    }

    public List<Student> saveStudentsFromJsonFile(MultipartFile file) {
        List<StudentDTO> studentDTOs = getStudentsFromJsonFile(file);
        return uploadStudents(studentDTOs);
    }

    private List<Student> uploadStudents(List<StudentDTO> studentDTOs) {

        List<Student> students = new ArrayList<>();

        for (StudentDTO studentDTO : studentDTOs) {
            Student student = new Student();

            student.setName(studentDTO.name);
            student.setDept(studentDTO.dept);
            student.setCity(studentDTO.city);
            student.setMobileNum(studentDTO.mobileNum);

            for (SubjectDTO subjectDTO : studentDTO.subjectDTOs) {
                Subject subject = new Subject();

                subject.setSubjectName(subjectDTO.subjectName);
                subject.setMarks(subjectDTO.marks);
                subject.setStudent(student);

                student.getSubjects().add(subject);
            }

            students.add(student);
        }

        return studentRepo.saveAll(students);
    }

    private List<StudentDTO> getStudentsFromJsonFile(MultipartFile file) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(
                    file.getInputStream(),
                    new TypeReference<List<StudentDTO>>() {}
            );


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read json file!");
        }
    }

    public List<Student> addStudentsFromCsv(MultipartFile multipartFile) {
        List<Student> studentInfoList = getStudentDataFromCsvFile(multipartFile);
        return studentRepo.saveAll(studentInfoList);
    }

    private List<Student> getStudentDataFromCsvFile(MultipartFile multipartFile) {

        List<Student> studentInfoList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {

            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");

                Student studentInfo = new Student(words[0], words[1], words[2], Long.parseLong(words[3]));
                studentInfoList.add(studentInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentInfoList;
    }

    public void addSubjectForStudent(long studentId, SubjectDTO subjectDTO) {
        Student student = studentRepo.findById(studentId).orElse(null);

        if (student == null) {
            throw new RuntimeException("No student exists for student id : " + studentId);
        }

        Subject subject = new Subject();
        subject.setSubjectName(subjectDTO.subjectName);
        subject.setMarks(subjectDTO.marks);
        subject.setStudent(student);

        student.getSubjects().add(subject);

        studentRepo.save(student);
    }

    public void removeSubject(SubjectRemoveReqDTO dto) {
        System.out.println("Remove subject called");
        Student student = studentRepo.findById(dto.studentId)
                .orElse(null);

        if (student == null) {
            System.out.println("No student available for id : " + dto.studentId);
            return;
        }

        Subject subjectToRemove = null;
        for (Subject subject : student.getSubjects()) {
            if (subject.getId() == dto.subjectId) {
                subjectToRemove = subject;
                break;
            }
        }

        if (subjectToRemove == null) {
            System.out.println("No subject available for student - " + student.getName() + " with id : " + dto.subjectId);
            return;
        }

        student.getSubjects().remove(subjectToRemove);

        studentRepo.save(student);
    }

}
