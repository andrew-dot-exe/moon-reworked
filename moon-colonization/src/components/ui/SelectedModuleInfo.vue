<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useModuleInfoStore } from '@/stores/moduleInfoStore'
import ModuleComponent from '@/components/ui/ModuleComponent.vue'

const moduleInfoStore = useModuleInfoStore()
const { selectedModule } = storeToRefs(moduleInfoStore)
const emit = defineEmits(['optimality'])

function logAndEmitOptimality(event: any) {
  console.log('[SelectedModuleInfo] optimality event:', event)
  emit('optimality', event)
}
</script>

<template>
  <div v-if="selectedModule">
    <ModuleComponent :data="selectedModule" @optimality="logAndEmitOptimality" />
  </div>
  <div v-else style="color: #fff; padding: 20px;">Выберите модуль на карте</div>
</template>
