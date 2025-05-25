<template>
  <div class="form-container">
    <h2>Promote Student</h2>
    <form @submit.prevent="promoteStudent">
      <div class="form-field">
        <label for="studentId">Student ID:</label>
        <input v-model="studentId" id="studentId" required />
      </div>
      <button type="submit">Promote</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const studentId = ref('')

async function promoteStudent() {
  try {
    await axios.get(`http://localhost:8080/student/promoted/${studentId.value}`)
    alert(`Student ID ${studentId.value} promoted successfully!`)
    studentId.value = ''
  } catch (error) {
    console.error('Error:', error)
    alert('Failed to promote student. Check the ID and try again.')
  }
}
</script>

<style scoped>
.form-container {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 350px;
}
.form-field {
  margin-bottom: 12px;
}
input {
  width: 100%;
  padding: 6px;
}
button {
  padding: 8px 12px;
}
</style>
