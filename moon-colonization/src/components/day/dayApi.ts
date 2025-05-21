import axiosInstance from '../engine/axios-instance'

export const dayApi = {
  nextDay: async () => {
     const response = await axiosInstance.get('/api/day')
     return;
  },
}
