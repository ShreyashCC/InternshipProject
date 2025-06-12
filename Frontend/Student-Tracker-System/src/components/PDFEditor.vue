<template>
  <div class="pdf-editor-wrapper">
    <div class="pdf-editor-header">
      <button @click="$emit('goBack')" class="go-back-btn">{{t('ShowAllStudents.GO_BACK')}}</button>
    </div>
    <iframe
        :src="pdfUrl"
        class="pdf-frame"
        frameborder="0"
    ></iframe>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
const props = defineProps({
  regNo: {
    type: String,
    required: true,
  }
})

const pdfUrl = computed(() => {
  return `http://localhost:8080/pdf/generate/${props.regNo}`
})
</script>

<style scoped>
.pdf-editor-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 0.5rem;
}

.pdf-editor-header {
  width: 30%;
  display: flex;
  justify-content: flex-start;
  padding: 0.5rem 2rem;
}

.go-back-btn {
  background-color: #dc3545;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
  margin-top: -40px;
}

/* ✅ Make iframe dynamically fit the screen */
.pdf-frame {
  width: 95vw;
  height: 80vh;
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-top: 0.5rem;
}

/* ✅ Optional: cap size on very large screens */
@media (min-width: 1400px) {
  .pdf-frame {
    max-width: 1000px;
    max-height: 600px;
  }
}
</style>

