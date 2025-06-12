package com.accenture.Student_Tracker_System.Entities;

import com.accenture.Student_Tracker_System.Enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer regNo;

     private Integer rollNo;

     private String firstName;
     private String lastName;

     private Integer standard;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date admissionDate;

     private String address;
     private String mobileNo;
    private String emailId;
     private Status status;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private TransferCertificate transferCertificate;

    public void setEmailId(String emailId) {
        if (emailId == null || !emailId.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email address format");
        }
        this.emailId = emailId;
    }

    public void setTC(TransferCertificate transferCertificate){
        this.transferCertificate = transferCertificate;
    }

    @Override
    public String toString(){
        return "Student Details: " +
                "\nFirstname: "+firstName+" Lastname: "+lastName+
                "\nRoll.No: "+rollNo+" Class: "+standard+
                "\nMobile.No: "+mobileNo+" EmailId: "+emailId+
                "\nAdmissionDate: "+admissionDate+" Status: "+status+
                "\nAddress: "+address+"\n";
    }
}
