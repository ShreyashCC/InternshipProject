package com.accenture.Student_Tracker_System.DTOs;
import com.accenture.Student_Tracker_System.Enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class StudentDTO {

    private Integer regNo;
    private Integer rollNo;
    private String firstName;
    private String lastName;
    private Integer standard;
    private Date admissionDate;
    private String address;
    private String mobileNo;
    private String emailId;
    private Status status;
//    private Date issuedDate;
//    private Date DOB;
//    private String GuardianName;

}
