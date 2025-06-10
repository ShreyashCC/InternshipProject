<template>
  <div class="student-details-container">
    <h2>Student Details</h2>

<!--    <div class="form-group">-->
<!--      <label for="regNumber">Enter Registration Number:</label>-->
<!--      <input-->
<!--          v-model="regNo"-->
<!--          type="text"-->
<!--          id="regNo"-->
<!--      />-->
<!--      <button @click="fetchStudentDetails">Get Details</button>-->
<!--    </div>-->

    <div v-if="loading" class="loading">Loading...</div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="student" class="student-info">
      <h3>{{t('ShowAllStudents.StudentInformation')}}</h3>
<!--      <p><strong>{{t('ShowAllStudents.RegistrationNo')}}</strong> {{student.regNo}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.RollNo')}}</strong> {{student.rollNo}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.FirstName')}}</strong> {{student.firstName}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.LastName')}}</strong> {{student.lastName}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.Standard')}}</strong> {{student.standard}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.AdmissionDate')}}</strong> {{student.admissionDate}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.Address')}}</strong> {{student.address}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.Mobile')}}</strong> {{student.mobileNo}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.Email')}}</strong> {{student.emailId}}</p>-->
<!--      <p><strong>{{t('ShowAllStudents.Status')}}</strong> {{student.status}}</p>-->
      <table class="student-table">
        <tr>
          <td><strong>{{ t('ShowAllStudents.RegistrationNo') }}</strong></td>
          <td>{{ student.regNo }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.RollNo') }}</strong></td>
          <td>{{ student.rollNo }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.FirstName') }}</strong></td>
          <td>{{ student.firstName }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.LastName') }}</strong></td>
          <td>{{ student.lastName }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Standard') }}</strong></td>
          <td>{{ student.standard }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.AdmissionDate') }}</strong></td>
          <td>{{ student.admissionDate }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Address') }}</strong></td>
          <td>{{ student.address }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Mobile') }}</strong></td>
          <td>{{ student.mobileNo }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Email') }}</strong></td>
          <td>{{ student.emailId }}</td>
        </tr>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Status') }}</strong></td>
          <td>{{ student.status }}</td>
        </tr>
      </table>
      <div v-if="student.status === 'ACTIVE'" > <button @click="showTCModel(student.regNo)" class="promote-button">{{ t('Promote.btnText2') }}</button></div>
      <div v-if = "student.standard != '12' && student.status == 'ACTIVE'"><button @click="showPromoteModel(student.regNo)" class="promote-button">{{t('Promote.btnText')}}</button></div>
      <div v-if="student.standard == '12' && student.status == 'ACTIVE'"><button @click="showGraduationModel(student.regNo)" class="promote-button">{{t('Promote.btnText4')}}</button></div>
    </div>
    <div v-if="showGraduationWarning" class="modal-backdrop">
      <div class="modal-overlay">
        <div class="model">
          <h3>Graduate Student?</h3>
          <div class="button-group">
            <button @click="confirmGraduation">Confirm</button>
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
            <button @click="confirmPromote">Confirm</button>
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
            <button @click="updateStatusToRESCINDED">Confirm</button>
            <button @click="cancelFxn">Cancel</button>
          </div>
        </div>
      </div>
    </div>


    <div v-if="showSuccess" class="top-notification">
      {{ successMessage }}
    </div>

    <div v-if="showFailed" class="top-notification-failed">
      {{ failedMessage }}
    </div>
  </div>
</template>

<script setup>
import {ref, watch, onMounted, onBeforeUpdate} from 'vue'
import axios from 'axios'
import {useI18n} from "vue-i18n";
const {t, availableLocales, locale}  = useI18n();

const props = defineProps({
  regNo: String
})
const student = ref(null)
const loading = ref(false)
const error = ref(null)
const showGraduationWarning = ref(false)
const showPromoteWarning = ref(false)
const showGenerateTCWarning = ref(false)
const showSuccess = ref(false)
const currentStudentRegNo = ref(null)
const successMessage = ref("")
const showFailed = ref(false)
const failedMessage = ref("")

const cancelFxn = async() => {
  showGraduationWarning.value=false;
  showPromoteWarning.value=false;
  showGenerateTCWarning.value=false;
  console.log("cancelled")
}

const confirmGraduation = async () => {
  try {
    await axios.get(`http://localhost:8080/student/status/${currentStudentRegNo.value}`);
    window.open(`http://localhost:8080/pdf/generate/${currentStudentRegNo.value}`, '_blank');
    successMessage.value = `Transfer certificate generated for ID ${currentStudentRegNo.value}`;
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);
  } catch (err) {
    showFailed.value = true;
    failedMessage.value = 'Failed to graduate!'
    showGraduationWarning.value = false
    setTimeout(() => (showFailed.value = false), 3000);
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
    showFailed.value = true;
    failedMessage.value = 'Failed to promote!'
    showPromoteWarning.value = false
    setTimeout(() => (showFailed.value = false), 3000);
  } finally {
    showPromoteWarning.value = false;
  }
};

const updateStatusToRESCINDED = async () => {
  try {
    await axios.get(`http://localhost:8080/student/status/tc/${currentStudentRegNo.value}`);

    // ✅ Open the PDF in a new tab
    window.open(`http://localhost:8080/pdf/generate/${currentStudentRegNo.value}`, '_blank');

    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);
  } catch (error) {
    console.error(error);
    showFailed.value = true;
    setTimeout(() => (showFailed.value = false), 3000);
  } finally {
    showGenerateTCWarning.value = false;
  }
}
const showTCModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showGenerateTCWarning.value = true;
}

const showGraduationModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showGraduationWarning.value = true;
}

const showPromoteModel = (regNo) =>{
  currentStudentRegNo.value = regNo;
  showPromoteWarning.value = true;
}

const fetchStudentDetails = async () => {
  if (!props.regNo) return

  loading.value = true
  error.value = null
  student.value = null

  try {
    const response = await axios.get(
        `http://localhost:8080/student/${props.regNo}`
    )
    student.value = response.data
  } catch (err) {
    error.value =
        err.response?.data?.message || 'Failed to fetch student details.'
  } finally {
    loading.value = false
  }

}

onMounted(() => {
  if (props.regNo) {
    fetchStudentDetails()
  }
})

watch(() => props.regNo, (newVal, oldVal) => {
  if (newVal && newVal !== oldVal) {
    fetchStudentDetails()
  }
})



</script>

<style scoped>

.student-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.student-table td {
  padding: 0.5rem;
  border: 1px solid #ccc;
  vertical-align: top;
  word-break: break-word;
}

.student-table td:first-child {
  background-color: #f0f0f0;
  font-weight: bold;
  width: 30%;
}

.student-details-container {
  max-width: 100%;
  padding: 1rem;
  margin: auto;
  background: #f9f9f9;
  border-radius: 8px;
  overflow-x: auto; /* ✅ prevents overflow */
  box-sizing: border-box;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}

input {
  padding: 0.5rem;
  margin-top: 0.5rem;
  font-size: 14px;
  max-width: 100%; /* ✅ makes input responsive */
}

button {
  width:  100%;
  margin-top: 0.5rem;
  padding: 0.5rem;
  background-color: #007bff;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 14px;
}

.loading {
  color: #666;
}

.error {
  color: red;
}

.student-info {
  background: #e0ffe0;
  padding: 1rem;
  border-radius: 6px;
  font-size: 14px;
  word-break: break-word; /* ✅ wrap long text */
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

</style>