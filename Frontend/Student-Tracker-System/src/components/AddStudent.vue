<template>
  <div v-if="notification.message" :class="['notification', notification.type]">
    {{ notification.message }}
  </div>

  <div class="form-container">
    <h2>{{t('SideBar.AddStudent')}}</h2>
    <form @submit.prevent="submitForm">
      <div class="form-field">
        <label>{{t('AddNewStudent.FirstName')}}</label>
        <input v-model="form.firstName" required />
      </div>

      <div class="form-field">
        <label>{{t('AddNewStudent.LastName')}}</label>
        <input v-model="form.lastName"/>
      </div>

      <div class="form-field">
        <label>{{t('AddNewStudent.Standard')}}</label>
        <input v-model="form.standard" required />
      </div>

      <div class="form-field">
        <label>{{t('AddNewStudent.Address')}}</label>
        <input v-model="form.address" required />
      </div>

      <div class="form-field">
        <label>{{t('AddNewStudent.Mobile')}}</label>
        <input v-model="form.mobileNo" type="tel" required />
      </div>

      <div class="form-field">
        <label>{{t('AddNewStudent.Email')}}</label>
        <input v-model="form.emailId" type="email" required />
      </div>

      <button type="submit">{{t('AddNewStudent.Submit')}}</button>
    </form>

    <div v-if="notification.message" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

  </div>
</template>

<script setup>
import { reactive , ref} from 'vue'
import axios from 'axios'
import {useI18n} from "vue-i18n";
const {t}  = useI18n();

const form = reactive({
  firstName: '',
  lastName: '',
  standard: '',
  address: '',
  mobileNo: '',
  emailId: '',
})

const notification = reactive({
  message: '',
  type: ''
})

function  showNotification(message , type){
  notification.message = message;
  notification.type = type;

  setTimeout(()=>{
    notification.message = ''
  } , 2500)
}


async function submitForm() {
  const namePattern = /^\d+$/;
  if (namePattern.test(String(form.firstName).trim()) || namePattern.test(String(form.lastName).trim())) {
    showNotification(t('AddNewStudent.error_firstname_lastname_number'), 'error');
    return;
  }
  const standard = parseInt(form.standard);
  if (isNaN(standard) || standard < 1 || standard > 12) {
    showNotification(t('AddNewStudent.error_standard_range'), 'error');
    return;
  }
  const studentData = {
    ...form,
    standard: standard,
    admissionDate: Date.now(),
    status: "ACTIVE"
  }

  try {
    const response = await axios.post('http://localhost:8080/student', studentData)
    showNotification(t('AddNewStudent.success_student_added'), 'success');
    console.log(response.data)
    Object.keys(form).forEach(key => form[key] = '')

  } catch (error) {
    console.error('Error submitting form:', error)
    showNotification(t('AddNewStudent.error_student_add_failed'), 'error');
  }
}
</script>

<style scoped>
.form-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  box-sizing: border-box;
  margin: auto;
}

.form-field {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  padding: 10px;
  width: 100%;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  box-sizing: border-box;
  margin-top: 10px;
}

button:hover {
  background-color: #0056b3;
}

.notification {
  position: fixed;
  top: 80px;
  right: -2%;
  transform: translateX(-10%);
  z-index: 9999;
  padding: 12px 20px;
  border-radius: 6px;
  text-align: center;
  font-weight: bold;
  min-width: 300px;
  max-width: 80%;
  transition: opacity 0.5s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.notification.success {
  background-color: #d4edda;
  color: #155724;
}

.notification.error {
  background-color: #f8d7da;
  color: #721c24;
}

</style>

