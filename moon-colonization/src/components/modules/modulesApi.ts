import axiosInstance from '../engine/axios-instance'
import type { Module, ModulePlacePosibillity } from './modules'

export const modulesApi = {
  checkModule: async (module: Module) => {
    // Всегда положительный результат
    /*return {
      opt: 100,
      rel: 100,
      rat: 100,
      possible: true,
      relief: 100,
      rationality: 100,
    } as ModulePlacePosibillity*/
    // Если нужно вернуть реальный ответ, раскомментируйте:
     const response = await axiosInstance.post('/api/check', module)
     if(response.data.relief == null) response.data.relief = 0
     if(response.data.rationality == null) response.data.rationality = 0
     return response.data as ModulePlacePosibillity
  },
}
