<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useModuleInfoStore } from '@/stores/moduleInfoStore'
import ModuleComponent from '@/components/ui/ModuleComponent.vue'

const moduleInfoStore = useModuleInfoStore()
const { selectedModule } = storeToRefs(moduleInfoStore)
</script>

<template>
  <div v-if="selectedModule">
    <ModuleComponent :data="selectedModule" />
  </div>
  <div v-else style="color: #fff; padding: 20px;">Выберите модуль на карте</div>
</template>
