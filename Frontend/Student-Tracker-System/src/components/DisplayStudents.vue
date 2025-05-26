<template>
  <div class="students-table-container">
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
        <th>Promote Student</th>
        <th> TC Generation </th>
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
        <td><button @click="promoteStudent(student.regNo)" class="promote-button">Promote</button></td>
        <td><button @click="generateTC(student.regNo)" class="promote-button">TC</button></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import {ref, onMounted, onUpdated} from 'vue'
import axios, {HttpStatusCode} from 'axios'

const students = ref([])
const loading = ref(true)
const error = ref(null)
const TC = ref(null)

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
const promoteStudent = async (regNo) => {
  try {
    await axios.get(`http://localhost:8080/student/promoted/${regNo}`)
    alert(`Student ID ${regNo} promoted successfully!`)
    // fetchStudents() // Optional: Refresh the table
  } catch (error) {
    console.error('Error:', error)
    alert('Failed to promote student. The Student is Already Graduated')
  }
}

const generateTC = async (regNoParam) => {
  loading.value = true
  error.value = null
  TC.value = null

  try {
    const response = await axios.get(`http://localhost:8080/student/tc/${regNoParam}`)
    TC.value = response.data
    alert(`TC for Reg No ${regNoParam} generated successfully!`)
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to generate TC.'
    alert(`Failed to generate TC for Reg No ${regNoParam}`)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchStudents()
})
onUpdated(()=> {
  fetchStudents()
})
</script>

<style scoped>
.students-table-container {
  margin-top: 0;
  padding: 1.5rem;
  background: #fff;
  border-radius: 12px;
  max-width: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow-x: auto;
}

.students-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  font-family: 'Segoe UI', sans-serif;
  table-layout: auto;
  min-width: 900px;
}

.students-table th,
.students-table td {
  padding: 0.75rem 1rem;
  text-align: left;
  font-size: 14px;
  word-break: break-word;
  vertical-align: top;
}

.students-table th {
  background-color: #007bff;
  color: white;
  font-weight: 600;
  text-transform: uppercase;
}

.students-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.promote-button {
  background-color: #007bff;
  border: none;
  color: white;
  padding: 0.4rem 0.8rem;
  font-size: 13px;
  font-weight: 500;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.promote-button:hover {
  background-color: #0056b3;
  transform: translateY(-1px);
}

.promote-button:focus {
  outline: 2px solid #0056b3;
  outline-offset: 2px;
}

.loading,
.error {
  text-align: center;
  font-size: 16px;
  margin-top: 1rem;
}

.error {
  color: red;
}

/* Responsive tweaks for smaller screens */
@media (max-width: 768px) {
  .students-table-container {
    padding: 1rem;
  }

  .students-table th,
  .students-table td {
    padding: 0.5rem;
    font-size: 13px;
  }

  .promote-button {
    font-size: 12px;
    padding: 0.35rem 0.6rem;
  }
}

@media (max-width: 480px) {
  .students-table {
    font-size: 12px;
  }

  .students-table th,
  .students-table td {
    padding: 0.4rem;
  }

  .promote-button {
    font-size: 11px;
    padding: 0.3rem 0.5rem;
  }
}
</style>
