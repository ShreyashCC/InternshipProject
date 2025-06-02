import { PDFDocument, StandardFonts, rgb } from 'pdf-lib';

/**
 * Generates a Transfer Certificate PDF and returns it as a Blob.
 * @param {Object} tc - TransferCertificate data
 * @returns {Promise<Blob>}
 */
export async function generateTransferCertificatePDF(tc) {
    const pdfDoc = await PDFDocument.create();
    const page = pdfDoc.addPage([595, 842]); // A4 size
    const { width, height } = page.getSize();
    const font = await pdfDoc.embedFont(StandardFonts.HELVETICA);
    const fontSize = 12;
    let y = height - 50;

    const drawText = (text, offset = 50) => {
        page.drawText(text, { x: offset, y, size: fontSize, font });
        y -= 20;
    };

    // Header
    page.drawText('SCHOOL/INSTITUTE NAME', { x: 180, y, size: 16, font, color: rgb(0, 0, 0) });
    y -= 30;
    page.drawText('Transfer Certificate (TC)', { x: 200, y, size: 14, font, color: rgb(0, 0, 0) });
    y -= 25;
    drawText('--------------------------------------------------------------');

    drawText(`TC No.: ${tc.uniqueID}       Date of Issue: ${tc.issuedDate}`);
    drawText(`1. Student's Full Name        : ${tc.student.firstName} ${tc.student.lastName}`);
    drawText(`2. Registration Number        : ${tc.student.regNo}`);
    drawText(`3. Roll Number                : ${tc.student.rollNo}`);
    drawText(`4. Standard/Class             : ${tc.student.standard}`);
    drawText(`5. Date of Birth (DOB)        : ${tc.DOB}`);
    drawText(`6. Admission Date             : ${tc.student.admissionDate}`);
    drawText(`7. Guardian's Name            : ${tc.guardianName}`);
    drawText(`8. Address                    : ${tc.student.address}`);
    drawText(`9. Mobile Number              : ${tc.student.mobileNo}`);
    drawText(`10. Email ID                  : ${tc.student.emailId}`);
    drawText(`11. Reason for Leaving        : ${tc.reasonOfLeaving}`);
    drawText(`12. Remarks                   : ${tc.remark}`);

    drawText('--------------------------------------------------------------');
    drawText('Signature of Principal', 370);
    drawText('(With Seal of Institution)', 340);
    drawText('--------------------------------------------------------------');

    const pdfBytes = await pdfDoc.save();
    return new Blob([pdfBytes], { type: 'application/pdf' });
}
