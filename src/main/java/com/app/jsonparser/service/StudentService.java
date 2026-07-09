package com.app.jsonparser.service;

import com.app.jsonparser.dto.StudentInfoDTO;
import com.app.jsonparser.entity.StudentInfo;
import com.app.jsonparser.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
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

    public void addStudentInfo(StudentInfoDTO studentInfoDTO) {
        StudentInfo studentInfo = new StudentInfo();

        studentInfo.setEmpName(studentInfoDTO.empName);
        studentInfo.setDept(studentInfoDTO.dept);
        studentInfo.setCity(studentInfoDTO.city);
        studentInfo.setMobileNum(studentInfoDTO.mobileNum);

        studentRepo.save(studentInfo);
    }

    public StudentInfoDTO getStudentInfoById(long studentId) {
        StudentInfo studentInfo = studentRepo.findById(studentId).orElse(null);

        if (studentInfo == null) {
            System.out.println("No student info found for id : " + studentId);
            return null;
        }

        return convertToDTO(studentInfo);
    }

    public List<StudentInfoDTO> getAllStudents() {
        List<StudentInfoDTO> studentInfoDTOS = new ArrayList<>();

        List<StudentInfo> studentInfoList = studentRepo.findAll();

        for (StudentInfo studentInfo : studentInfoList) {
            studentInfoDTOS.add(convertToDTO(studentInfo));
        }

        return studentInfoDTOS;
    }

    public StudentInfoDTO convertToDTO(StudentInfo student) {
        StudentInfoDTO studentInfoDTO = new StudentInfoDTO();

        studentInfoDTO.id = student.getId();
        studentInfoDTO.empName = student.getEmpName();
        studentInfoDTO.dept = student.getDept();
        studentInfoDTO.city = student.getCity();
        studentInfoDTO.mobileNum = student.getMobileNum();

        return studentInfoDTO;
    }

    public List<StudentInfo> saveStudentsFromJsonFile(MultipartFile file) {
        List<StudentInfo> students = getStudentsFromJsonFile(file);
        return studentRepo.saveAll(students);
    }

    private List<StudentInfo> getStudentsFromJsonFile(MultipartFile file) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(
                    file.getInputStream(),
                    new TypeReference<List<StudentInfo>>() {}
            );


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read json file!");
        }
    }

    public List<StudentInfo> addStudentsFromCsv(MultipartFile multipartFile) {
        List<StudentInfo> studentInfoList = getStudentDataFromCsvFile(multipartFile);
        return studentRepo.saveAll(studentInfoList);
    }

    private List<StudentInfo> getStudentDataFromCsvFile(MultipartFile multipartFile) {

        List<StudentInfo> studentInfoList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {

            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");

                StudentInfo studentInfo = new StudentInfo(words[0], words[1], words[2], Long.parseLong(words[3]));
                studentInfoList.add(studentInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentInfoList;
    }

}
