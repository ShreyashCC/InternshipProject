package com.accenture.Student_Tracker_System.Services;

import com.accenture.Student_Tracker_System.Repositories.StudentRepository;
import com.accenture.Student_Tracker_System.Repositories.TCRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TCService {

    @Autowired
    TCRepository tcRepository;

    @Autowired
    StudentRepository studentRepository;

}
