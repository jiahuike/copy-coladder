import './assets/main.scss'

import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import router from '@/router'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
import 'element-plus/dist/index.css'




const pinia = createPinia();
const app = createApp(App);
const persistedstate = createPersistedState();

pinia.use(persistedstate);
app.use(pinia);
app.use(router);
app.use(ElementPlus);


app.mount('#app')
