<template>
  <div class="form-container">
    <h2>Promote Student</h2>
    <form @submit.prevent="promoteStudent">
      <div class="form-field">
        <label for="studentId">Enter Registration Number</label>
        <input v-model="studentId" id="studentId" required />
        <button type="submit">Promote</button>
      </div>
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
  width: 100%;
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

</style>
