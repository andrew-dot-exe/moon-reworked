import { defineStore } from 'pinia'
import { ref, computed, onUnmounted } from 'vue'
// import { useResourcesStore } from './resourcesStore'
// import { useUserInfoStore } from './userInfoStore'

export const useDayStore = defineStore('dayStore', () => {
  // Состояние скорости
  const speed = ref<0 | 1 | 2 | 3>(0)
  const updateInterval = ref<number | null>(null)
  
  // Другие необходимые хранилища
//   const resourcesStore = useResourcesStore()
//   const userInfoStore = useUserInfoStore()

  // Интервалы для разных скоростей (в мс)
  const speedIntervals = {
    pause: null,
    1: 5000,  // x1 - каждые 5 сек
    2: 2500,  // x2 - каждые 2.5 сек
    3: 1000   // x3 - каждые 1 сек
  }

  // Запуск/остановка автообновления
  const startAutoUpdate = () => {
    stopAutoUpdate() // Сначала останавливаем предыдущий интервал
    
    if (speed.value === 0) return
    
    const interval = speedIntervals[speed.value]
    
    updateInterval.value = window.setInterval(async () => {
      await updateGameData()
    }, interval)
  }

  const stopAutoUpdate = () => {
    if (updateInterval.value !== null) {
        window.clearInterval(updateInterval.value)
        updateInterval.value = null
      }
  }

  // Обновление всех данных игры
  const updateGameData = async () => {
    // Изменение дня
  }

  // Изменение скорости
  const setSpeed = (newSpeed: 0 | 1 | 2 | 3) => {
    speed.value = newSpeed
    startAutoUpdate()
  }

  // Автоматическая очистка при уничтожении
  onUnmounted(stopAutoUpdate)

  return {
    speed,
    setSpeed,
    startAutoUpdate,
    stopAutoUpdate,
    updateGameData
  }
})