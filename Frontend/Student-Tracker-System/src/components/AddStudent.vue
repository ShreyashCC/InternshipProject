<template>
  <div class="form-container">
    <h2>Add New Student</h2>
    <form @submit.prevent="submitForm">
      <div class="form-field">
        <label>First Name:</label>
        <input v-model="form.firstName" required />
      </div>

      <div class="form-field">
        <label>Last Name:</label>
        <input v-model="form.lastName" required />
      </div>

      <div class="form-field">
        <label>Standard (Class):</label>
        <input v-model="form.standard" required />
      </div>

      <div class="form-field">
        <label>Address:</label>
        <input v-model="form.address" required />
      </div>

      <div class="form-field">
        <label>Mobile Number:</label>
        <input v-model="form.mobileNo" type="tel" required />
      </div>

      <div class="form-field">
        <label>Email ID:</label>
        <input v-model="form.emailId" type="email" required />
      </div>

      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import axios from 'axios'

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
}
.form-field {
  margin-bottom: 15px;
}
input {
  width: 100%;
  padding: 6px;
}
button {
  padding: 8px 12px;
}
</style>
