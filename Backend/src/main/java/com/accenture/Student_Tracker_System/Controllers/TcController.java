package com.accenture.Student_Tracker_System.Controllers;

import com.accenture.Student_Tracker_System.DTOs.TransferCertificateDTO;
import com.accenture.Student_Tracker_System.Entities.TransferCertificate;

public class TcController {


    private TransferCertificate DTOToTC(TransferCertificateDTO transferCertificateDTO){
        TransferCertificate transferCertificate = new TransferCertificate();

        transferCertificate.setUniqueID(transferCertificateDTO.getUniqueID());
        transferCertificate.setStudent(transferCertificateDTO.getStudent());
        transferCertificate.setRemark(transferCertificateDTO.getRemark());
        transferCertificate.setReasonOfLeaving(transferCertificateDTO.getReasonOfLeaving());
        transferCertificate.setIssuedDate(transferCertificateDTO.getIssuedDate());
        return transferCertificate;
    }

    private TransferCertificateDTO TcToDTO(TransferCertificate transferCertificate){
        TransferCertificateDTO transferCertificateDTO = new TransferCertificateDTO();

        transferCertificateDTO.setUniqueID(transferCertificate.getUniqueID());
        transferCertificateDTO.setStudent(transferCertificate.getStudent());
        transferCertificateDTO.setRemark(transferCertificate.getRemark());
        transferCertificateDTO.setReasonOfLeaving(transferCertificate.getReasonOfLeaving());
        transferCertificateDTO.setIssuedDate(transferCertificate.getIssuedDate());
        return transferCertificateDTO;
    }
}
