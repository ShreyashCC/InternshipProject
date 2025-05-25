<template>
  <div class="app-container">
    <!-- Sidebar -->
    <div v-if="sidebarOpen" class="sidebar">
      <div class="sidebar-option" @click="selectView('HomeComponent')">
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
      <div class="sidebar-option" @click="toggleDropdown('details')">
        Show Student Details
      </div>
      <div v-if="dropdowns.details" class="dropdown">
        <div class="dropdown-option" @click="selectView('showStudentDetails')">Show by ID</div>
        <div class="dropdown-option" @click="selectView('showAllStudents')">Show All</div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="header">
        <button class="burger-btn" @click="sidebarOpen = !sidebarOpen">
          ☰
        </button>
      </div>

      <div class="content">
        <component :is="currentComponent" />
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
