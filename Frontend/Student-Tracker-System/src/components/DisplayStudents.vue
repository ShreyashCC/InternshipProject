<template>
  <div>
    <div>
    <!-- Show table view by default -->
    <div v-if="!showPDFEditor" class="students-table-container">
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
          <td v-if="student.status === 'ACTIVE'">
            <button @click="showTCModel(student.regNo)" class="promote-button">{{ t('Promote.btnText2') }}</button>
          </td>
          <td v-if="student.standard != '12' && student.status == 'ACTIVE'">
            <button @click="showPromoteModel(student.regNo)" class="promote-button">{{t('Promote.btnText')}}</button>
          </td>
          <td v-if="student.standard == '12' && student.status == 'ACTIVE'">
            <button @click="showGraduationModel(student.regNo)" class="promote-button">{{t('Promote.btnText4')}}</button>
          </td>
        </tr>
        </tbody>
      </table>

      <!-- Modals -->
      <div v-if="showGraduationWarning" class="modal-backdrop">
        <div class="modal-overlay">
          <div class="model">
            <h3>{{ t('ShowAllStudents.graduate_student_title') }}</h3>
            <div class="button-group">
              <button @click="confirmGraduation">{{t('ShowAllStudents.confirm')}}</button>
              <button @click="cancelFxn">{{t('ShowAllStudents.cancel')}}</button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="showPromoteWarning" class="modal-backdrop">
        <div class="modal-overlay">
          <div class="model">
            <h3>{{t('ShowAllStudents.promote_student_title')}}</h3>
            <div class="button-group">
              <button @click="confirmPromote">{{t('ShowAllStudents.confirm')}}</button>
              <button @click="cancelFxn">{{t('ShowAllStudents.cancel')}}</button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="showGenerateTCWarning" class="modal-backdrop">
        <div class="modal-overlay">
          <div class="model">
            <h3>{{t('ShowAllStudents.graduate_student_title')}}</h3>
            <div class="button-group">
              <button @click="updateStatusToRESCINDED">{{t('ShowAllStudents.confirm')}}</button>
              <button @click="cancelFxn">{{t('ShowAllStudents.cancel')}}</button>
            </div>
          </div>
        </div>
      </div>

      <!-- Notifications -->
      <div v-if="showSuccess" class="top-notification">
        {{ successMessage }}
      </div>

      <div v-if="showFailed" class="top-notification-failed">
        {{ failedMessage }}
      </div>
    </div>

    <!-- Show PDF Editor when TC is generated -->

    <template v-else>
      <PDFEditor :regNo="currentStudentRegNo" @goBack="showPDFEditor = false" />
    </template>
  </div>
  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue'
import axios from 'axios'
import { useI18n } from "vue-i18n";
import PDFEditor from './PDFEditor.vue'

const { t } = useI18n();

const students = ref([])
const loading = ref(true)
const error = ref(null)
const showPDFEditor = ref(false)

const showGraduationWarning = ref(false)
const showPromoteWarning = ref(false)
const showGenerateTCWarning = ref(false)
const showSuccess = ref(false)
const currentStudentRegNo = ref(null)
const successMessage = ref("")
const showFailed = ref(false)
const failedMessage = ref("")

const cancelFxn = () => {
  showGraduationWarning.value = false;
  showPromoteWarning.value = false;
  showGenerateTCWarning.value = false;
}

const confirmGraduation = async () => {
  try {
    await axios.get(`http://localhost:8080/student/status/${currentStudentRegNo.value}`);
    showPDFEditor.value = true;
    successMessage.value = t('ShowAllStudents.success_tc_generated', { regNo: currentStudentRegNo.value });
    showSuccess.value = true;
    await fetchStudents();
    setTimeout(() => (showSuccess.value = false), 3000);
  } catch (err) {
    showFailed.value = true;
    failedMessage.value = t('ShowAllStudents.failed_to_graduate')
    setTimeout(() => (showFailed.value = false), 3000);
  } finally {
    showGraduationWarning.value = false;
  }
};

const confirmPromote = async () => {
  try {
    await axios.get(`http://localhost:8080/student/promoted/${currentStudentRegNo.value}`);
    successMessage.value =  t('Promote.success', { regNo: currentStudentRegNo.value });
    showSuccess.value = true;
    await fetchStudents();
    setTimeout(() => (showSuccess.value = false), 3000);
  } catch (error) {
    showFailed.value = true;
    failedMessage.value =  t('Promote.failed');
    setTimeout(() => (showFailed.value = false), 3000);
  } finally {
    showPromoteWarning.value = false;
  }
};

const updateStatusToRESCINDED = async () => {
  try {
    await axios.get(`http://localhost:8080/student/status/tc/${currentStudentRegNo.value}`);
    showPDFEditor.value = true;
    showSuccess.value = true;
    await fetchStudents();
    setTimeout(() => (showSuccess.value = false), 3000);

  } catch (error) {
    console.error(error);
    showFailed.value = true;
    setTimeout(() => (showFailed.value = false), 3000);
  } finally {
    showGenerateTCWarning.value = false;
  }
}

const fetchStudents = async () => {
  try {
    loading.value = true;
    const response = await axios.get('http://localhost:8080/student')
    students.value = response.data
    error.value = null;
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to fetch students.'
  } finally {
    loading.value = false
  }
}

const showTCModel = (regNo) => {
  currentStudentRegNo.value = regNo;
  showGenerateTCWarning.value = true;
}

const showGraduationModel = (regNo) => {
  currentStudentRegNo.value = regNo;
  showGraduationWarning.value = true;
}

const showPromoteModel = (regNo) => {
  currentStudentRegNo.value = regNo;
  showPromoteWarning.value = true;
}

onMounted(() => {
  fetchStudents()
})

</script>

<style scoped>
/* Your existing styles remain exactly the same */
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
  background-color: rgba(173, 216, 230, 0.5);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(1px);
}

.modal-overlay {
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
  top: 80px;
  right: -2%;
  transform: translateX(-15%);
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