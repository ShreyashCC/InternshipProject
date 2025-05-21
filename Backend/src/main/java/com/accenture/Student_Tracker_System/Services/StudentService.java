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
import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TCRepository tcRepository;

    public int getNextAvailableRollNo(int standard, List<Integer> existingRollNos) {
        int base = standard * 1000;

        for (int i = 0; i < existingRollNos.size(); i++) {
            int expected = base + i;
            if (!existingRollNos.get(i).equals(expected)) {
                return expected;
            }
        }

        // No gap found, assign next sequential roll
        return base + existingRollNos.size();
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

    public Student promoteStudent(Student student){
        Student existingStudent = studentRepository.findById(student.getRollNo()).orElseThrow(()-> new RuntimeException("Student not found"));
        updateStudentStatus(existingStudent);
        studentRepository.deleteById(existingStudent.getRollNo());
        if (existingStudent.getStatus() == Status.GRADUATED) return null;
        List<Integer> existingRollNos = studentRepository.findAllRollNosByStandard(student.getStandard());
        int newRollNo = getNextAvailableRollNo(existingStudent.getStandard(), existingRollNos);
        Student promotedStudent = new Student();
        promotedStudent.setRollNo(newRollNo);
        promotedStudent.setStandard(existingStudent.getStandard());
        promotedStudent.setLastName(existingStudent.getLastName());
        promotedStudent.setFirstName(existingStudent.getFirstName());
        promotedStudent.setAdmissionDate(existingStudent.getAdmissionDate());
        promotedStudent.setAddress(existingStudent.getAddress());
        promotedStudent.setMobileNo(existingStudent.getMobileNo());
        promotedStudent.setEmailId(existingStudent.getEmailId());
        promotedStudent.setStatus(Status.ACTIVE);
        return studentRepository.save(promotedStudent);
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
        studentRepository.save(student);
    }
}


// add students
// show student details
// promote student to new class
// Update student status