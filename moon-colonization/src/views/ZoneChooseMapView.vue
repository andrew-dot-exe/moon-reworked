<script setup lang="ts">
//import type { ZoneModel } from '@/components/colony/zones';
import Header from '@/components/ui/Header.vue';
import { ImagePoint, ImageResizer } from '@/components/utils/fitElements';
import { useComponentStore } from '@/stores/componentStore';
//import { useZoneStore } from '@/stores/zoneStore';
import { onMounted, ref } from 'vue';

//const zonesStore = useZoneStore()
const componentStore = useComponentStore()

// низина 1 - (1251, 1658)
// низина 2 - (1392, 1387)

// Высота 1 - (1048, 853)
// Высота 2 - (1181, 753)

// Равнина 1 - (1922, 353)
// Равнина 2 - (2110, 586)

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
const imgSrc = '/textures/zones/zone-bg-2048.jpeg'
const mapBackground = ref<HTMLElement | null>(null)
const resizer = new ImageResizer(0, 0);

//КОСТЫЛЬ
const originalPositions = [
  new ZonePosition("Низина 1", new ImagePoint(1251, 1658), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Низина 2", new ImagePoint(1392, 1387), `${markerPaths}/lowlands-default.svg`),

  new ZonePosition("Высота 1", new ImagePoint(1048, 853), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Высота 2", new ImagePoint(1181, 753), `${markerPaths}/lowlands-default.svg`),

  new ZonePosition("Равнина 1", new ImagePoint(1922, 353), `${markerPaths}/lowlands-default.svg`),
  new ZonePosition("Равнина 2", new ImagePoint(2110, 586), `${markerPaths}/lowlands-default.svg`),
]

onMounted(() => {
  window.addEventListener('resize', onResize)
  onResize()
});

const onResize = () => {
  if (mapBackground.value) {
    resizer.setImageX(mapBackground.value.clientWidth);
    resizer.setImageY(mapBackground.value.clientHeight);

  }
}

const handleZoneClick = async (zone: ZonePosition) => {
  console.log(zone.name);
  // map and colonization start
  // save to state
  componentStore.setComponent('login')
  //componentStore.setComponent('colonization')

}

</script>

<template>
  <Header/>
  <div class="map-container">
    <div class="map-aspect-ratio">
      <div class="map-content">
        <img class="map-image" :src="imgSrc" alt="Moon Map" ref="mapBackground" />
        
        <div 
          v-for="position in originalPositions" 
          :key="position.name" 
          class="zone-marker" 
          :style="{
            left: `${(position.point.x / 2880) * 100}%`,
            top: `${(position.point.y / 2048) * 100}%`,
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
/* Глобальные стили для отключения скролла */
html, body {
  overflow: hidden;
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

.map-container {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background: black;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  touch-action: none; /* Отключает зум на мобильных устройствах */
}

.map-aspect-ratio {
  width: 100%;
  height: 0;
  padding-bottom: 71.11%; /* 2048/2880 ≈ 0.7111 */
  position: relative;
  max-width: 100vw;
  max-height: 100vh;
}

.map-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.map-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  position: absolute;
  top: 0;
  left: 0;
  user-select: none; /* Запрещает выделение изображения */
  -webkit-user-drag: none; /* Запрещает перетаскивание в WebKit */
}

.zone-marker {
  position: absolute;
  display: flex;
  align-items: flex-start;
  gap: clamp(5px, 1vw, 10px);
  transform: translate(-50%, -50%);
  z-index: 1;
  cursor: pointer;
}

.marker-icon {
  width: clamp(60px, 10vw, 180px);
  height: clamp(60px, 10vw, 180px);
  position: relative;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  pointer-events: auto; /* Обеспечивает кликабельность */
}
.zone-name {
  color: white;
  font-family: 'Feature Mono', monospace;
  font-size: clamp(14px, 1.5vw, 20px);
  font-weight: 80;
  letter-spacing: 1px;
  text-shadow: 1px 1px 3px rgba(15, 15, 15, 1);
}
</style>
