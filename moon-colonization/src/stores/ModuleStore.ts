import type { Module } from '@/components/modules/modules'
import { modulesApi } from '@/components/modules/modulesApi'
import { defineStore } from 'pinia'
import { userInfoStore } from './userInfoStore'
import { Optimality } from '@/components/modules/optimality'

export const useModuleStore = defineStore('moduleSStore', {
  state: () => ({
    modules: [] as Module[], // Массив без ref - Pinia сделает его реактивным
    optimals: [] as Optimality[]
  }),
  actions: {
    async createModule(module: Module) {
      const response = await modulesApi.createModule(module)
      if (response.data) {
        module.id = response.data.id
        this.modules.push(module)
      }
    },
    async getModules(){
        const uInfo = userInfoStore()
        await uInfo.fetchUserInfo()
        this.modules = uInfo.userInfo.modules
    },
    async getOptimality(){
      const response = await modulesApi.getOptimal()
      if(response){
        this.optimals = response.map((item: Optimality) => new Optimality(
          item.id != null ? item.id : 0,
          item.relief != null ? item.relief : 0,
          item.rationality != null ? item.rationality : 0,
        ))
      }
    },
    getOptimal(id: number){
      for(let i = 0; i < this.optimals.length; i++){
        if(this.optimals[i].id == id) return this.optimals[i]
      }
      return null
    }
  },
})