<template>
  <div class="app-container">
    <!-- Sidebar -->
    <div v-if="sidebarOpen" class="sidebar">
      <div class="sidebar-option" @click="selectView('addStudent')">
        Add Student
      </div>

      <div class="sidebar-option" @click="toggleDropdown('promote')">
        Promote Student
      </div>
      <div v-if="dropdowns.promote" class="dropdown">
        <div class="dropdown-option">Promote to Next Standard</div>
        <div class="dropdown-option">Generate TC</div>
      </div>

      <div class="sidebar-option" @click="toggleDropdown('details')">
        Show Student Details
      </div>
      <div v-if="dropdowns.details" class="dropdown">
        <div class="dropdown-option">Show by ID</div>
        <div class="dropdown-option">Show All</div>
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
import { ref, reactive, computed } from 'vue'
import AddStudentForm from './components/AddStudent.vue'

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
    default:
      return {
        template: `
          <div class="welcome-card">
            <h2>Student Tracker System</h2>
            <p>Click options in the sidebar to manage students.</p>
          </div>`
      }
  }
})

</script>
