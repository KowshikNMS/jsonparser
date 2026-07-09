package com.app.jsonparser.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "student_info")
public class StudentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String empName;
    private String city;
    private String dept;
    private long mobileNum;

    public StudentInfo() {
    }

    public StudentInfo(String empName, String city, String dept, long mobileNum) {
        this.empName = empName;
        this.city = city;
        this.dept = dept;
        this.mobileNum = mobileNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public long getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(long mobileNum) {
        this.mobileNum = mobileNum;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "id=" + id +
                ", empName='" + empName + '\'' +
                ", city='" + city + '\'' +
                ", dept='" + dept + '\'' +
                ", mobileNum=" + mobileNum +
                '}';
    }
}
