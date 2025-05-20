package com.accenture.Student_Tracker_System.Repositories;

import com.accenture.Student_Tracker_System.Entities.TransferCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TCRepository extends JpaRepository<TransferCertificate, Integer> {

}
