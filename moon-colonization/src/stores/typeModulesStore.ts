import { typeModulesApi } from '@/components/typeModules/typeModulesApi'
import { TypeModule } from '@/components/typeModules/typeModules'
import { defineStore } from 'pinia'

export const typeModulesStore = defineStore('typeModulesStore', {
  state: () => ({
    typeModules: [] as TypeModule[],
  }),
  actions: {
    async getTypeModules() {
      if(this.typeModules === null || this.typeModules === undefined || this.typeModules.length == 0){
        const response = await typeModulesApi.getTypeModulesApi()
        if (response !== undefined) {
          // map
          response.forEach((item: TypeModule) => {
            this.typeModules.push(new TypeModule(item.type, item.name, item.people, item.cost, item.live))
          })
        }
      }
    },
  },
})