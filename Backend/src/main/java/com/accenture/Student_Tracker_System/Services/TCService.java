package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.Entities.Student;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@Transactional
public class TCService {

    @Autowired
    TCRepository tcRepository;

    @Autowired
    StudentRepository studentRepository;

    public TransferCertificate saveTC (TransferCertificate transferCertificate){
        return tcRepository.save(transferCertificate);
    }

    public TransferCertificate getTC (int id){
        return tcRepository.findById(id).get();
    }

    public TransferCertificate issueTC (Student student){
        TransferCertificate transferCertificate = new TransferCertificate();
        
        transferCertificate.setStudent(student);
        LocalDate today = LocalDate.now();
        transferCertificate.setIssuedDate(today);
        return tcRepository.save(transferCertificate);
    }
}
