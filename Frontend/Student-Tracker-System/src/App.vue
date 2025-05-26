<template>
  <div class="app-container">
    <!-- Sidebar -->
    <div v-if="sidebarOpen" class="sidebar">
      <div class="sidebar-option" @click="selectView('home')">
        Home
      </div>
      <div class="sidebar-option" @click="selectView('addStudent')">
        Add Student
      </div>

      <div class="sidebar-option" @click="toggleDropdown('promote')">
        Promote Student
      </div>
      <div v-if="dropdowns.promote" class="dropdown">
        <div class="dropdown-option" @click="selectView('promoteStudent')">Promote to Next Standard</div>
        <div class="dropdown-option" @click="selectView('generateTC')">Generate TC</div>
      </div>
<!--      <div class="sidebar-option" @click="toggleDropdown('details')">-->
<!--        Show Student Details-->
<!--      </div>-->
<!--      <div v-if="dropdowns.details" class="dropdown">-->
<!--        <div class="dropdown-option" @click="selectView('showStudentDetails')">Show by ID</div>-->
<!--        <div class="dropdown-option" @click="selectView('showAllStudents')">Show All</div>-->
<!--      </div>-->
      <div class="sidebar-option" @click="selectView('showAllStudents')">
        Show All Students
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
<!--      <div class="header">-->
<!--        <button class="burger-btn" @click="sidebarOpen = !sidebarOpen">-->
<!--          ☰-->
<!--        </button>-->
<!--      </div>-->
      <div class="header">
        <h1 class="app-title">Student Management System</h1>
        <div class="search-bar">
          <!--          <label for="search">Search Students by Registration No:</label>-->
          <input id="search" type="text" v-model="searchInput" placeholder="Enter Student Registration No." />
          <button @click="searchStudent">Search</button>
        </div>
        <div class="language-selector">
          <label for="language">Change Language:</label>
          <select id="language">
            <button class="language-btn">Change Language</button>
            <option>English</option>
            <option>Hindi</option>
          </select>
        </div>
        <button class="burger-btn" @click="sidebarOpen = !sidebarOpen">☰</button>
      </div>
<!--      <div>-->
<!--        <component :is="currentComponent" :reg-no="selectedRegNo" />-->
<!--      </div>-->

     <div :class="currentView === 'home' ? 'home-content' : 'content'">
        <component :is="currentComponent" :regNo="selectedRegNo" />
      </div>
      </div>
  </div>
</template>

<script setup>
import {ref, reactive, computed} from 'vue'
import AddStudentForm from './components/AddStudent.vue'
import HomeComponent from "./components/HomeComponent.vue";
import PromoteStudent from './components/PromoteStudent.vue'
import DisplayStudentDetails from './components/DisplayStudentDetails.vue'
import GenerateTC from "@/components/GenerateTC.vue";
import DisplayStudents from './components/DisplayStudents.vue'

const searchInput = ref('')
const selectedRegNo = ref('')
function searchStudent() {
  if (searchInput.value.trim()) {
    selectedRegNo.value = searchInput.value.trim()
    selectView('showStudentDetails')
  }
}


const sidebarOpen = ref(true)

const dropdowns = reactive({
  promote: false,
  details: false,
  add: false
})

const currentView = ref('home') // 'home' | 'addStudent'

function toggleDropdown(menu) {
  dropdowns[menu] = !dropdowns[menu]
}

function selectView(view) {
  currentView.value = view
}

const currentComponent = computed(() => {
  switch (currentView.value) {
    case 'addStudent':
      return AddStudentForm
    case 'promoteStudent':
      return PromoteStudent
    case 'showStudentDetails':
      return DisplayStudentDetails
    case 'showAllStudents':
      return DisplayStudents
    case 'generateTC':
      return GenerateTC
    default:
      return HomeComponent
  }
})

</script>
<style scoped>
.content {
  display: flex;
  align-items: flex-start;
}
.header {
  display: flex;
  align-items: start;
  gap: 1rem;
  padding: 1rem;
  background-color: #f4f4f4;
  border-bottom: 1px solid #ccc;
}
.home-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.app-title {
  margin: 0;
  font-size: 1.5rem;
  flex-grow: 1;
}

.search-bar input {
  padding: 0.4rem;
}

.language-selector select,
.language-btn {
  margin-left: 0.5rem;
  padding: 0.4rem;
}

.search-bar button {
  margin-left: 0.5rem;
  padding: 0.4rem;
}

</style>