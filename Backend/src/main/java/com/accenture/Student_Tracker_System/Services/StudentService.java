package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TCRepository tcRepository;

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void displayStudentDetails(Student student){
        student.toString();
    }

    public void promoteStudent(Student student){
        student.setStandard(student.getStandard()+1);
        studentRepository.save(student);
    }

    public void updateStudentStatus(Student student) {
        if (student.getStandard() >= 12) {
            student.setStatus(Status.GRADUATED);
        } else {
            student.setStatus(Status.ACTIVE);
        }
        studentRepository.save(student);
    }

    public void suspendStudent(Student student){
        student.setStatus(Status.SUSPENDED);
    }
}


// add students
// show student details
// promote student to new class
// Update student status