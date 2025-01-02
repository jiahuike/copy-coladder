import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import monacoEditorPlugin from 'vite-plugin-monaco-editor'


// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    (monacoEditorPlugin ).default({})
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
    
  },
  publicDir:'public',
  server:{
    proxy:{
      '/api':{
        //获取路径中含有/api的请求
        target:'http://localhost:8080',//后台服务源
        changeOrigin:true,
        rewrite:(path) => path.replace(/^\/api/,'') ///api 换位‘’
      },
      '/article':{
        target:'http://localhost:5173',
        changeOrigin:true,
        rewrite:(path) => path.replace(/^\/article/,'')
      }
    }
  },

})
