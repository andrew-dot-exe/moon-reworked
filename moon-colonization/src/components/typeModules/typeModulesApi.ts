import axiosInstance from '../engine/axios-instance'

export const typeModulesApi = {
  getResources: async () => {
    const response = await axiosInstance.get('/api/module/types')
    return response.data
  },
}
