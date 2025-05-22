
<script setup lang="ts">
import { useCellStore } from '@/stores/cellStore';
import { useZoneStore } from '@/stores/zoneStore';
import { onMounted, ref, computed } from 'vue'
const props = defineProps({
  x: {
    type: Number,
    required: true
  },
  y: { type: Number, required: true }
});
const cellStore = useCellStore()
const zoneStore = useZoneStore()
const idZone = ref(zoneStore.current_zone?.id) //

onMounted(async () => {
    await cellStore.getCells(idZone.value)
})

// Безопасные computed свойства
const currentCell = computed(() => {
    if (idZone.value !== undefined) return cellStore.cells[idZone.value][props.y][props.x]
    return null
})

const angleC = computed(() => currentCell.value?.angle ?? 'N/A')
const heightCor = computed(() => {
  if (currentCell.value == null) return 'N/A'
  const grad = currentCell.value.widthSecond
  return `${grad%60}°${Math.floor(grad/60)%60}'${Math.floor((grad/3600)%60)}"в.д.`
})
const widgthCor = computed(() => {
  if (currentCell.value == null) return 'N/A'
  const grad = currentCell.value.longitudeSecond
  return `${grad%60}°${Math.floor(grad/60)%60}'${Math.floor((grad/3600)%60)}"ю.ш.`
})
const heightC = computed(() => currentCell.value?.height ?? 'N/A')
</script>

<template>
    <div class="info-container">
        <p>{{ angleC }}
        </p>
        <p> {{ widgthCor }}
        </p>
        <p> {{ heightCor }}
        </p>
        <p> {{ heightC }}
        </p>
    </div>
</template>

<style scoped>

.info-container{
  display: flex;
padding: 8px;
justify-content: center;
align-items: center;
gap: 8px;
align-self: stretch;
background: rgba(0, 0, 0, 0.80);
}
.info-container p{
    color: #A3A3A3;
font-feature-settings: 'dlig' on;
font-family: "Feature Mono";
font-size: 16px;
font-style: normal;
font-weight: 600;
line-height: normal;
letter-spacing: 0.6px;
}
</style>