import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Any request to /api/... is forwarded to our Spring Boot backend.
    // The browser thinks it's talking to 5173 only, so no CORS problem.
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})