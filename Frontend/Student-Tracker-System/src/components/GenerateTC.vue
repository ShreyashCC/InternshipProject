<template>
  <div class="generate-TC-container">
    <h2>{{t('Promote.GenerateTransferCertificate')}}</h2>

    <div class="form-group">
      <label for="regNumber">{{t('Promote.InputFieldText')}}</label>
      <input
          v-model="regNo"
          type="number"
          id="regNo"
          placeholder="e.g. 1"
      />
      <button @click="showGenerateTCModel(regNo)">{{t('Promote.btntext3')}}</button>
    </div>

    <div v-if="loading" class="loading">Loading...</div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="TC" class="TC-message">
      <h3>TC Generated</h3>

    </div>

    <div v-if="showGenerateTCWarning" class="modal-backdrop">
      <div class="modal-overlay">
        <div class="model">
          <h3>Generate Student TC?</h3>
          <div class="button-group">
            <button @click="updateStatusToRESCINDED">Ok</button>
            <button @click="cancelFxn">Cancel</button>
          </div>
        </div>
      </div>
    </div>


    <div v-if="showSuccess" class="top-notification-success">
      TC Generated
    </div>

    <div v-if="showFailed" class="top-notification-failed">
      Failed to generate TC!
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { PDFDocument, StandardFonts, rgb } from 'pdf-lib'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()

const currentStudentRegNo = ref("")
const regNo = ref(null)
const TC = ref(null)
const loading = ref(false)
const error = ref(null)
const showGenerateTCWarning = ref(null)
const showSuccess = ref(null)
const showFailed = ref(null)

const cancelFxn = async() => {
  showGenerateTCWarning.value=false;
  console.log("cancelled")
}

const showGenerateTCModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showGenerateTCWarning.value = true;
}



const updateStatusToRESCINDED = async () => {
  try {
    console.log("error here")
    await axios.get(`http://localhost:8080/student/status/tc/${currentStudentRegNo.value}`)

    await generateEditablePDF(currentStudentRegNo.value)
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);

  } catch (error) {
    console.log(error)
    showFailed.value = true;
    setTimeout(() => (showFailed.value = false), 3000);
  } finally {
    showGenerateTCWarning.value = false;
  }
}

const fetchStudentsById = async (regId) => {
  try {
    const response = await axios.get(`http://localhost:8080/student/${regId}`)
    // if (response.data.standard === 12 && response.data.Status === 'ACTIVE') {
    return response.data
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to fetch students.'
    throw new Error("No such Student");
  } finally {
    loading.value = false
  }
}


async function generateEditablePDF(regNo) {
  const students = await  fetchStudentsById(regNo);
  const pdfDoc = await PDFDocument.create();
  const page = pdfDoc.addPage([595, 842]); // A4 size
  const { height } = page.getSize();
  const font = await pdfDoc.embedFont(StandardFonts.Courier);
  const fontSize = 10;
  let y = height - 80;

  const lineSpacing = 40;

  const drawText = (text, offset = 50, space = lineSpacing) => {
    page.drawText(text, { x: offset, y, size: fontSize, font });
    y -= space;
  };

  // Header
  page.drawText('Kendriya Vidyalaya', {
    x: 210,
    y,
    size: 16,
    font,
    color: rgb(0, 0, 0),
  });
  y -= 45;

  page.drawText('Transfer Certificate (TC)', {
    x: 200,
    y,
    size: 14,
    font,
    color: rgb(1, 0, 0),
  });
  y -= 40;

  drawText('-----------------------------------------------------------------------------------', 50, 20);

  // Student Details
  drawText(`1. Student's Full Name        : ${students.firstName} ${students.lastName}`);
  drawText(`2. Registration Number        : ${students.regNo}`);
  drawText(`3. Roll Number                : ${students.rollNo}`);
  drawText(`4. Standard/Class             : ${students.standard}`);
  drawText('5. Date of Birth (DOB)        :', 50);
  const dobY = y + lineSpacing;
  drawText(`6. Admission Date             : ${students.admissionDate}`);
  drawText(`7. Address                    : ${students.address}`);
  drawText(`8. Mobile Number              : ${students.mobileNo}`);
  drawText(`9. Email ID                   : ${students.emailId}`);

  drawText('11. Guardian Name              :', 44);
  const guardianY = y + lineSpacing;

  drawText('12. Reason of Leaving          :', 44);
  const reasonY = y + lineSpacing;

  drawText('13. Remarks                    :', 44);
  const remarksY = y + lineSpacing;

  // Create form
  const form = pdfDoc.getForm();
  const dobField = form.createTextField('dobField');
  dobField.setText(students.dob);
  dobField.addToPage(page, { x: 245, y: dobY - 14, width: 150, height: 25 });

  const guardianNameField = form.createTextField('guardianName');
  guardianNameField.addToPage(page, { x: 242, y: guardianY - 14, width: 300, height: 25 });

  const reasonField = form.createDropdown('reasonOfLeaving');
  reasonField.addOptions(['PASSED_AND_LEFT', 'RUSTICATED', 'ADMISSION_REVOKED']);
  reasonField.select('PASSED_AND_LEFT');
  reasonField.addToPage(page, { x: 242, y: reasonY - 14, width: 300, height: 25 });

  const remarksField = form.createDropdown('remarks');
  remarksField.addOptions(['BAD', 'BELOW_AVERAGE', 'AVERAGE', 'GOOD', 'EXCELLENT', 'BRILLIANT']);
  remarksField.select('AVERAGE');
  remarksField.addToPage(page, { x: 242, y: remarksY - 14, width: 300, height: 25 });

  // Save and download
  const pdfBytes = await pdfDoc.save();
  const blob = new Blob([pdfBytes], { type: 'application/pdf' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'TransferCertificate.pdf';
  a.click();
  URL.revokeObjectURL(url);
}
</script>


<style scoped>
.generate-TC-container {
  max-width: 600px;
  margin: auto;
  padding: 1rem;
  background: #f9f9f9;
  border-radius: 8px;
}
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}
input {
  padding: 0.5rem;
  margin-top: 0.5rem;
}
button {
  margin-top: 0.5rem;
  padding: 0.5rem;
  background-color: #007bff;
  border: none;
  color: white;
  cursor: pointer;
}
.loading {
  color: #666;
}
.error {
  color: red;
}
.TC-message {
  background: #e0ffe0;
  padding: 1rem;
  border-radius: 6px;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(173, 216, 230, 0.5); /* light blue transparent */
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(1px); /* Optional: adds blur effect */
}

.modal-overlay  {
  background-color: #ffeeba;
  color: #856404;
  border: 1px solid #ffc107;
  padding: 1.2rem 2rem 1.7rem 2rem;
  min-width: 360px;
  max-width: 90vw;
  border-radius: 10px;
  z-index: 1000;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  animation: slide-down 0.3s ease;
}

.button-group {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  gap: 8px;
}

.button-group button {
  padding: 4px 10px;
  min-width: 60px;
  font-size: 13px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  width: auto !important;  /* ✅ OVERRIDES global width */
  max-width: 100px;        /* ✅ Controls max width */
}

.button-group button:first-child {
  background-color: #28a745;
  color: white;
}

.button-group button:last-child {
  background-color: #dc3545;
  color: white;
}

.top-notification-success{
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #d4edda;
  color: #155724;
  padding: 12px 24px;
  border-radius: 8px;
  border: 1px solid #c3e6cb;
  z-index: 1001;
  font-weight: bold;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  animation: fade-slide-down 0.3s ease;
}

.top-notification-failed {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #f8d7da;
  color: #721c24;
  padding: 12px 24px;
  border-radius: 8px;
  border: 1px solid #c3e6cb;
  z-index: 1001;
  font-weight: bold;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  animation: fade-slide-down 0.3s ease;
}
</style>