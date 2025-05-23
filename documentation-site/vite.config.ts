import { defineConfig } from 'vite';

export default defineConfig({
  // ...existing config
  server: {
    allowedHosts: ['bfg10k.ru']
  }
});
