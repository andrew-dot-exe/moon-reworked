import type { Module } from '@/components/modules/modules'
import { modulesApi } from '@/components/modules/modulesApi'
import { defineStore } from 'pinia'
import { userInfoStore } from './userInfoStore'

export const useModuleStore = defineStore('moduleSStore', {
  state: () => ({
    modules: [] as Module[], // Массив без ref - Pinia сделает его реактивным
  }),
  actions: {
    async createModule(module: Module) {
      const response = await modulesApi.createModule(module)
      if (response) {
        module.id = response.data.id
        this.modules.push(module)
      }
    },
    async getModules(){
        const uInfo = userInfoStore()
        await uInfo.fetchUserInfo()
        this.modules = uInfo.userInfo.modules
    }
  },
})