
<script setup lang="ts">
import { nextTick, onMounted, ref, reactive, watch, onUnmounted } from 'vue'
import MapRenderer from '@/components/ui/MapRenderer.vue'
import UIHeader from '@/components/ui/UIHeader.vue'
import type { MoonCell } from '@/components/engine/map-renderer/map'
import { useMapStore } from '@/stores/mapStore'
import Construction from '@/components/ui/Construction.vue'
import Optimality from '@/components/ui/Optimality.vue'
import Success from '@/components/ui/Success.vue';
import EndColonyWindow from '@/components/ui/EndColonyWindow.vue';
import { useSelectedCellStore } from '@/stores/selectedCellStore'
import { Module } from '@/components/modules/modules'
import { modulesApi } from '@/components/modules/modulesApi'
import { userInfoStore } from '@/stores/userInfoStore';
import CellInfo from '@/components/ui/CellInfo.vue'

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

onMounted(async () => {
  await nextTick()
  mapStore.setRender(mapRendererRef)
  await uInfo.fetchUserInfo();
  if (!uInfo.userInfo.live) {
    end.value = true
  }
  setupWatchers(); 
})

// Функция для отслеживания изменений
const setupWatchers = () => {  
  // Добавляем вотчер для отслеживания изменений в resourceStore
  const unwatchLive = watch(
    () => uInfo.userInfo.live, // отслеживаем массив ресурсов
    (newLive, oldLive) => {
      // Здесь можно добавить любую логику, которая должна выполняться при изменении ресурсов
      if(newLive == false){
        end.value = true
      }
    },
  );
  
  onUnmounted(() => {
    unwatchLive();
  });
}

const selectedCell = ref(false)
const cellX = ref(-1)
const cellY = ref(-1)
function selectingCell(x: number, y: number){
  if(x < 0 || y < 0){
    selectedCell.value = false
    return
  }
  cellX.value = x
  cellY.value = y
  selectedCell.value = true
}
const selectedModule = ref(false)

const currentModule = ref<null | Module>(null)
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

<template>
  <div class="map-view">
    <MapRenderer ref="mapRendererRef" @cell-selected="selectingCell"/>
    <div class="ui-overlay">
      <div class="header">
        <UIHeader name="Режим строительства" @end_col="endColonisation" />
      </div>
      <div class="cellinfo-block">
        <div class="area-container">
          <svg xmlns="http://www.w3.org/2000/svg" width="11" height="16" viewBox="0 0 11 16" fill="none">
            <path d="M8.8 16H11V0H8.8L0 7.30709V8.72441L8.8 16Z" fill="#A3A3A3"/>
          </svg>
          <p>Области</p>
        </div>
        <svg class="Separator" xmlns="http://www.w3.org/2000/svg" width="2" height="45" viewBox="0 0 2 45" fill="none">
          <path d="M0 32V0H2V45H0Z" fill="#A3A3A3"/>
        </svg>
        <div class="question-container">
          <svg xmlns="http://www.w3.org/2000/svg" width="9" height="16" viewBox="0 0 9 16" fill="none">
            <path d="M4.63171 11.4853H3.88537L3.6 10.4485V7.44853H5.88293L7.68293 5.59559V3.67647L5.88293 1.82353H3.11707L1.31707 3.67647V5.44118H0V3.14706L2.56829 0.5H6.43171L9 3.14706V6.125L6.43171 8.77206H4.91707V10.4485L4.63171 11.4853ZM5.22439 15.5H3.33659V13.4706H5.22439V15.5Z" fill="#BCFE37"/>
          </svg>
        </div>
        <div class="cell">
          <CellInfo v-if="selectedCell" :x="cellX" :y="cellY"/>
        </div>
        
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


<style scoped>
.cell{
  display: flex;
}
.question-container{
  display: flex;
padding: 8px;
flex-direction: column;
justify-content: center;
align-items: center;
gap: 10px;
align-self: stretch;
background: rgba(0, 0, 0, 0.80);
}
.question-container svg{
  fill: #BCFE37;
  width: 9px;
height: 15px;
aspect-ratio: 3/5;
}
.Separator{
  fill: #A3A3A3;
  width: 2px;
  align-self: stretch;
  height: 45px;
}
p{
  margin: 0;
}
.area-container{
  display: flex;
  width: 141px;
  padding: 8px 12px;
  justify-content: center;
  align-items: center;
  gap: 12px;
  background: #464646;
}
.area-container svg{
  fill: #A3A3A3;
  width: 11px;
  height: 16px;
  flex-shrink: 0;
}
.area-container p{
  color: #A3A3A3;
  font-feature-settings: 'dlig' on;
  font-family: "Feature Mono";
  font-size: 20px;
  font-style: normal;
  font-weight: 600;
  line-height: normal;
  letter-spacing: 1px;
}
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
  pointer-events: none;
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
.cellinfo-block{
  pointer-events: auto;
  position: absolute;
  display: flex;
  top: 92px;
  left: 20px;
  display: inline-flex;
  align-items: flex-start;
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
