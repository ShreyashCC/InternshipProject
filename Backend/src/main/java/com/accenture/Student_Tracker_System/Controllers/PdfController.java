package com.accenture.Student_Tracker_System.Controllers;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.cos.COSName;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "http://localhost:5173")
public class PdfController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/generate/{regNo}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String regNo) throws IOException {
        try (PDDocument document = new PDDocument()) {
            String studentApiUrl = "http://localhost:8080/student/" + regNo;
            Map<String, Object> studentData = restTemplate.getForObject(studentApiUrl, Map.class);

            if (studentData == null) {
                return ResponseEntity.notFound().build();
            }

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float labelX = 50;
            float fieldX = 250;
            float yStart = 720;
            float lineGap = 40;

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                // Header
                cs.beginText();
                cs.setFont(PDType1Font.COURIER_BOLD, 16);
                cs.newLineAtOffset(180, 780);
                cs.showText("Kendriya Vidyalaya");
                cs.newLineAtOffset(0, -20);
                cs.showText("Transfer Certificate (TC)");
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 12);

                // Labels and fixed values
                String[] labels = {
                        "Student's Full Name:", "Registration Number:", "Standard/Class:",
                        "Admission Date:", "Address:", "Mobile Number:", "Email ID:",
                        "Date of Birth:", "Guardian Name:", "Reason of Leaving:", "Remarks:"
                };

                String[] values = {
                        safeGetString(studentData.get("firstName")) + " " + safeGetString(studentData.get("lastName")),
                        safeGetString(studentData.get("regNo")),
                        safeGetString(studentData.get("standard")),
                        safeGetString(studentData.get("admissionDate")),
                        safeGetString(studentData.get("address")),
                        safeGetString(studentData.get("mobileNo")),
                        safeGetString(studentData.get("emailId")),
                        safeGetString(studentData.get("DOB")),
                        "", "", "" // interactive fields
                };

                for (int i = 0; i < labels.length; i++) {
                    float y = yStart - (i * lineGap);

                    cs.beginText();
                    cs.newLineAtOffset(labelX, y);
                    cs.showText(labels[i]);
                    cs.endText();

                    if (i < 8) {
                        cs.beginText();
                        cs.newLineAtOffset(fieldX, y);
                        cs.showText(values[i]);
                        cs.endText();
                    }
                }

                // Field boxes
                cs.setLineWidth(0.5f);
                cs.addRect(fieldX, yStart -  7 * lineGap - 5, 300, 20); // DOB
                cs.addRect(fieldX, yStart - 8 * lineGap - 5, 300, 20); // Guardian Name
                cs.addRect(fieldX, yStart - 9 * lineGap - 5, 300, 20); // Reason of Leaving
                cs.addRect(fieldX, yStart - 10 * lineGap - 5, 300, 20); // Remarks
                cs.stroke();
            }

            // AcroForm setup
            PDAcroForm acroForm = new PDAcroForm(document);
            document.getDocumentCatalog().setAcroForm(acroForm);
            acroForm.setNeedAppearances(true);

            PDResources resources = new PDResources();
            resources.put(COSName.getPDFName("Helv"), PDType1Font.HELVETICA);
            acroForm.setDefaultResources(resources);
            acroForm.setDefaultAppearance("/Helv 12 Tf 0 g");

            // Add form fields
            addTextField(document, acroForm, page, "Date of Birth:",
                    safeGetString(studentData.get("DOB")), fieldX, yStart - 7 * lineGap);

            addTextField(document, acroForm, page, "guardianName",
                    safeGetString(studentData.get("guardianName")), fieldX, yStart - 8 * lineGap);

            addDropdownField(document, acroForm, page, "reasonOfLeaving",
                    getReasonOfLeavingOptions(),
                    safeGetString(studentData.get("reasonOfLeaving")), fieldX, yStart - 9 * lineGap);

            addDropdownField(document, acroForm, page, "remarks",
                    getRemarksOptions(),
                    safeGetString(studentData.get("remarks")), fieldX, yStart - 10 * lineGap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=tc_" + regNo + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());
        }
    }

    private void addTextField(PDDocument doc, PDAcroForm form, PDPage page,
                              String name, String value, float x, float y) throws IOException {
        PDTextField textField = new PDTextField(form);
        textField.setPartialName(name);
        textField.setDefaultAppearance("/Helv 12 Tf 0 g");
        textField.setReadOnly(false);
        textField.setMultiline(false);
        textField.setValue(value == null ? "" : value);

        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(new PDRectangle(x, y, 300, 20));
        widget.setPage(page);
        widget.setParent(textField);
        widget.setPrinted(true);

        textField.getWidgets().add(widget);
        form.getFields().add(textField);
        page.getAnnotations().add(widget);
    }

    private void addDropdownField(PDDocument doc, PDAcroForm form, PDPage page,
                                  String name, String[] options, String selected,
                                  float x, float y) throws IOException {
        PDComboBox comboBox = new PDComboBox(form);
        comboBox.setPartialName(name);
        comboBox.setOptions(Arrays.asList(options));
        comboBox.setDefaultAppearance("/Helv 12 Tf 0 g");
        comboBox.setEdit(true);
        comboBox.setValue((selected != null && !selected.isEmpty()) ? selected : options[0]);

        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(new PDRectangle(x, y, 300, 20));
        widget.setPage(page);
        widget.setParent(comboBox);
        widget.setPrinted(true);

        comboBox.getWidgets().add(widget);
        form.getFields().add(comboBox);
        page.getAnnotations().add(widget);
    }

    private String[] getReasonOfLeavingOptions() {
        return new String[]{"Select Option", "PASSED_AND_LEFT", "RUSTICATED"};
    }

    private String[] getRemarksOptions() {
        return new String[]{"Select Option", "BAD", "BELOW_AVERAGE", "AVERAGE", "GOOD", "EXCELLENT", "BRILLIANT"};
    }

    private String safeGetString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
