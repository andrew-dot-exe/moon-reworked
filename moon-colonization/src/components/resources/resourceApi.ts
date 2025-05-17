import axiosInstance from '../engine/axios-instance'

export const resourcesApi = {
  getResources: async () => {
    const response = await axiosInstance.get('/api/product')
    return response.data
  },
}
