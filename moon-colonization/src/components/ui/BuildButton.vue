<template>
  <button @click="buildClick" :disabled="!isMapReady">test build</button>
</template>

<script setup lang="ts">
const emit = defineEmits(['close-opt']);
import { useMapStore } from '@/stores/mapStore';
import { useCellStore } from '@/stores/cellStore';
import { computed } from 'vue';
import { TypeModule } from '@/components/typeModules/typeModules';

const mapStore = useMapStore();
const cellStore = useCellStore();

const isMapReady = computed(() => !!mapStore.mapRender);

function buildClick() {
  const mapRender = mapStore.mapRender;
  const coords = cellStore.selectedCellCoords;
  if (
    mapRender &&
    typeof mapRender.placeManufactureModule === 'function' &&
    coords &&
    coords.x != null &&
    coords.z != null
  ) {
    // Пример объекта TypeModule для manufacture
    const manufactureModule = new TypeModule(1, 'manufacture', 10, 1000, false);
    mapRender.placeManufactureModule(coords.x, coords.z, manufactureModule, 1, 1);
    emit("close-opt")
  } else {
    console.error('placeManufactureModule не найден или не выбрана ячейка', mapRender, coords);
  }
}
</script>
