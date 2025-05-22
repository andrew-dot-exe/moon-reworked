<script setup lang="ts">
import UIHeader from '@/components/ui/UIHeader.vue';
import { ImagePoint } from '@/components/utils/fitElements';
import { useComponentStore } from '@/stores/componentStore';
import { onMounted, ref, watch } from 'vue';
import ConstructionLink from '@/components/ui/ConstructionLink.vue'
import Success from '@/components/ui/Success.vue';
import EndColonyWindow from '@/components/ui/EndColonyWindow.vue';
import { useZoneStore } from '@/stores/zoneStore';
import { linkApi } from '@/components/link/LinkApi';
import { Link, PrimaryKey } from '@/components/link/Link';
import { userInfoStore } from '@/stores/userInfoStore';
import { colonyApi } from '@/components/colony/colony';
import { useLinkStore } from '@/stores/useLinkStore';
const end = ref(false)



interface PositionByName {
  name: string,
  point: ImagePoint,
  icon: string
}

class ZonePosition implements PositionByName {
  name: string;
  point: ImagePoint;
  icon: string;

  constructor(name: string, point: ImagePoint, icon: string) {
    this.name = name;
    this.point = point;
    this.icon = icon;
  }
}

const markerPaths = '/textures/zones/markers'
const imgSrc = '/textures/zones/zone-bg.jpg'
const mapContainer = ref<HTMLElement | null>(null)
const mapImage = ref<HTMLImageElement | null>(null)
const isDragging = ref(false)
const startPos = ref({ x: 0, y: 0 })
const translatePos = ref({ x: 0, y: 0 })
const scale = ref(1)
const imgWidth = 2696
const imgHeight = 2360

const originalPositions = [
  new ZonePosition("Равнина 1", new ImagePoint(1646, 1226), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Равнина 2", new ImagePoint(1699, 1264), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Высота 1", new ImagePoint(1477, 1285), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Высота 2", new ImagePoint(1408, 1322), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Низина 1", new ImagePoint(1479, 1449), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Низина 2", new ImagePoint(1467, 1532), `${markerPaths}/lowlands-default.svg`),
]

const componentStore = useComponentStore()
const zoneStore = useZoneStore()
const uInfo = userInfoStore();
const linkStore = useLinkStore()

onMounted(async () => {
  centerMap()
  zoneStore.fetchAllZones()
  window.addEventListener('resize', centerMap)
  await uInfo.fetchUserInfo();
  await linkStore.getLink()
  if (linkStore.links) {
    redrawLines();
  }
  if(!uInfo.userInfo.live){
    end.value = true
  }
})

const checkBoundaries = () => {
  if (!mapContainer.value) return;

  const containerWidth = mapContainer.value.clientWidth;
  const containerHeight = mapContainer.value.clientHeight;

  // Рассчитываем минимальные и максимальные допустимые позиции
  const minX = containerWidth - imgWidth * scale.value;
  const minY = containerHeight - imgHeight * scale.value;

  // Ограничиваем позицию по X
  translatePos.value.x = Math.max(
    minX,
    Math.min(0, translatePos.value.x)
  );

  // Ограничиваем позицию по Y
  translatePos.value.y = Math.max(
    minY,
    Math.min(0, translatePos.value.y)
  );

  // Если карта меньше контейнера, центрируем ее
  if (imgWidth * scale.value < containerWidth) {
    translatePos.value.x = (containerWidth - imgWidth * scale.value) / 2;
  }

  if (imgHeight * scale.value < containerHeight) {
    translatePos.value.y = (containerHeight - imgHeight * scale.value) / 2;
  }
}

const centerMap = () => {
  if (mapContainer.value) {
    const containerWidth = mapContainer.value.clientWidth
    const containerHeight = mapContainer.value.clientHeight

    // Масштаб для полного отображения по ширине или высоте
    const scaleWidth = containerWidth / imgWidth * 5
    const scaleHeight = containerHeight / imgHeight * 5

    scale.value = Math.min(scaleWidth, scaleHeight)

    // Устанавливаем начальные координаты для центрирования карты
    translatePos.value = {
      x: (containerWidth - imgWidth * scale.value) / 2 - 300,
      y: (containerHeight - imgHeight * scale.value) / 2 - 230
    }

    checkBoundaries()
  }
}

const handleMouseDown = (e: MouseEvent) => {
  isDragging.value = true
  startPos.value = {
    x: e.clientX - translatePos.value.x,
    y: e.clientY - translatePos.value.y
  }
  document.body.style.cursor = 'grabbing'
}

const handleMouseMove = (e: MouseEvent) => {
  if (!isDragging.value) return

  translatePos.value = {
    x: e.clientX - startPos.value.x,
    y: e.clientY - startPos.value.y
  }

  checkBoundaries()
}

const handleMouseUp = () => {
  isDragging.value = false
  document.body.style.cursor = ''
}

const handleWheel = (e: WheelEvent) => {
  e.preventDefault()

  const rect = mapContainer.value?.getBoundingClientRect()
  if (!rect) return

  const mouseX = e.clientX - rect.left
  const mouseY = e.clientY - rect.top

  const mapX = (mouseX - translatePos.value.x) / scale.value
  const mapY = (mouseY - translatePos.value.y) / scale.value

  const delta = -e.deltaY
  const zoomFactor = 0.08
  const newScale = delta > 0
    ? scale.value * (1 + zoomFactor)
    : scale.value / (1 + zoomFactor)

  // Ограничиваем масштаб
  scale.value = Math.min(Math.max(1.2, newScale), 4)

  // Корректируем позицию для zoom относительно курсора
  translatePos.value = {
    x: mouseX - mapX * scale.value,
    y: mouseY - mapY * scale.value
  }

  checkBoundaries()
}

const handleZoneClick = async (zone: ZonePosition, i: number) => {
  if(open.value){selectZone(i)}
  else{
  zoneStore.selectZoneByName(zone.name)
  componentStore.setComponent('colonization')
  }
}

watch([scale, translatePos], () => {
  checkBoundaries()
  //redrawLines()
})

const redrawLines = () => {
  if (!canvas.value || !linkStore.links){
    return;
  }
  
  const ctx = canvas.value.getContext('2d');
  if (!ctx){
    return;
  }
  
  // Очищаем canvas
  ctx.clearRect(0, 0, imgWidth, imgHeight);
  
  // Перерисовываем все линии
  if(linkStore.links != undefined){
    for(let i = 0; i < linkStore.links.length; i++){

      const t1 = originalPositions[linkStore.links[i].idZone1].point
      const t2 = originalPositions[linkStore.links[i].idZone2].point
      //console.log(`Drawing line from zone ${linkStore.links[i].id_zone1} (${t1.x},${t1.y}) to zone ${linkStore.links[i].id_zone2} (${t2.x},${t2.y})`);
      drawLine(t1.x, t1.y, t2.x, t2.y, linkStore.links[i].type)
    }
  }
};

const open = ref(true)
const opening = () =>{
  open.value = !open.value
}

const selectedLink = ref(0)
const selectLink = (type: number) => {
  console.log("link ", type)
  if(selectedLink.value == type) selectedLink.value = 0
  else selectedLink.value = type
}

const selectedZones = ref({zone1: -1, zone2: -1})
const selectZone = (zone: number) => {
  if(selectedZones.value !== undefined){
    if(selectedZones.value.zone1 < 0) {
      selectedZones.value.zone1 = zone
    }
    else if(selectedZones.value.zone2 < 0) {
      selectedZones.value.zone2 = zone
    }
    else{
      selectedZones.value.zone1 = -1
      selectedZones.value.zone2 = -1
    }
  }
}

const buildLink = async () => {
  if(selectedLink.value > 0 && selectedZones.value.zone1 > -1 && selectedZones.value.zone2 > -1 && selectedZones.value.zone2 != selectedZones.value.zone1){
    const response = await linkApi.createLink(new Link(new PrimaryKey(selectedLink.value - 1, selectedZones.value.zone1,selectedZones.value.zone2) ))
    console.log(response)
  }
}
const endColonisation =  async () => {
  end.value = true
}

const canvas = ref<HTMLCanvasElement | null>(null);

function drawLine(x1: number, y1: number, x2: number, y2: number, type: number) {
  if (!canvas.value) {
    return;
  }
  
  const ctx = canvas.value.getContext('2d');
  if (!ctx) {
    return;
  }
  
  ctx.beginPath();
  ctx.moveTo(x1, y1 + type * 2);
  ctx.lineTo(x2, y2 + type * 2);
  ctx.strokeStyle = type === 0 ? "#F3FF73B2" : "#ff4f00"; // Используем strokeStyle вместо color
  ctx.lineWidth = 2;
  ctx.stroke();
}
</script>

<template>
  <div class="main-container">
    <div class="header">
      <UIHeader name="Режим выбор области" @end_col="endColonisation" />
    </div>

    <div class="map-container" ref="mapContainer" @mousedown="handleMouseDown" @mousemove="handleMouseMove"
      @mouseup="handleMouseUp" @mouseleave="handleMouseUp" @wheel="handleWheel">
      <div class="map-transform" :style="{
        transform: `translate(${translatePos.x}px, ${translatePos.y}px) scale(${scale})`,
        cursor: isDragging ? 'grabbing' : 'grab'
      }">
        <img ref="mapImage" class="map-image" :src="imgSrc" alt="Moon Map" draggable="false" />
        <canvas ref="canvas" class="map-canvas" :width="imgWidth" :height="imgHeight"></canvas>

        <div v-for="(position, index) in originalPositions" :key="position.name" class="zone-marker" :style="{
          left: `${(position.point.x / imgWidth) * 100}%`,
          top: `${(position.point.y / imgHeight) * 100}%`,
        }" @click="handleZoneClick(position, index)" :class="{select: index == selectedZones.zone1 || index == selectedZones.zone2 }">
          <div class="marker-icon" :style="{ backgroundImage: `url(${position.icon})` }" >
            <!-- <svg>
              <use xlink:h></use>
            </svg> -->
          </div>
        </div>
      </div>
    </div>
    <div class="success-block">
      <Success />
    </div>
    <div class="construction-block">
      <ConstructionLink @open-fun="opening" @select-link1="selectLink(1)" @select-link2="selectLink(2)" @build="buildLink"/>
    </div>
    <div v-if="end" class="stat-overlay">
      <EndColonyWindow />
    </div>
  </div>
</template>

<style scoped>
.map-canvas {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 2;
  pointer-events: none; /* Чтобы события мыши проходили сквозь canvas */
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

.construction-block {
  pointer-events: auto;
  display: flex;
  position: absolute;
  margin-left: 20px;
  margin-right: 20px;
  bottom: 20px;

}

.success-block {
  pointer-events: auto;
  position: absolute;
  display: flex;
  top: 92px;
  right: 20px;

}

.main-container {
  max-width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  scrollbar-width: none;
  /* Firefox */
  -ms-overflow-style: none;
  /* IE и Edge */
}

.header {
  flex-shrink: 0;
  z-index: 10;
  overflow: auto;
  overflow-x: auto;
  /* Включаем прокрутку */
  scrollbar-width: none;
  /* Firefox */
}

.map-container {
  flex: 1;
  position: relative;
  overflow: hidden;
  background-color: #000;
  touch-action: none;
}

.map-transform {
  position: absolute;
  transform-origin: 0 0;
  will-change: transform;
}

.map-image {
  width: 2696px;
  height: 2360px;
  user-select: none;
  pointer-events: none;
}

.zone-marker {
  position: absolute;
  z-index: 1;
  cursor: pointer;
  pointer-events: auto;
  width: 0;
  height: 0;
}


.marker-icon {
  width: 60px;
  height: 60px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  /*filter: drop-shadow(0 0 8px rgba(0, 0, 0, 0.7));*/
  transform: translate(-10px, -40px);
  transition: transform 0.2s ease;
}
.select div{
  /* filter: drop-shadow(0 0 8px #BCFE37); */
  filter:brightness(2);
}
</style>
