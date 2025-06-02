<template>
  <div class="generate-TC-container">
    <h2>{{t('Promote.GenerateTransferCertificate')}}</h2>

    <div class="form-group">
      <label for="regNumber">{{t('Promote.InputFieldText')}}</label>
      <input
          v-model="regNo"
          type="text"
          id="regNo"
          placeholder="e.g. 1"
      />
      <button @click="generateTC">{{t('Promote.btntext3')}}</button>
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
import { PDFDocument, StandardFonts, rgb } from 'pdf-lib'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()

const regNo = ref('')
const TC = ref(null)
const loading = ref(false)
const error = ref(null)

const generateTC = async () => {
  loading.value = true
  error.value = null
  TC.value = null

  try {
    const response = await axios.get(`http://localhost:8080/student/${regNo.value}`)
    TC.value = response.data
    await generateEditablePDF(TC.value)
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to fetch TC details.'
  } finally {
    loading.value = false
  }
}

async function generateEditablePDF(tc) {
  const pdfDoc = await PDFDocument.create()
  const page = pdfDoc.addPage([595, 842])
  const form = pdfDoc.getForm()
  const font = await pdfDoc.embedFont(StandardFonts.HELVETICA)

  const drawLabel = (text, x, y) => {
    page.drawText(text, { x, y, size: 12, font, color: rgb(0, 0, 0) })
  }

  let y = 780
  const lineHeight = 25

  drawLabel('TRANSFER CERTIFICATE', 200, y)
  y -= lineHeight * 2

  drawLabel(`Name: ${tc.student.firstName} ${tc.student.lastName}`, 50, y)
  y -= lineHeight
  drawLabel(`Reg No: ${tc.student.regNo}    Roll No: ${tc.student.rollNo}`, 50, y)
  y -= lineHeight
  drawLabel(`Class: ${tc.student.standard}`, 50, y)
  y -= lineHeight
  drawLabel(`DOB: ${tc.DOB}`, 50, y)
  y -= lineHeight
  drawLabel(`Admission Date: ${tc.student.admissionDate}`, 50, y)
  y -= lineHeight
  drawLabel(`Mobile: ${tc.student.mobileNo}`, 50, y)
  y -= lineHeight
  drawLabel(`Email: ${tc.student.emailId}`, 50, y)
  y -= lineHeight

  // Editable: Guardian Name
  drawLabel('Guardian Name:', 50, y)
  const guardianField = form.createTextField('guardianName')
  guardianField.setText(tc.guardianName)
  guardianField.addToPage(page, { x: 160, y: y - 4, width: 300, height: 20 })
  y -= lineHeight

  // Editable: Reason of Leaving
  drawLabel('Reason of Leaving:', 50, y)
  const reasonField = form.createDropdown('reasonOfLeaving')
  reasonField.addOptions(['PASSED_AND_LEFT', 'RUSTICATED', 'ADMISSION_REVOKED'])
  reasonField.select(tc.reasonOfLeaving || 'PASSED_AND_LEFT')
  reasonField.addToPage(page, { x: 180, y: y - 4, width: 200, height: 20 })
  y -= lineHeight

  // Editable: Remarks
  drawLabel('Remarks:', 50, y)
  const remarksField = form.createDropdown('remarks')
  remarksField.addOptions(['BAD', 'BELOW_AVERAGE', 'AVERAGE', 'GOOD', 'EXCELLENT', 'BRILLIANT'])
  remarksField.select(tc.remark || 'GOOD')
  remarksField.addToPage(page, { x: 120, y: y - 4, width: 150, height: 20 })
  y -= lineHeight * 2

  drawLabel('Signature of Principal', 400, y)
  y -= lineHeight
  drawLabel('(With Seal)', 420, y)

  const pdfBytes = await pdfDoc.save()
  const blob = new Blob([pdfBytes], { type: 'application/pdf' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `Editable_TC_${tc.student.regNo}.pdf`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
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