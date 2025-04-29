<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
<!-- Роутер  -->
  <RouterView class="router-view" />
</template>

<style scoped>
.router-view {
  width: 100%;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  overflow: auto;
}
</style>
