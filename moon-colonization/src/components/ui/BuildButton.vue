<template>
  <button @click="buildClick" :disabled="!isMapReady">test build</button>
</template>

<script setup lang="ts">
import { useMapStore } from '@/stores/mapStore';
import { useCellStore } from '@/stores/cellStore';
import { computed } from 'vue';
import { ModuleMesh } from '../engine/map-renderer/baseModule';

const mapStore = useMapStore()
const cellStore = useCellStore()

const isMapReady = computed(() => !!mapStore.mapRender)

function buildClick() {
  const mapRender = mapStore.mapRender
  if (mapRender && typeof mapRender.placeMeshOnCell === 'function') {
    const sampleModule = new ModuleMesh('test')
    mapRender.placeMeshOnCell(
      sampleModule.getMesh(),
      cellStore.selectedCellCoords.x,
      cellStore.selectedCellCoords.z
    )
  } else {
    console.error('placeMeshOnCell не найден', mapRender)
  }
}
</script>
