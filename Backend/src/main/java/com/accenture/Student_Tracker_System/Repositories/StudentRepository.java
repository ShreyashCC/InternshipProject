package com.accenture.Student_Tracker_System.Repositories;

import com.accenture.Student_Tracker_System.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

}
