<template>
  <div class="vp-collapse">
    <button @click="isOpen = !isOpen" class="vp-collapse-btn">
      {{ isOpen ? '▼' : '▶' }} {{ title || 'Подробнее' }}
    </button>
    <Transition name="vp-fade">
      <div v-show="isOpen" class="vp-collapse-content">
        <slot />
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref } from 'vue'
defineProps({
  title: String
})

const isOpen = ref(false)
</script>

<!-- Пример использования
<Collapse>
  Этот текст будет скрыт/показан при клике на кнопку.
</Collapse>
-->
  
  
  <style scoped>
  .collapse-btn {
    background: var(--vp-c-brand);
    color: white;
    border: none;
    padding: 8px 12px;
    cursor: pointer;
    border-radius: 4px;
  }
  .collapse-content {
    margin-top: 10px;
    padding: 12px;
    border: 1px solid var(--vp-c-divider);
  }
  </style>