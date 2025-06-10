package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Enums.Status;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private static final Logger logger = LogManager.getLogger(StudentService.class);

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TCRepository tcRepository;

    public int getNextAvailableRollNo(List<Integer> existingRollNos) {
        logger.debug("Calculating next available roll number from existing list: {}", existingRollNos);
        for (int i = 0; i < existingRollNos.size(); i++) {
            int expected = i + 1;
            if (!existingRollNos.get(i).equals(expected)) {
                logger.info("Found gap at position {}: returning {}", i, expected);
                return expected;
            }
        }
        int next = existingRollNos.size() + 1;
        logger.info("No gaps found, assigning next roll number: {}", next);
        return next;
    }

    public Student saveStudent(Student student) {
        logger.info("Saving new student: {}", student);
        return studentRepository.save(student);
    }

    public Student getStudentById(int id) {
        logger.debug("Fetching student by ID: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new RuntimeException("Student not found");
                });
    }

    public List<Student> getAllStudents() {
        logger.debug("Fetching all students");
        return studentRepository.findAll();
    }

    public void displayStudentDetails(Student student) {
        logger.info("Student details: {}", student);
    }

    public Student promoteStudent(Student student) {
        logger.info("Promoting student: {}", student.getRegNo());

        Student existingStudent = studentRepository.findById(student.getRegNo())
                .orElseThrow(() -> {
                    logger.error("Student not found for promotion: {}", student.getRegNo());
                    return new RuntimeException("Student not found");
                });

        List<Integer> existingRollNos = studentRepository.findAllRollNosByStandard(existingStudent.getStandard() + 1);
        int newRollNo = getNextAvailableRollNo(existingRollNos);
        existingStudent.setRollNo(newRollNo);

        updateStudentStatus(existingStudent);
        logger.info("Promoted student: {} to standard {}, new roll no: {}",
                existingStudent.getRegNo(), existingStudent.getStandard(), newRollNo);

        return studentRepository.save(existingStudent);
    }

    public void changeStudentStatus(Student student) {
        logger.info("Changing student status based on standard for: {}", student.getRegNo());
        if (student.getStandard() == 12) {
            student.setStatus(Status.GRADUATED);
            logger.info("Student graduated: {}", student.getRegNo());
        } else {
            student.setStatus(Status.RESCINDED);
            logger.info("Student rescinded: {}", student.getRegNo());
        }
        studentRepository.save(student);
    }

    public void changeStudentStatusToRESCINDED(Student student) {
        logger.info("Setting status to RESCINDED for student: {}", student.getRegNo());
        student.setStatus(Status.RESCINDED);
        studentRepository.save(student);
    }

    public List<Integer> getRollList(Integer standard) {
        logger.debug("Getting roll list for standard: {}", standard);
        return studentRepository.findAllRollNosByStandard(standard);
    }

    public void updateStudentStatus(Student student) {
        logger.debug("Updating status and standard for student: {}", student.getRegNo());
        student.setStandard(student.getStandard() + 1);
        if (student.getStandard() <= 12) {
            student.setStatus(Status.ACTIVE);
            logger.info("Student status set to ACTIVE: {}", student.getRegNo());
        } else {
            student.setStatus(Status.GRADUATED);
            logger.info("Student status set to GRADUATED: {}", student.getRegNo());
        }
    }
}



// add students
// show student details
// promote student to new class
// Update student status