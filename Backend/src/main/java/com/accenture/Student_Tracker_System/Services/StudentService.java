package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TCRepository tcRepository;

    public int getNextAvailableRollNo(List<Integer> existingRollNos) {
        for (int i = 0; i < existingRollNos.size(); i++) {
            int expected = i + 1;
            if (!existingRollNos.get(i).equals(expected)) {
                return expected;
            }
        }
        // No gap found, assign next sequential roll
        return existingRollNos.size() + 1;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).get();
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void displayStudentDetails(Student student){
        student.toString();
    }

    public Student promoteStudent(Student student) {
        Student existingStudent = studentRepository.findById(student.getRegNo())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Integer> existingRollNos = studentRepository.findAllRollNosByStandard(existingStudent.getStandard()+1);
        int newRollNo = getNextAvailableRollNo(existingRollNos);
        existingStudent.setRollNo(newRollNo);
        updateStudentStatus(existingStudent); // already increments standard

        if (existingStudent.getStatus() == Status.GRADUATED) {
            existingStudent.setRollNo(null);
            existingStudent.setStandard(null);
            return studentRepository.save(existingStudent);
        }

        return studentRepository.save(existingStudent);
    }


    public List<Integer> getRollList(Integer standard) {
        List<Integer> existingRollNos = studentRepository.findAllRollNosByStandard(standard);
        return existingRollNos;
    }
    public void updateStudentStatus(Student student) {
        student.setStandard(student.getStandard()+1);
        if (student.getStandard() <= 12) {
            student.setStatus(Status.ACTIVE);
        } else if (student.getStandard() > 12){
            student.setStatus(Status.GRADUATED);
        }
    }
}


// add students
// show student details
// promote student to new class
// Update student status