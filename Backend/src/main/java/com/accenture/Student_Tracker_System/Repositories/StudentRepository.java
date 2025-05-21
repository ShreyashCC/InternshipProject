package com.accenture.Student_Tracker_System.Repositories;

import com.accenture.Student_Tracker_System.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("SELECT s.rollNo FROM Student s WHERE s.standard = :standard ORDER BY s.rollNo ASC")
    List<Integer> findAllRollNosByStandard(@Param("standard") int standard);
    Optional<Student> findById(Integer integer);
}
