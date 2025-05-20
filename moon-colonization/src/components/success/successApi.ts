import axiosInstance from '../engine/axios-instance'

export const successApi = {
  getSuccessApi: async () => {
    const response = await axiosInstance.get('/api/success')
    return response.data
  },
}
