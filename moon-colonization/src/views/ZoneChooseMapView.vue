<script setup lang="ts">
import Header from '@/components/ui/Header.vue';
import { ImagePoint } from '@/components/utils/fitElements';
import { useComponentStore } from '@/stores/componentStore';
import { onMounted, ref, watch } from 'vue';

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
  new ZonePosition("Низина 1", new ImagePoint(1479, 1449), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Низина 2", new ImagePoint(1467, 1532), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Высота 1", new ImagePoint(1477, 1285), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Высота 2", new ImagePoint(1408, 1322), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Равнина 1", new ImagePoint(1646, 1226), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Равнина 2", new ImagePoint(1699, 1264), `${markerPaths}/lowlands-default.svg`),
]

const componentStore = useComponentStore()

onMounted(() => {
  centerMap()
  window.addEventListener('resize', centerMap)
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

const handleZoneClick = async (zone: ZonePosition) => {
  console.log(zone.name)
  componentStore.setComponent('colonization')
}

watch([scale, translatePos], () => {
  checkBoundaries()
})
</script>

<template>
  <div class="main-container">
    <Header class="header" />
    
    <div 
      class="map-container"
      ref="mapContainer"
      @mousedown="handleMouseDown"
      @mousemove="handleMouseMove"
      @mouseup="handleMouseUp"
      @mouseleave="handleMouseUp"
      @wheel="handleWheel"
    >
      <div 
        class="map-transform"
        :style="{
          transform: `translate(${translatePos.x}px, ${translatePos.y}px) scale(${scale})`,
          cursor: isDragging ? 'grabbing' : 'grab'
        }"
      >
        <img 
          ref="mapImage"
          class="map-image" 
          :src="imgSrc" 
          alt="Moon Map" 
          draggable="false"
        />
        
        <div
          v-for="position in originalPositions"
          :key="position.name"
          class="zone-marker"
          :style="{
            left: `${(position.point.x / imgWidth) * 100}%`,
            top: `${(position.point.y / imgHeight) * 100}%`,
          }"
          @click="handleZoneClick(position)"
        >
          <div class="marker-icon" :style="{ backgroundImage: `url(${position.icon})` }"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  flex-shrink: 0;
  z-index: 10;
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
  transform: translate(-50%, -50%);
  z-index: 1;
  cursor: pointer;
  pointer-events: auto;
}

.marker-icon {
  width: clamp(60px, 10vw, 180px);
  height: clamp(60px, 10vw, 180px);
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  filter: drop-shadow(0 0 8px rgba(0, 0, 0, 0.7));
}
</style>