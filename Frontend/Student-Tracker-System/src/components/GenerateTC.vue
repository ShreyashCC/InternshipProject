<template>
  <div class="generate-TC-container">
    <h2>Generate Transfer Certificate</h2>

    <div class="form-group">
      <label for="regNumber">Enter Registration Number:</label>
      <input
          v-model="regNo"
          type="text"
          id="regNo"
          placeholder="e.g. 1"
      />
      <button @click="generateTC">Get Details</button>
    </div>

    <div v-if="loading" class="loading">Loading...</div>

    <div v-if="error" class="error">{{ error }}</div>

    <div v-if="TC" class="TC-message">
      <h3>TC Generated</h3>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const regNo = ref('')
const TC = ref(null)
const loading = ref(false)
const error = ref(null)

const generateTC = async () => {
  loading.value = true
  error.value = null
  TC.value = null

  try {
    const response = await axios.get(
        // `http://localhost:8080/student/${regNo.value}`
    //     call TC Interface
    )
    student.value = response.data
  } catch (err) {
    error.value =
        err.response?.data?.message || 'Failed to fetch TC details.'
  } finally {
    loading.value = false
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
</style>