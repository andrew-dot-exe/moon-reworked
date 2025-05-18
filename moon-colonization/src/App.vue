<script setup lang="ts">
import { onMounted } from 'vue';
import { useComponentStore } from './stores/componentStore';
import userApi from './components/user/userApi';

const componentStore = useComponentStore()

const checkTokenFromCookie = (): boolean => {
  const cookies = document.cookie.split(';');
  const tokenCookie = cookies.find(cookie => cookie.trim().startsWith('refresh_token'));
  if (tokenCookie) {
    userApi.refresh();
    return true;
  }
  return false;
};

onMounted(() => {
  const isLogged: boolean = checkTokenFromCookie();
  if (isLogged) {
    componentStore.setComponent('zoneChooserMap');
  }
})
</script>

<template>
  <!-- Роутер  -->
  <component :is="componentStore.current" class="router-view" />
</template>

<style>
/* Глобальные стили для удаления отступов и полей */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

#app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}
</style>

<style scoped>
/* Ваши скоупированные стили */
.router-view {
  width: 100%;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  overflow: auto;
  display: flex;
  flex-direction: column;
}
</style>
