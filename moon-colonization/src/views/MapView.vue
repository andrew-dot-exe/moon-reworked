<template>
  <div class="map-view">
    <MapRenderer ref="mapRendererRef" @cell-selected="onCellSelected" />

    <!-- UI оверлей -->
    <div class="ui-overlay">
      <div style="pointer-events: auto;">
        <UIHeader />
      </div>
      <div style="pointer-events: auto;">
        <BuildButton />
      </div>

      <div class="construction-block">
        <Construction/>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from 'vue'
import MapRenderer from '@/components/ui/MapRenderer.vue'
import UIHeader from '@/components/ui/UIHeader.vue'
import BuildButton from '@/components/ui/BuildButton.vue'
import type { MoonCell } from '@/components/engine/map-renderer/map'
import { useMapStore } from '@/stores/mapStore'
import Construction from '@/components/ui/Construction.vue'

const mapRendererRef = ref(null)
const mapStore = useMapStore()

onMounted(async () => {
  await nextTick()
  console.log('mapRendererRef after nextTick:', mapRendererRef.value)
  mapStore.setRender(mapRendererRef)
})

// Обработчик выбора клетки
function onCellSelected(x: number, z: number, cellData: MoonCell) {
  console.log(`${x},${z}, on canvas, ${cellData.x};${cellData.y} on cellData`)
}


</script>

<style scoped>
.construction-block{
  pointer-events: auto;
  display: flex;
  position: absolute;
  margin-left: 20px;
  margin-right: 20px;
  bottom: 20px;

}

.map-view {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.ui-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  /* клики проходят сквозь overlay */
  z-index: 2;
}

.info-panel {
  pointer-events: auto;
  /* Восстанавливает возможность взаимодействовать с элементами панели */
  background-color: rgba(0, 0, 0, 0.7);
  color: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.top-panel {
  position: absolute;
  top: 20px;
  left: 20px;
  right: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
}

.top-panel h2 {
  margin: 0;
  font-size: 1.4em;
}

.cell-info {
  position: absolute;
  bottom: 20px;
  left: 20px;
  width: 280px;
}

.cell-info h3 {
  margin-top: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  padding-bottom: 8px;
}

.cell-details {
  margin: 10px 0;
}

.cell-details p {
  margin: 5px 0;
}

.cell-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.map-controls {
  position: absolute;
  bottom: 20px;
  right: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  pointer-events: auto;
}

button {
  background-color: #3a5f8a;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #4a70a1;
}

button:disabled {
  background-color: #2a3f5a;
  color: #aaaaaa;
  cursor: not-allowed;
}

.minimap {
  position: absolute;
  bottom: 20px;
  right: 20px;
  width: 150px;
  height: 150px;
  background-color: rgba(0, 0, 0, 0.7);
  border-radius: 8px;
  display: none;
  /* Пока скрываем, реализация будет позже */
}

@media (max-width: 768px) {
  .top-panel {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .cell-info {
    width: calc(100% - 40px);
  }
}
</style>
