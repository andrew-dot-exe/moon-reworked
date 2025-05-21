<template>
  <button @click="buildClick" :disabled="!isMapReady">test build</button>
</template>

<script setup lang="ts">
const emit = defineEmits(['close-opt', 'get-select']);
import { useMapStore } from '@/stores/mapStore';
import { useCellStore } from '@/stores/cellStore';
import { computed } from 'vue';
import { TypeModule } from '@/components/typeModules/typeModules';
import { typeModulesStore } from '@/stores/typeModulesStore';

const mapStore = useMapStore();
const cellStore = useCellStore();
const tm = typeModulesStore()


const isMapReady = computed(() => !!mapStore.mapRender);

async function buildClick() {
  await tm.getTypeModules();
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
    const manufactureModule = tm.typeModules[emit('get-select')]
    mapRender.placeManufactureModule(coords.x, coords.z, manufactureModule, 1, 1);
    emit("close-opt")
  } else {
    console.error('placeManufactureModule не найден или не выбрана ячейка', mapRender, coords);
  }
}
</script>
