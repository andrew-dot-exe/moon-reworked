<template>
    <div class="map-view">
      <MapRenderer ref="mapRendererRef" @cell-selected="onCellSelected" />

      <!-- UI оверлей -->
      <div class="ui-overlay">
        <!-- Верхняя панель информации -->
        <div class="info-panel top-panel">
          <div class="location-info">
            <span>Текущая локация: {{ currentZone }}</span>
          </div>
          <div class="resources-info">
            <span>Ресурсы: {{ playerResources.energy }} ⚡ | {{ playerResources.materials }} 🧱</span>
          </div>
        </div>

        <!-- Панель информации о выбранной клетке -->
        <div class="info-panel cell-info" v-if="selectedCell.x !== null">
          <h3>Информация о клетке</h3>
          <div class="cell-coordinates">
            <span>Координаты: {{ selectedCell.x }}, {{ selectedCell.z }}</span>
          </div>
          <div class="cell-details">
            <p>Тип местности: {{ selectedCell.type }}</p>
            <p>Ресурсы: {{ selectedCell.resources || 'Нет' }}</p>
            <p>Высота: {{ selectedCell.height }}</p>
          </div>
          <div class="cell-actions">
            <button @click="buildStructure" :disabled="!canBuild">Построить сооружение</button>
            <button @click="exploreCell">Исследовать</button>
          </div>
        </div>

        <!-- Панель настроек карты -->
        <div class="map-controls">
          <button @click="toggleWireframe">{{ isWireframe ? 'Выключить' : 'Включить' }} каркас</button>
          <button @click="toggleGrid">{{ isGridVisible ? 'Скрыть' : 'Показать' }} сетку</button>
          <button @click="resetCamera">Сбросить камеру</button>
        </div>

        <!-- Мини-карта (можно добавить позже) -->
        <div class="minimap">
          <!-- Здесь будет мини-карта -->
        </div>
      </div>
    </div>
  </template>

  <script lang="ts">
  import { defineComponent, ref, reactive } from 'vue';
  import MapRenderer from '@/components/ui/MapRenderer.vue';

  export default defineComponent({
    components: {
      MapRenderer
    },
    setup() {
      const mapRendererRef = ref<InstanceType<typeof MapRenderer> | null>(null);
      const currentZone = ref('Море Спокойствия');

      // Состояние игрока
      const playerResources = reactive({
        energy: 100,
        materials: 50
      });

      // Состояние выбранной клетки
      const selectedCell = reactive({
        x: null as number | null,
        z: null as number | null,
        type: '',
        resources: '',
        height: 0,
        hasStructure: false
      });

      // Состояние UI
      const isWireframe = ref(false);
      const isGridVisible = ref(true);
      const canBuild = ref(false);

      // Обработчик выбора клетки
      const onCellSelected = (x: number, z: number, cellData: any) => {
        selectedCell.x = x;
        selectedCell.z = z;
        selectedCell.type = cellData.type || 'Неизвестно';
        selectedCell.resources = cellData.resources || '';
        selectedCell.height = cellData.height || 0;
        selectedCell.hasStructure = cellData.hasStructure || false;

        // Определяем, можно ли строить
        canBuild.value = !selectedCell.hasStructure && playerResources.materials >= 10;
      };

      // Действия с картой
      const toggleWireframe = () => {
        isWireframe.value = !isWireframe.value;
        if (mapRendererRef.value) {
          mapRendererRef.value.toggleWireframe(isWireframe.value);
        }
      };

      const toggleGrid = () => {
        isGridVisible.value = !isGridVisible.value;
        if (mapRendererRef.value) {
          mapRendererRef.value.toggleGrid(isGridVisible.value);
        }
      };

      const resetCamera = () => {
        if (mapRendererRef.value) {
          mapRendererRef.value.resetCamera();
        }
      };

      // Действия с клеткой
      const buildStructure = () => {
        if (!canBuild.value) return;

        // Списываем ресурсы
        playerResources.materials -= 10;
        playerResources.energy -= 5;

        // Отмечаем, что на клетке есть сооружение
        selectedCell.hasStructure = true;
        canBuild.value = false;

        // Вызываем метод в MapRenderer для визуализации сооружения
        if (mapRendererRef.value && selectedCell.x !== null && selectedCell.z !== null) {
          mapRendererRef.value.addStructure(selectedCell.x, selectedCell.z, 'base');
        }
      };

      const exploreCell = () => {
        // Лёгкое исследование
        playerResources.energy -= 2;

        // Симуляция обнаружения ресурсов при исследовании
        if (Math.random() > 0.7 && !selectedCell.resources) {
          const resources = ['water', 'iron', 'helium', 'titanium'][Math.floor(Math.random() * 4)];
          selectedCell.resources = resources;

          // Обновляем данные в рендерере
            if (mapRendererRef.value && selectedCell.x !== null && selectedCell.z !== null) {
              mapRendererRef.value.updateCellData(selectedCell.x, selectedCell.z, { resources });
            }
        }
      };

      return {
        mapRendererRef,
        currentZone,
        playerResources,
        selectedCell,
        isWireframe,
        isGridVisible,
        canBuild,
        onCellSelected,
        toggleWireframe,
        toggleGrid,
        resetCamera,
        buildStructure,
        exploreCell
      };
    }
  });
  </script>

  <style scoped>
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
    pointer-events: none; /* Позволяет кликам проходить сквозь UI на карту */
  }

  .info-panel {
    pointer-events: auto; /* Восстанавливает возможность взаимодействовать с элементами панели */
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
    display: none; /* Пока скрываем, реализация будет позже */
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
