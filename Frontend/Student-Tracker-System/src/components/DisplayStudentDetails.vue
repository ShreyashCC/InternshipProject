<template>
  <div v-if="!showPDFeditor" class="student-details-container">
    <h2>{{ t('ShowAllStudents.StudentInformation') }}</h2>

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
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.RegistrationNo') }}</strong></td>
          <td>{{ student.regNo }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.RollNo') }}</strong></td>
          <td>{{ student.rollNo }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.FirstName') }}</strong></td>
          <td>{{ student.firstName }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.LastName') }}</strong></td>
          <td>{{ student.lastName }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Standard') }}</strong></td>
          <td>{{ student.standard }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.AdmissionDate') }}</strong></td>
          <td>{{ student.admissionDate }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Address') }}</strong></td>
          <td>{{ student.address }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Mobile') }}</strong></td>
          <td>{{ student.mobileNo }}</td>
        </tr>
        </tbody>
        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Email') }}</strong></td>
          <td>{{ student.emailId }}</td>
        </tr>
        </tbody>

        <tbody>
        <tr>
          <td><strong>{{ t('ShowAllStudents.Status') }}</strong></td>
          <td>{{ student.status }}</td>
        </tr>
        </tbody>
      </table>
      <div class = "btn-container">
        <div v-if="student.status === 'ACTIVE'" > <button @click="showTCModel(student.regNo)" class="promote-button">{{ t('Promote.btnText2') }}</button></div>
        <div v-if = "student.standard != '12' && student.status == 'ACTIVE'"><button @click="showPromoteModel(student.regNo)" class="promote-button">{{t('Promote.btnText')}}</button></div>
        <div v-if="student.standard == '12' && student.status == 'ACTIVE'"><button @click="showGraduationModel(student.regNo)" class="promote-button">{{t('Promote.btnText4')}}</button></div>
      </div>
    </div>
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
            <button @click="confirmPromote">{{ t('ShowAllStudents.confirm') }}</button>
            <button @click="cancelFxn">{{ t('ShowAllStudents.cancel') }}</button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showGenerateTCWarning" class="modal-backdrop">
      <div class="modal-overlay">
        <div class="model">
          <h3>{{ t('ShowAllStudents.generate_tc_title') }}</h3>
          <div class="button-group">
            <button @click="updateStatusToRESCINDED">{{t('ShowAllStudents.confirm')}}</button>
            <button @click="cancelFxn">{{t('ShowAllStudents.confirm')}}</button>
          </div>
        </div>
      </div>
    </div>



  </div>
  <template v-else>
    <PDFEditor :regNo="currentStudentRegNo" @goBack="showPDFeditor = false" />
  </template>

  <div v-if="showSuccess" class="top-notification">
    {{ successMessage }}
  </div>

  <div v-if="showFailed" class="top-notification-failed">
    {{ failedMessage }}
  </div>

</template>

<script setup>
import {ref, watch, onMounted, onBeforeUpdate} from 'vue'
import axios from 'axios'
import {useI18n} from "vue-i18n";
import PDFEditor from "@/components/PDFEditor.vue";
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
const showPDFeditor = ref(null)

const cancelFxn = async() => {
  showGraduationWarning.value=false;
  showPromoteWarning.value=false;
  showGenerateTCWarning.value=false;
  console.log("cancelled")
}

const confirmGraduation = async () => {
  try {
    await axios.get(`http://localhost:8080/student/status/${currentStudentRegNo.value}`);
    // window.open(`http://localhost:8080/pdf/generate/${currentStudentRegNo.value}`, '_blank');
    showPDFeditor.value = true;
    successMessage.value = t('ShowAllStudents.success_tc_generated', { regNo: currentStudentRegNo.value })  ;
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);
    fetchStudentDetails();
  } catch (err) {
    showFailed.value = true;
    failedMessage.value = t('ShowAllStudents.failed_to_graduate');
    showGraduationWarning.value = false
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
    setTimeout(() => (showSuccess.value = false), 3000);
    fetchStudentDetails();
  } catch (error) {
    showFailed.value = true;
    failedMessage.value = t('Promote.failed');
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
    // window.open(`http://localhost:8080/pdf/generate/${currentStudentRegNo.value}`, '_blank');
    showPDFeditor.value = true;

    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);

    fetchStudentDetails();
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


.promote-button {
  margin: 0.5rem 2rem 0 1.5rem;
  flex: 1 1 160px;
  max-width: 220px;
  padding: 0.6rem 1rem;
  background-color: #007bff;
  border: none;
  color: white;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  cursor: pointer;
  text-align: center;
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

.btn-container {
  display: flex;
  flex-wrap: wrap;            /* Allows buttons to wrap on smaller screens */
  gap: 0.75rem;               /* Space between buttons */
  justify-content: center;    /* Center buttons */
  margin-top: 1rem;
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

</style>