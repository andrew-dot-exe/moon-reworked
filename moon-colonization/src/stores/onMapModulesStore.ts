import { defineStore } from 'pinia'
import type { TypeModule } from '@/components/typeModules/typeModules'

export interface MapModule {
  id: number
  module: TypeModule
  x: number
  y: number
  width?: number
  height?: number
  gltfFile?: string
}

// Ассоциативный массив: baseName -> gltf-файл
export const gltfFileMap: Record<string, string> = {
  // Пример: 'test': 'test.gltf', 'manufacture': 'manufacture.glb'
  test: 'test.gltf',
  manufacture: 'manufacture.glb',
  // Добавьте остальные соответствия по мере необходимости
}

// Пример перевода (русское имя -> baseName)
export const moduleNameToBaseName: Record<string, string> = {
  Производство: 'manufacture',
  Тест: 'test',
  // ...добавьте остальные переводы
}

export function getGltfFileByBaseName(baseName: string): string | undefined {
  return gltfFileMap[baseName]
}

export const useOnMapModulesStore = defineStore('onMapModules', {
  state: () => ({
    modules: [] as MapModule[],
  }),
  actions: {
    addModule(module: MapModule) {
      this.modules.push(module)
    },
    removeModuleById(id: number) {
      this.modules = this.modules.filter((m) => m.id !== id)
    },
    clearModules() {
      this.modules = []
    },
    getModuleAt(x: number, y: number): MapModule | undefined {
      return this.modules.find((m) => m.x === x && m.y === y)
    },
    updateModule(id: number, data: Partial<MapModule>) {
      const idx = this.modules.findIndex((m) => m.id === id)
      if (idx !== -1) {
        this.modules[idx] = { ...this.modules[idx], ...data }
      }
    },
  },
})
