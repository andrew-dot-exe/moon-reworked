import axiosInstance from '../engine/axios-instance'
import type { Module, ModulePlacePosibillity } from './modules'
import { useResourceStore } from '@/stores/resourceStore'
import { userInfoStore } from '@/stores/userInfoStore'


export const modulesApi = {
  checkModule: async (module: Module) => {
    const response = await axiosInstance.post('/api/check', module)
    if (response.data.relief == null) response.data.relief = 0
    if (response.data.rationality == null) response.data.rationality = 0
    return response.data as ModulePlacePosibillity
  },
  createModule: async(module: Module) => {
    const resourceStore = useResourceStore()
    const uInfo = userInfoStore()
    const response = await axiosInstance.post('/api/module', {id_zone: module.idZone, module_type: module.typeModule, x: module.x, y:module.y})
    if(response.data){
      await uInfo.fetchUserInfo()
      await resourceStore.getResources()
    }
    return response.data // id
  },
  getOptimal: async () => {
    const response = await axiosInstance.get('/api/module')
    return response.data
  }
}
