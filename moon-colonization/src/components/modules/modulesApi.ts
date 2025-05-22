import axiosInstance from '../engine/axios-instance'
import type { Module, ModulePlacePosibillity } from './modules'

export const modulesApi = {
  checkModule: async (module: Module) => {
    const response = await axiosInstance.post('/api/check', module)
    if (response.data.relief == null) response.data.relief = 0
    if (response.data.rationality == null) response.data.rationality = 0
    return response.data as ModulePlacePosibillity
  },
}
