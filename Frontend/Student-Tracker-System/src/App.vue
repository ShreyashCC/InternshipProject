<template>
  <div class="app-container">
    <!-- Sidebar -->
    <div v-if="sidebarOpen" class="sidebar">
      <div class="sidebar-option" @click="selectView('home')">
        {{t('SideBar.Home')}}
      </div>
      <div class="sidebar-option" @click="selectView('addStudent')">
        {{t('SideBar.AddStudent')}}
      </div>

      <div class="sidebar-option" @click="toggleDropdown('promote')">
        {{t('SideBar.PromoteStudent')}}
      </div>
      <div v-if="dropdowns.promote" class="dropdown">
        <div class="dropdown-option" @click="selectView('promoteStudent')">{{t('SideBar.PromoteStudent')}}</div>
        <div class="dropdown-option" @click="selectView('generateTC')">{{t('SideBar.GenerateTC')}}</div>
      </div>
<!--      <div class="sidebar-option" @click="toggleDropdown('details')">-->
<!--        Show Student Details-->
<!--      </div>-->
<!--      <div v-if="dropdowns.details" class="dropdown">-->
<!--        <div class="dropdown-option" @click="selectView('showStudentDetails')">Show by ID</div>-->
<!--        <div class="dropdown-option" @click="selectView('showAllStudents')">Show All</div>-->
<!--      </div>-->
      <div class="sidebar-option" @click="selectView('showAllStudents')">
        {{t('SideBar.ShowAllStudents')}}
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
        <div class="title-container">
          <h1 class = "app-title">{{t('homeContent.schoolName')}}</h1>
          <h1 class="school-title">{{t('header.name')}}</h1>
        </div>
        <div class="search-bar">
          <!--          <label for="search">Search Students by Registration No:</label>-->
          <input id="search" type="text" v-model="searchInput" :placeholder="t('header.Placeholder')" />
          <button @click="searchStudent">{{t('header.Search')}}</button>
        </div>

          <div class="end-container">
            <div class="language-selector">
              <label for="language">{{t('header.ChangeLanguage')}}</label>
              <select v-model = "locale" id="language">
                <!--            <button class="language-btn">Change Language</button>-->
                <option v-for = "locale in availableLocales" :value="locale" key="`locale-${locale}`">{{locale}}</option>
              </select>
            </div>
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
import {useI18n} from "vue-i18n";
const {t, availableLocales, locale}  = useI18n();

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
.main-content {
  padding: 0.5rem;
}
.header {
  display: flex;
  align-items: start;
  gap: 1rem;
  padding: 10px 10px 5px 5px;
  background-color: #f4f4f4;
  border-bottom: 1px solid #ccc;
}
.home-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.end-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  margin-left: auto;
}
.app-title {
  margin: 0;
  font-size: 1.5rem;
  flex-grow: 1;
  color: #3498DB
}
.burger-btn {
  font-size: 1rem;
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
.title-container {
  display: flex;
  flex-direction: column;
}
.school-title {
  font-size: 1rem;
  margin-top: 0;
  flex-grow: 1;
}
</style>