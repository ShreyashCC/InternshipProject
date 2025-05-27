<template>
  <div class="welcome-card">
    <h2>{{t('homeContent.name')}}</h2>
    <p>{{t('homeContent.showMessage')}}</p>
  </div>
</template>

<script setup>
import {useI18n} from "vue-i18n";
const {t}  = useI18n();
// No logic needed
</script>
