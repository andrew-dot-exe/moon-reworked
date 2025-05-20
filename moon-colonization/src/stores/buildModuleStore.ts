import { defineStore } from 'pinia'
import type { TypeModule } from '@/components/typeModules/typeModules'

export const useBuildModuleStore = defineStore('buildModule', {
  state: () => ({
    selectedBuildModule: null as TypeModule | null,
    selectedBuildModelFile: null as string | null
  }),
  actions: {
    setBuildModule(module: TypeModule, modelFile?: string) {
      this.selectedBuildModule = module
      if (modelFile) this.selectedBuildModelFile = modelFile
    },
    clearBuildModule() {
      this.selectedBuildModule = null
      this.selectedBuildModelFile = null
    }
  }
})
