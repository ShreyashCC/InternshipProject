const express = require('express');
const cors = require('cors');
const { PDFDocument, StandardFonts } = require('pdf-lib');

const app = express();
const PORT = 8080;

app.use(cors());

app.get('/generate-editable-pdf/:regNo', async (req, res) => {
    const regNo = req.params.regNo;

    // Sample student data (mocked)
    const student = {
        regNo,
        rollNo: 23,
        firstName: "Rahul",
        lastName: "Sharma",
        standard: 12,
        admissionDate: "2021-04-15",
        address: "123 Sector A, Delhi",
        mobileNo: "9876543210",
        emailId: "rahul.sharma@example.com",
        dob: "2005-08-21"
    };

    try {
        const pdfDoc = await PDFDocument.create();
        const page = pdfDoc.addPage([595, 842]); // A4
        const { height } = page.getSize();
        const font = await pdfDoc.embedFont(StandardFonts.Courier);
        const fontSize = 10;
        let y = height - 80;
        const spacing = 40;

        const drawText = (text, offset = 50, space = spacing) => {
            page.drawText(text, { x: offset, y, size: fontSize, font });
            y -= space;
        };

        // Headers
        page.drawText('Kendriya Vidyalaya', { x: 210, y, size: 16, font });
        y -= 45;
        page.drawText('Transfer Certificate (TC)', { x: 200, y, size: 14, font });
        y -= 40;

        drawText('---------------------------------------------------------------------', 50, 20);

        // Student info
        drawText(`1. Student's Full Name        : ${student.firstName} ${student.lastName}`);
        drawText(`2. Registration Number        : ${student.regNo}`);
        drawText(`3. Roll Number                : ${student.rollNo}`);
        drawText(`4. Standard/Class             : ${student.standard}`);
        drawText(`5. Date of Birth (DOB)        :`, 50);
        const dobY = y + spacing;
        drawText(`6. Admission Date             : ${student.admissionDate}`);
        drawText(`7. Address                    : ${student.address}`);
        drawText(`8. Mobile Number              : ${student.mobileNo}`);
        drawText(`9. Email ID                   : ${student.emailId}`);

        drawText('11. Guardian Name              :', 44);
        const guardianY = y + spacing;

        drawText('12. Reason of Leaving          :', 44);
        const reasonY = y + spacing;

        drawText('13. Remarks                    :', 44);
        const remarksY = y + spacing;

        // Create Form Fields
        const form = pdfDoc.getForm();

        const dobField = form.createTextField('dobField');
        dobField.setText(student.dob);
        dobField.addToPage(page, { x: 245, y: dobY - 14, width: 150, height: 25 });

        const guardianField = form.createTextField('guardianName');
        guardianField.addToPage(page, { x: 242, y: guardianY - 14, width: 300, height: 25 });

        const reasonField = form.createDropdown('reasonOfLeaving');
        reasonField.addOptions(['PASSED_AND_LEFT', 'RUSTICATED', 'ADMISSION_REVOKED']);
        reasonField.select('PASSED_AND_LEFT');
        reasonField.addToPage(page, { x: 242, y: reasonY - 14, width: 300, height: 25 });

        const remarksField = form.createDropdown('remarks');
        remarksField.addOptions(['BAD', 'BELOW_AVERAGE', 'AVERAGE', 'GOOD', 'EXCELLENT', 'BRILLIANT']);
        remarksField.select('AVERAGE');
        remarksField.addToPage(page, { x: 242, y: remarksY - 14, width: 300, height: 25 });

        // Finalize and send as response
        const pdfBytes = await pdfDoc.save();

        res.setHeader('Content-Type', 'application/pdf');
        res.setHeader('Content-Disposition', 'inline; filename="TransferCertificate.pdf"');
        res.send(Buffer.from(pdfBytes));
    } catch (error) {
        console.error(error);
        res.status(500).send('Failed to generate editable PDF');
    }
});

app.listen(PORT, () => {
    console.log(`✅ Server running at http://localhost:${PORT}`);
});
