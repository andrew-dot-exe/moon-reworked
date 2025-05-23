import { defineStore } from 'pinia'

export const useModuleInfoStore = defineStore('moduleInfoStore', {
  state: () => ({
    selectedModule: null as any
  }),
  actions: {
    setSelectedModule(module: any) {
      this.selectedModule = module
    }
  }
})
