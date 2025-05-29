<template>
  <div class="student-details-container">
    <h2>Show Student Details</h2>

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
      <p><strong>{{t('ShowAllStudents.RegistrationNo')}}</strong> {{student.regNo}}</p>
      <p><strong>{{t('ShowAllStudents.RollNo')}}</strong> {{student.rollNo}}</p>
      <p><strong>{{t('ShowAllStudents.FirstName')}}</strong> {{student.firstName}}</p>
      <p><strong>{{t('ShowAllStudents.LastName')}}</strong> {{student.lastName}}</p>
      <p><strong>{{t('ShowAllStudents.Standard')}}</strong> {{student.standard}}</p>
      <p><strong>{{t('ShowAllStudents.AdmissionDate')}}</strong> {{student.admissionDate}}</p>
      <p><strong>{{t('ShowAllStudents.Address')}}</strong> {{student.address}}</p>
      <p><strong>{{t('ShowAllStudents.Mobile')}}</strong> {{student.mobileNo}}</p>
      <p><strong>{{t('ShowAllStudents.Email')}}</strong> {{student.emailId}}</p>
      <p><strong>{{t('ShowAllStudents.Status')}}</strong> {{student.status}}</p>
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

<<<<<<< HEAD
// Auto-fetch on mount (optional, if regNo is already passed)


// Watch for prop changes
watch(() => props.regNo, (newVal, oldValue) => {
  if (newVal && newVal != oldValue) {
=======
onMounted(() => {
  if (props.regNo) {
>>>>>>> 088a205cbe3321d9f162ed12f9a2b1f1bb0f015b
    fetchStudentDetails()
  }
})


</script>

<style scoped>
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

</style>