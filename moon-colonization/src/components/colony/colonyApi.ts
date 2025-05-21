import axiosInstance from '../engine/axios-instance'

export const colonyApi = {
  deleteColony: async () => {
     const response = await axiosInstance.delete('/api/colony')
     return;
  },
  createColony: async () => {
    const response = await axiosInstance.get('/api/colony')
    return;
 },
}
