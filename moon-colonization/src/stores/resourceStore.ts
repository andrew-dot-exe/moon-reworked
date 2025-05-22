import { resourcesApi } from '@/components/resources/resourceApi'
import { ResourceModel } from '@/components/resources/resources'
import { defineStore } from 'pinia'

export const useResourceStore = defineStore('resourceStore', {
  state: () => ({
    resources: [] as ResourceModel[], // Массив без ref - Pinia сделает его реактивным
  }),
  actions: {
    async getResources() {
      const response = await resourcesApi.getResources()
      if (response) {
        // 1. Создаем новый массив (важно для реактивности)
        const newResources = response.map((item: ResourceModel) => 
          new ResourceModel(
            item.type, 
            Math.floor(item.count / 1000), 
            item.production
          )
        )
        
        // 2. Полностью заменяем массив (триггер обновления)
        this.resources = newResources
      }
    },
  },
})