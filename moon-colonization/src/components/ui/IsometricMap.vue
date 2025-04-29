<template>
  <div class="isometric-map-container" ref="mapContainer" @mousemove="handleMouseMove" @click="handleClick" @wheel="handleWheel">
    <!-- Main map container with transformation -->
    <div 
      class="isometric-map" 
      :style="{ transform: `translate(${offsetX}px, ${offsetY}px) scale(${scale})` }"
    >
      <!-- Render each map cell -->
      <div 
        v-for="cell in allCells" 
        :key="cell.id" 
        class="map-cell" 
        :class="{ 'cell-selected': cell.isSelected }"
        :style="getCellStyle(cell)"
        @click.stop="selectCell(cell.x, cell.y)"
      >
        <div class="cell-content">
          {{ cell.x }},{{ cell.y }}
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watchEffect } from 'vue';
import type { PropType } from 'vue';
import { IsometricMap, createDefaultMap } from '@/engine/map';

export default defineComponent({
  name: 'IsometricMapComponent',
  props: {
    // Scale prop allows parent component to control zoom level
    scale: {
      type: Number,
      default: 1.0
    }
  },
  setup(props) {
    // DOM reference to the container element
    const mapContainer = ref<HTMLElement | null>(null);
    
    // Initialize the isometric map with default settings
    const map = ref<IsometricMap>(createDefaultMap());
    
    // Position offsets for panning the map
    const offsetX = ref(300); 
    const offsetY = ref(100);
    
    // State for handling drag operations
    const isDragging = ref(false);
    const lastMouseX = ref(0);
    const lastMouseY = ref(0);
    
    // Zoom control constants
    const zoomStep = 0.1;
    const minZoom = 0.5;
    const maxZoom = 2.0;
    
    // Internal scale to handle both prop-based and wheel-based zooming
    const internalScale = ref(props.scale);
    
    // Sync internal scale with prop updates
    watchEffect(() => {
      internalScale.value = props.scale;
    });

    // Computed property that returns cells sorted for correct rendering order
    // Cells need to be drawn from back-to-front for proper isometric perspective
    const allCells = computed(() => {
      return map.value.getAllCells().sort((a, b) => {
        // Sort by Manhattan distance (x+y) first, then by y for correct overlap
        if (a.x + a.y !== b.x + b.y) {
          return (a.x + a.y) - (b.x + b.y);
        }
        return a.y - b.y;
      });
    });

    // Generate CSS style object for each cell based on its isometric position
    const getCellStyle = (cell: any) => {
      const coords = cell.getIsoCoordinates(map.value.cellWidth, map.value.cellHeight);
      return {
        left: `${coords.x}px`,
        top: `${coords.y}px`,
        width: `${map.value.cellWidth}px`,
        height: `${map.value.cellHeight}px`,
        backgroundColor: getColorByHeight(cell.height),
        zIndex: cell.x + cell.y // Higher z-index for cells further from origin
      };
    };

    // Generate different colors based on cell height for terrain visualization
    const getColorByHeight = (height: number) => {
      // Different colors for different heights
      const colors = ['#8FB982', '#7FA972', '#698B61', '#567D52'];
      return colors[height % colors.length];
    };

    // Handle mouse movement for map panning
    const handleMouseMove = (event: MouseEvent) => {
      if (isDragging.value) {
        const deltaX = event.clientX - lastMouseX.value;
        const deltaY = event.clientY - lastMouseY.value;
        
        offsetX.value += deltaX;
        offsetY.value += deltaY;
        
        lastMouseX.value = event.clientX;
        lastMouseY.value = event.clientY;
      }
    };

    // Start dragging operation when mouse button is pressed
    const handleMouseDown = (event: MouseEvent) => {
      isDragging.value = true;
      lastMouseX.value = event.clientX;
      lastMouseY.value = event.clientY;
    };

    // End dragging operation when mouse button is released
    const handleMouseUp = () => {
      isDragging.value = false;
    };

    // Handle click on map to select a cell
    const handleClick = (event: MouseEvent) => {
      if (mapContainer.value) {
        const rect = mapContainer.value.getBoundingClientRect();
        const mouseX = event.clientX - rect.left;
        const mouseY = event.clientY - rect.top;
        
        // Account for scale when converting screen coordinates to grid
        const scaledX = (mouseX - offsetX.value) / internalScale.value;
        const scaledY = (mouseY - offsetY.value) / internalScale.value;
        
        // Convert screen coordinates to grid coordinates
        const gridCoords = map.value.screenToGrid(
          scaledX + offsetX.value, 
          scaledY + offsetY.value, 
          offsetX.value, 
          offsetY.value
        );
        
        if (gridCoords) {
          selectCell(gridCoords.x, gridCoords.y);
        }
      }
    };
    
    // Handle mouse wheel for zooming in/out
    const handleWheel = (event: WheelEvent) => {
      event.preventDefault();
      
      // Get mouse position relative to container
      if (!mapContainer.value) return;
      
      const rect = mapContainer.value.getBoundingClientRect();
      const mouseX = event.clientX - rect.left;
      const mouseY = event.clientY - rect.top;
      
      // Calculate position relative to the current transform
      const relativeX = (mouseX - offsetX.value) / internalScale.value;
      const relativeY = (mouseY - offsetY.value) / internalScale.value;
      
      // Determine zoom in or out
      const delta = event.deltaY > 0 ? -zoomStep : zoomStep;
      const newScale = Math.max(minZoom, Math.min(maxZoom, internalScale.value + delta));
      
      // Only proceed if scale actually changed
      if (newScale !== internalScale.value) {
        // Calculate new offsets to zoom toward cursor position
        offsetX.value = mouseX - relativeX * newScale;
        offsetY.value = mouseY - relativeY * newScale;
        
        internalScale.value = newScale;
        
        // Emit update to parent component
        if (props.scale !== internalScale.value) {
          emitScaleChange(newScale);
        }
      }
    };
    
    // Method to emit scale changes to parent component
    const emitScaleChange = (newScale: number) => {
      // Implement if needed with defineEmits
    };

    // Select a cell at the given coordinates
    const selectCell = (x: number, y: number) => {
      map.value.selectCell(x, y);
      const cell = map.value.getCell(x, y);
      if (cell) {
        console.log(`Selected cell at (${x}, ${y}), height: ${cell.height}`);
      }
    };

    // Initialize map and event handlers when component is mounted
    onMounted(() => {
      if (mapContainer.value) {
        // Add global event listeners for mouse drag
        window.addEventListener('mousedown', handleMouseDown);
        window.addEventListener('mouseup', handleMouseUp);
        
        // Center the map initially
        const containerWidth = mapContainer.value.clientWidth;
        const containerHeight = mapContainer.value.clientHeight;
        
        // Calculate center position based on map dimensions and cell size
        const mapWidthPx = map.value.width * map.value.cellWidth / 2;
        const mapHeightPx = map.value.height * map.value.cellHeight / 2;
        
        offsetX.value = (containerWidth / 2) - mapWidthPx / 2;
        offsetY.value = (containerHeight / 2) - mapHeightPx / 2;
      }
    });

    return {
      mapContainer,
      map,
      allCells,
      offsetX,
      offsetY,
      getCellStyle,
      handleMouseMove,
      handleClick,
      handleWheel,
      selectCell,
      scale: computed(() => internalScale.value)
    };
  }
});
</script>

<style scoped>
/* Container that holds the entire map view */
.isometric-map-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
  background-color: #f0f0f0;
  user-select: none;
}

/* Inner map container that can be transformed (scaled and translated) */
.isometric-map {
  position: absolute;
  transform-origin: center;
  transition: transform 0.1s ease;
}

/* Individual map cell with isometric perspective */
.map-cell {
  position: absolute;
  transform: rotateX(60deg) rotateZ(45deg);
  transform-style: preserve-3d;
  border: 1px solid rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: filter 0.2s ease;
}

/* Hover effect for cells */
.map-cell:hover {
  filter: brightness(1.2);
  z-index: 1000 !important;
}

/* Style for selected cells */
.cell-selected {
  border: 2px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
  filter: brightness(1.3);
  z-index: 1000 !important;
}

/* Content inside each cell (coordinates) */
.cell-content {
  color: rgba(0, 0, 0, 0.5);
  font-size: 0.7rem;
  transform: rotateX(-60deg) rotateZ(-45deg);
  pointer-events: none;
}
</style>