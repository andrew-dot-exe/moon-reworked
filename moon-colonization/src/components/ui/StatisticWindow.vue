
<script setup lang="ts">
defineProps<{
  toggle?: () => void
}>();
</script>

<template>
  <div class="statistics-content">
    <div class="statistics-header">
      <h3>Статистика</h3>
      <button @click="$emit('toggle')" class="close-button">×</button>
    </div>
    <div class="statistics-body">
        <!-- Ваш контент статистики здесь -->
        <p>Общая площадь: 1000 км²</p>
        <p>Застроено: 300 км²</p>
        <p>Свободно: 700 км²</p>
    </div>
  </div>
    
</template>

<style scoped>
.statistics-content {
  background: white;
  border-radius: 12px;
  width: 80%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
}

.statistics-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.close-button:hover {
  color: #000;
}

.statistics-body {
  /* Стили для содержимого статистики */
}
</style>