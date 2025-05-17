import { resourcesApi } from '@/components/resources/resourceApi'
import { ResourceModel } from '@/components/resources/resources'
import { defineStore } from 'pinia'

export const useResourceStore = defineStore('resourceStore', {
  state: () => ({
    resources: [] as ResourceModel[],
  }),
  actions: {
    async getResources() {
      const response = await resourcesApi.getResources()
      if (response !== undefined) {
        // map
        response.forEach((item: ResourceModel) => {
          this.resources.push(new ResourceModel(item.type, item.count, item.production))
        })
      }
    },
  },
})
