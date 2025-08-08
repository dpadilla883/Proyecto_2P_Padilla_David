import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // todo lo que empiece por /api se reenvía al backend
      '/api': {
        target: 'http://localhost:8083',
        changeOrigin: true,
      },
    },
  },
});
