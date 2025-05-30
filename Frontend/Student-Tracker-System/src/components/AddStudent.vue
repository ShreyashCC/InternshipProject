<template>
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
  </div>
</template>

<script setup>
import { reactive } from 'vue'
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
async function submitForm() {
  const studentData = {
    ...form,
    admissionDate: Date.now(),
    status: "ACTIVE"
  }

  try {
    const response = await axios.post('http://localhost:8080/student', studentData)
    alert('Student Added Successfully!')
    console.log(response.data)

    // Reset form
    Object.keys(form).forEach(key => form[key] = '')

  } catch (error) {
    console.error('Error submitting form:', error)
    alert('Failed to add student.')
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
</style>

