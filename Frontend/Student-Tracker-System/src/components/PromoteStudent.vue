<template>
  <div class="form-container">
    <h2>{{t('Promote.promote')}}</h2>
    <form @submit.prevent="promoteStudent">
      <div class="form-field">
        <label for="studentId">{{t('Promote.InputFieldText')}}</label>
        <input v-model="studentId" id="studentId" required />
        <button type="submit">{{t('Promote.btnText')}}</button>
        <p v-if="error" style="color: red;">{{ error }}</p>
      </div>

    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import {useI18n} from "vue-i18n";
const {t}  = useI18n();
const error = ref(null)
const promoted = ref(null)

const studentId = ref('')

async function promoteStudent() {
  error.value = null;
  promoted.value = null;
  try {
    const res = await axios.get(`http://localhost:8080/student/promoted/${studentId.value}`)
    promoted.value = res.data;
    alert(`Student ID ${studentId.value} promoted successfully!`)
    studentId.value = ''
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to Promote.'
    console.error('Error:', error)
    alert('Failed to promote student. Check the ID or Standard and try again.')
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
