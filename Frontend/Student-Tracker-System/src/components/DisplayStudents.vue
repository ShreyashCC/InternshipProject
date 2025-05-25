<template>
  <div class="students-table-container">
    <h2>All Students</h2>

    <div v-if="loading" class="loading">Loading student records...</div>
    <div v-if="error" class="error">{{ error }}</div>

    <table v-if="students.length" class="students-table">
      <thead>
      <tr>
        <th>Registration No.</th>
        <th>Roll No.</th>
        <th>Firstname</th>
        <th>Lastname</th>
        <th>Standard</th>
        <th>Admission Date</th>
        <th>Address</th>
        <th>Mobile No</th>
        <th>Email Id</th>
        <th>Status</th>
        <!-- Add more fields as needed -->
      </tr>
      </thead>
      <tbody>
      <tr v-for="student in students" :key="student.regNo">
        <td>{{student.regNo}}</td>
        <td>{{student.rollNo}}</td>
        <td>{{student.firstName}}</td>
        <td>{{student.lastName}}</td>
        <td>{{student.standard}}</td>
        <td>{{student.admissionDate}}</td>
        <td>{{student.address}}</td>
        <td>{{student.mobileNo}}</td>
        <td>{{student.emailId}}</td>
        <td>{{student.status}}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const students = ref([])
const loading = ref(true)
const error = ref(null)

const fetchStudents = async () => {
  try {
    const response = await axios.get('http://localhost:8080/student')
    students.value = response.data
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to fetch students.'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStudents()
})
</script>

<style scoped>
.students-table-container {
  padding: 1rem;
  background: #f1f1f1;
  border-radius: 8px;
  max-width: 1000px;
  margin: auto;
}
.students-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
.students-table th,
.students-table td {
  border: 1px solid #ccc;
  padding: 0.75rem;
  text-align: left;
}
.students-table th {
  background-color: #007bff;
  color: white;
}
.loading {
  color: #666;
}
.error {
  color: red;
}
</style>
