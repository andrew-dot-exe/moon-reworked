import { defineStore } from 'pinia'
import type { TypeModule } from '@/components/typeModules/typeModules'

export const useModuleInfoStore = defineStore('moduleInfo', {
  state: () => ({
    selectedModule: null as TypeModule | null,
  }),
  actions: {
    setSelectedModule(module: TypeModule) {
      this.selectedModule = module
    },
    clearSelectedModule() {
      this.selectedModule = null
    },
  },
})
