
import { createApp } from 'vue'
import App from './App.vue'
import './styles.css'
import {createI18n} from "vue-i18n";
import English from "@/langs/English.js";
import Hindi from "@/langs/Hindi.js";
import Marathi from "@/langs/Marathi.js";
import Bengali from "@/langs/Bengali.js";

const i18n = createI18n({
    legacy: false,
    locale: 'en-US',
    fallbackLocale: 'hi-IN',
    messages: {
        'en-US' : English.message,
        'hi-IN': Hindi.message,
        'mr-IN': Marathi.message,
        'bn-IN': Bengali.message,
    }
})
const app = createApp(App);
app.use(i18n)
app.mount('#app')
