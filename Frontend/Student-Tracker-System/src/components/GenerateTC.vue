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
            <button @click="updateStatusToRESCINDED">Confirm</button>
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
  left: 50%;
  top: 80px;
  right: -2%;
  transform: translateX(-10%);
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