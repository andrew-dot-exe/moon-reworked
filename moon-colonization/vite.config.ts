import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // Перенаправляем все запросы, начинающиеся с `/api`, на бэкенд
      '/api': {
        target: 'http://localhost:8080', // URL вашего бэкенда (Spring Boot)
        changeOrigin: true, // Меняет заголовок `Origin` на целевой URL
        secure: false, // Если бэкенд на HTTP (не HTTPS)
        rewrite: (path) => path.replace(/^\/api/, ''), // Убираем `/api` из пути
      },
    },
  },
})
