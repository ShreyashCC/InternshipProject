package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.StudentDTO;
import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;

import java.util.HashMap;
import java.util.List;


public class StudentController {


    private Student DTOToStudent(StudentDTO studentDTO){
        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setRollNo(studentDTO.getRollNo());
        student.setStandard(studentDTO.getStandard());
        student.setAddress(studentDTO.getAddress());
        student.setMobileNo(studentDTO.getMobileNo());
        student.setEmailId(studentDTO.getEmailId());

        return student;
    }

    private StudentDTO StudentToDTO(Student student){
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setRollNo(student.getRollNo());
        studentDTO.setStandard(student.getStandard());
        studentDTO.setAddress(student.getAddress());
        studentDTO.setMobileNo(student.getMobileNo());
        studentDTO.setEmailId(student.getEmailId());

        return studentDTO;
    }


}
