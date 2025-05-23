import { defineConfig } from 'vitepress';

export default defineConfig({
  // ...existing config
  server: {
    allowedHosts: ['bfg10k.ru']
  }
});
