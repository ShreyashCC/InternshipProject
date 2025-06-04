<template>
  <div class="students-table-container">
    <div v-if="loading" class="loading">Loading student records...</div>
    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="students.length" class="students-table">
      <thead>
      <tr>
        <th>{{t('ShowAllStudents.RegistrationNo')}}</th>
        <th>{{t('ShowAllStudents.RollNo')}}</th>
        <th>{{t('ShowAllStudents.FirstName')}}</th>
        <th>{{t('ShowAllStudents.LastName')}}</th>
        <th>{{t('ShowAllStudents.Standard')}}</th>
        <th>{{t('ShowAllStudents.AdmissionDate')}}</th>
        <th>{{t('ShowAllStudents.Address')}}</th>
        <th>{{t('ShowAllStudents.Mobile')}}</th>
        <th>{{t('ShowAllStudents.Email')}}</th>
        <th>{{t('ShowAllStudents.Status')}}</th>
        <th>{{t('ShowAllStudents.TCGeneration')}}</th>
        <th>{{t('ShowAllStudents.PromoteStudent')}}</th>

      </tr>
      </thead>
      <tbody>
      <tr v-for="student in students" :key="student.regNo">
        <td>{{student.regNo}}</td>
        <td>{{student.rollNo}}</td>
        <td>{{student.firstName}}</td>
        <td>{{student.lastName}}</td>
        <td>{{student.standard}}</td>
        <td>{{student.admissionDate}}</td>
        <td>{{student.address}}</td>
        <td>{{student.mobileNo}}</td>
        <td>{{student.emailId}}</td>
        <td>{{student.status}}</td>
        <td v-if="student.status === 'ACTIVE'" > <button @click="showTCModel(student.regNo)" class="promote-button">{{ t('Promote.btnText2') }}</button></td>
        <td v-if = "student.standard != '12' && student.status == 'ACTIVE'"><button @click="showPromoteModel(student.regNo)" class="promote-button">{{t('Promote.btnText')}}</button></td>
        <td v-if="student.standard == '12' && student.status == 'ACTIVE'"><button @click="showGraduationModel(student.regNo)" class="promote-button">{{t('Promote.btnText4')}}</button></td>
      </tr>
      </tbody>
    </table>

    <div v-if="showGraduationWarning" class="modal-backdrop">
      <div class="modal-overlay">
        <div class="model">
          <h3>Graduate Student?</h3>
          <div class="button-group">
            <button @click="confirmGraduation">Ok</button>
            <button @click="cancelFxn">Cancel</button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showPromoteWarning" class="modal-backdrop">
      <div class="modal-overlay">
        <div class="model">
          <h3>Promote Student?</h3>
          <div class="button-group">
            <button @click="confirmPromote">Ok</button>
            <button @click="cancelFxn">Cancel</button>
          </div>
        </div>
      </div>
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


    <div v-if="showSuccess" class="top-notification">
      {{ successMessage }}
    </div>
  </div>


</template>

<script setup>
import {ref, onMounted, onUpdated, watch} from 'vue'
import axios, {HttpStatusCode} from 'axios'
import {useI18n} from "vue-i18n";
const {t, availableLocales, locale}  = useI18n();
import { PDFDocument, StandardFonts, rgb } from 'pdf-lib'
const students = ref([])
const loading = ref(true)
const error = ref(null)
const TC = ref(null)

const showGraduationWarning = ref(false)
const showPromoteWarning = ref(false)
const showGenerateTCWarning = ref(false)
const showSuccess = ref(false)
const currentStudentRegNo = ref(null)
const successMessage = ref("")

const cancelFxn = async() => {
  showGraduationWarning.value=false;
  showPromoteWarning.value=false;
  showGenerateTCWarning.value=false;
  console.log("cancelled")
}

const confirmGraduation = async () => {
  try {
    await axios.get(`http://localhost:8080/student/status/${currentStudentRegNo.value}`);
    await generateEditablePDF(currentStudentRegNo.value);
    successMessage.value = `Transfer certificate generated for ID ${currentStudentRegNo.value}`;
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);
  } catch (err) {
    alert('Error graduating student');
  } finally {
    showGraduationWarning.value = false;
  }
};



const confirmPromote = async () => {
  try {
    await axios.get(`http://localhost:8080/student/promoted/${currentStudentRegNo.value}`);
    successMessage.value = `Student ID ${currentStudentRegNo.value} promoted successfully!`;
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);
  } catch (error) {
    alert('Failed to promote student. The Student is Already Graduated');
  } finally {
    showPromoteWarning.value = false;
  }
};

const updateStatusToRESCINDED = async () => {
  try {
    console.log("error here")
    await axios.get(`http://localhost:8080/student/status/tc/${currentStudentRegNo.value}`)

    await generateEditablePDF(currentStudentRegNo.value)
    successMessage.value = `Transfer certificate generated for ID ${currentStudentRegNo.value}`;
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);

  } catch (error) {
    alert('Failed to generate TC. The Student is Already Graduated');
  } finally {
    showGenerateTCWarning.value = false;
  }
}

const fetchStudents = async () => {
  try {
    const response = await axios.get('http://localhost:8080/student')
    students.value = response.data
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to fetch students.'
  } finally {
    loading.value = false
  }
}

const showTCModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showGenerateTCWarning.value = true;
}


const promoteStudent = async (regNo) => {
  try {
    const res = await axios.get(`http://localhost:8080/student/promoted/${regNo}`)
    alert(`Student ID ${regNo} promoted successfully!`)

  } catch (error) {
    console.error('Error:', error)
    alert('Failed to promote student. The Student is Already Graduated')
  }
}

const showGraduationModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showGraduationWarning.value = true;
}

const showPromoteModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showPromoteWarning.value = true;
}

/*const UpdateStudentStatus = async (regNo) => {
  const res = await axios.get(`http://localhost:8080/student/status/${regNo}`)
  await generateEditablePDF(regNo);
}*/
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


onMounted(() => {
  fetchStudents()
})

onUpdated(()=> {
  fetchStudents()
})
</script>

<style scoped>
.students-table-container {
  margin-top: 0;
  padding: 1.5rem;
  background: #fff;
  border-radius: 12px;
  max-width: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow-x: auto;
}

.students-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  font-family: 'Segoe UI', sans-serif;
  table-layout: auto;
  min-width: 900px;
}

.students-table th,
.students-table td {
  padding: 0.75rem 1rem;
  text-align: left;
  font-size: 14px;
  word-break: break-word;
  vertical-align: top;
}

.students-table th {
  background-color: #007bff;
  color: white;
  font-weight: 600;
  text-transform: uppercase;
}

.students-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.promote-button {
  background-color: #007bff;
  border: none;
  color: white;
  padding: 0.4rem 0.8rem;
  font-size: 13px;
  font-weight: 500;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.promote-button:hover {
  background-color: #0056b3;
  transform: translateY(-1px);
}

.promote-button:focus {
  outline: 2px solid #0056b3;
  outline-offset: 2px;
}

.loading,
.error {
  text-align: center;
  font-size: 16px;
  margin-top: 1rem;
}

.error {
  color: red;
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
}

.button-group button {
  margin: 0 5px;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.button-group button:first-child {
  background-color: #28a745;
  color: white;
}

.button-group button:last-child {
  background-color: #dc3545;
  color: white;
}

.top-notification {
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

@keyframes fade-slide-down {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

/* Responsive tweaks for smaller screens */
@media (max-width: 768px) {
  .students-table-container {
    padding: 1rem;
  }

  .students-table th,
  .students-table td {
    padding: 0.5rem;
    font-size: 13px;
  }

  .promote-button {
    font-size: 12px;
    padding: 0.35rem 0.6rem;
  }
}

@media (max-width: 480px) {
  .students-table {
    font-size: 12px;
  }

  .students-table th,
  .students-table td {
    padding: 0.4rem;
  }

  .promote-button {
    font-size: 11px;
    padding: 0.3rem 0.5rem;
  }
}
</style>
