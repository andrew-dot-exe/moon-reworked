<template>
  <div class="map-view">
    <MapRenderer ref="mapRendererRef" @cell-selected="onCellSelected" />
    <div class="ui-overlay">
      <div class="header">
        <UIHeader name="Режим строительства" @end_col="endColonisation" />
      </div>
      <div class="success-block">
        <Success />
      </div>
      <div v-if="selectedCell && selectedModule" class="optimality-block">
        <Optimality :opt="optimalityResult.opt" :rel="optimalityResult.rel" :rat="optimalityResult.rat" />
      </div>
      <div class="construction-block">
        <Construction @select-module="onModuleSelect" />
      </div>
    </div>
    <div v-if="end" class="stat-overlay">
      <EndColonyWindow />
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref, reactive } from 'vue'
import MapRenderer from '@/components/ui/MapRenderer.vue'
import UIHeader from '@/components/ui/UIHeader.vue'
import type { MoonCell } from '@/components/engine/map-renderer/map'
import { useMapStore } from '@/stores/mapStore'
import Construction from '@/components/ui/Construction.vue'
import Optimality from '@/components/ui/Optimality.vue'
import Success from '@/components/ui/Success.vue';
import EndColonyWindow from '@/components/ui/EndColonyWindow.vue';
import { useSelectedCellStore } from '@/stores/selectedCellStore'
import { useModuleInfoStore } from '@/stores/moduleInfoStore'
import { Module } from '@/components/modules/modules'
import { modulesApi } from '@/components/modules/modulesApi'
import { userInfoStore } from '@/stores/userInfoStore';

const end = ref(false)

const uInfo = userInfoStore();

const mapRendererRef = ref(null)
const mapStore = useMapStore()

const endColonisation = async () => {
  end.value = true
}

const optimalityResult = reactive({
  opt: 0,
  rel: 0,
  rat: 0
});
// ячейку выбрал, надо будет сделать запрос
// потом надо сделать выбор из

onMounted(async () => {
  await nextTick()
  console.log('mapRendererRef after nextTick:', mapRendererRef.value)
  mapStore.setRender(mapRendererRef)
  await uInfo.fetchUserInfo();
  if (!uInfo.userInfo.live) {
    end.value = true
  }
})

const selectedCell = ref(false)
const selectedModule = ref(false)

// Обработчик выбора клетки
async function onCellSelected(x: number, z: number) {
  selectedCell.value = true
  if (selectedModule.value && currentModule.value != undefined
    && currentModule.value?.x != undefined && currentModule.value?.y != undefined) {
    currentModule.value.x = x
    currentModule.value.y = z
    await onModuleSelect(undefined)
  }
}

function offModuleSelect() {
  //?
  selectedModule.value = false
}

const currentModule = ref<null | Module>(null)

function getSelect(): number {
  return Number(currentModule.value?.typeModule)
}

async function onModuleSelect(module: Module | undefined) {

  // Вызов проверки эффективности
  if (selectedCell.value) {
    selectedModule.value = true
    Object.assign(optimalityResult, { opt: 0, rel: 0, rat: 0 });
    if (module !== undefined) {
      currentModule.value = module
    }
    // Проверка и вывод всех полей объекта moduleToCheck
    /*Object.entries(module).forEach(([key, value]) => {
      console.log(`moduleToCheck.${key}:`, value)
    })
    console.log('Module для API:', module)*/
    try {
      if (currentModule.value != undefined) {
        const result = await modulesApi.checkModule(currentModule.value)
        //console.log('Эффективность модуля (ответ API):', result)
        if (result && result.rationality && result.relief) {
          // Принудительное обновление через новое присваивание
          optimalityResult.rat = result.rationality ?? 0;
          optimalityResult.rel = result.relief ?? 0;
          optimalityResult.opt = (optimalityResult.rat * 0.7 + optimalityResult.rel * 0.3) || 0;
        }
      }
      //emit('optimality', result)
    } catch (e) {
      console.error('Ошибка при вычислении эффективности:', e)
      //emit('optimality', null)
    }
  }
}


</script>

<style scoped>
.stat-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.header {
  pointer-events: auto;
  flex-shrink: 0;
  z-index: 10;
  overflow: auto;
  overflow-x: auto;
  /* Включаем прокрутку */
  scrollbar-width: none;
  /* Firefox */
}

.construction-block {
  pointer-events: auto;
  display: flex;
  position: absolute;
  margin: 20px;
  bottom: 0;
}

.optimality-block {
  pointer-events: auto;
  display: flex;
  position: absolute;
  margin: 20px;
  bottom: 230px;
}

.success-block {
  pointer-events: auto;
  position: absolute;
  display: flex;
  top: 92px;
  right: 20px;

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
