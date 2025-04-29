<template>
  <div class="map-view">
    <div class="map-container">
      <IsometricMap ref="mapRef" :scale="scale" />
    </div>
    
    <div class="zoom-controls">
      <button @click="zoomIn">+</button>
      <button @click="zoomOut">-</button>
      <button @click="resetZoom">Reset</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import IsometricMap from '@/components/ui/IsometricMap.vue';

export default defineComponent({
  name: 'MapView',
  components: {
    IsometricMap
  },
  setup() {
    const mapRef = ref(null);
    const scale = ref(1.0);
    const zoomStep = 0.1;
    const minZoom = 0.5;
    const maxZoom = 2.0;

    const zoomIn = () => {
      if (scale.value < maxZoom) {
        scale.value = Math.min(maxZoom, scale.value + zoomStep);
      }
    };
    
    const zoomOut = () => {
      if (scale.value > minZoom) {
        scale.value = Math.max(minZoom, scale.value - zoomStep);
      }
    };
    
    const resetZoom = () => {
      scale.value = 1.0;
    };

    return {
      mapRef,
      scale,
      zoomIn,
      zoomOut,
      resetZoom
    };
  }
});
</script>

<style scoped>
.map-view {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

.map-container {
  flex: 1;
  overflow: hidden;
  width: 100%;
  height: 100%;
}

.zoom-controls {
  position: absolute;
  bottom: 20px;
  right: 20px;
  display: flex;
  flex-direction: column;
  gap: 5px;
  z-index: 1000;
}

.zoom-controls button {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  background-color: rgba(255, 255, 255, 0.8);
  border: 1px solid #ccc;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.zoom-controls button:hover {
  background-color: rgba(255, 255, 255, 1);
}

h1 {
  text-align: center;
  margin: 1rem 0;
}

.map-controls {
  padding: 1rem;
  margin: 0 1rem 1rem;
  background-color: #f8f8f8;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.map-info {
  max-width: 600px;
  margin: 0 auto;
}

h3 {
  margin-top: 0;
}
</style>