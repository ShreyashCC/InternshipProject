<script setup>
import { ref, onMounted } from 'vue'
// Use the correct import path
import * as pdfjsLib from 'pdfjs-dist'

pdfjsLib.GlobalWorkerOptions.workerSrc =
    'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.worker.min.js'

const canvasRef = ref(null)

onMounted(async () => {
  const url = sessionStorage.getItem('editablePdfUrl')

  if (!url) {
    alert('No PDF URL found')
    return
  }

  try {
    const loadingTask = pdfjsLib.getDocument(url)
    const pdf = await loadingTask.promise
    const page = await pdf.getPage(1)

    const scale = 1.5
    const viewport = page.getViewport({ scale })
    const canvas = canvasRef.value
    const context = canvas.getContext('2d')
    canvas.width = viewport.width
    canvas.height = viewport.height

    await page.render({ canvasContext: context, viewport }).promise
  } catch (error) {
    console.error('PDF rendering failed:', error)
    alert('Failed to load PDF.')
  }
})
</script>

<template>
  <div style="text-align: center; padding: 20px;">
    <canvas ref="canvasRef"></canvas>
  </div>
</template>