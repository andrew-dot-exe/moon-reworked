<template>
  <button @click="buildClick" :disabled="!isMapReady">test build</button>
</template>

<script setup lang="ts">
import { useMapStore } from '@/stores/mapStore';
import { BaseModule } from '../engine/map-renderer/baseModule';
import { useCellStore } from '@/stores/cellStore';
import { computed } from 'vue';

const mapStore = useMapStore()
const cellStore = useCellStore()

const isMapReady = computed(() => !!mapStore.mapRender)

function buildClick() {
  const mapRender = mapStore.mapRender
  if (mapRender && typeof mapRender.placeMeshOnCell === 'function') {
    const sampleMesh = new BaseModule()
    mapRender.placeMeshOnCell(
      sampleMesh.getMesh(),
      cellStore.selectedCellCoords.x,
      cellStore.selectedCellCoords.z
    )
  } else {
    console.error('placeMeshOnCell не найден', mapRender)
  }
}
</script>
