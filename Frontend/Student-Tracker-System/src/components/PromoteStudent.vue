<template>
  <div class="form-container">
    <h2>{{t('Promote.promote')}}</h2>
    <form @submit.prevent="showPromoteModel">
      <div class="form-field">
        <label for="studentId">{{t('Promote.InputFieldText')}}</label>
        <input v-model="studentId" id="studentId" required />
        <button type="submit">{{t('Promote.btnText')}}</button>
        <p v-if="error" style="color: red;">{{ error }}</p>
      </div>

    </form>

    <div v-if="showPromoteWarning" class="modal-backdrop">
      <div class="modal-overlay">
        <div class="model">
          <h3>Promote Student?</h3>
          <div class="button-group">
            <button @click="promoteStudent">Confirm</button>
            <button @click="cancelFxn">Cancel</button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showSuccess" class="top-notification-success">
      Student Promoted
    </div>

    <div v-if="showFailed" class="top-notification-failed">
      Failed to Promote!
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import {useI18n} from "vue-i18n";
import PromoteStudent from "@/components/PromoteStudent.vue";
const {t}  = useI18n();
const error = ref(null)
const promoted = ref(null)
const showPromoteWarning = ref(false)
const currentStudentRegNo= ref(null)
const showSuccess = ref(false)
const showFailed = ref(false)

const studentId = ref('')

const showPromoteModel = (regNo) =>{
  currentStudentRegNo.value = regNo;


  showPromoteWarning.value = true;
}

const cancelFxn = async() =>{
  showPromoteWarning.value=false;
  console.log("cancelled");
}

async function promoteStudent() {
  error.value = null;
  promoted.value = null;
  try {
    const res = await axios.get(`http://localhost:8080/student/promoted/${studentId.value}`)
    promoted.value = res.data;

    showPromoteWarning.value = false;
    console.log("current warning value : ",showPromoteWarning)
    showSuccess.value = true;
    setTimeout(() => (showSuccess.value = false), 3000);

    studentId.value = ''
  } catch (err) {
    showFailed.value = true;
    showPromoteWarning.value = false;
    setTimeout(() => (showFailed.value = false), 3000);
  }
}
</script>

<style scoped>

.form-container {
  max-width: 600px;
  background: white;
  margin: auto;
  border-radius: 10px;
  width: 350px;
  padding: 1rem;
  box-sizing: border-box;
}

.form-field {
  margin-bottom: 1rem;
  display: flex;
  flex-direction: column;
}

input,
button {
  padding: 0.5rem;
  box-sizing: border-box;
  font-size: 14px;
}

button {
  margin-top: 0.5rem;
  background-color: #007bff;
  border: none;
  color: white;
  cursor: pointer;
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

.top-notification-success {
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
}

.top-notification-failed {
  position: fixed;
  top: 80px;
  right: -2%;
  transform: translateX(-15%);
  background-color: #f8d7da;
  color: #721c24;
  padding: 12px 24px;
  border-radius: 8px;
  border: 1px solid #c3e6cb;
  z-index: 1001;
  font-weight: bold;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  transition: opacity 0.5s ease;
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
