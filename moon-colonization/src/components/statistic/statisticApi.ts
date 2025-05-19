import axiosInstance from '../engine/axios-instance'

export const statisticApi = {
  getStatistic: async () => {
    const response = await axiosInstance.get('/api/user/statistic')
    return response.data
  },
}
