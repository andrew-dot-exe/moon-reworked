// API for colonization start
// @andrew-dot-exe, 2025

import axiosInstance from '../engine/axios-instance'

export const colonyApi = {
  createColony: async () => {
    try {
      const response = await axiosInstance.get(
        '/api/colony', // just GET, without any data
        {
          validateStatus: (status) => status === 200 || status === 409,
        },
      )
      if (response.status == 500) {
        console.error('server error on colony create')
      } else if (response.status == 409) {
        console.log('user has an alive colony')
      } else {
        console.log('colony created')
      }
    } catch (error) {}
  },
  deleteColony: async () => {
    try {
      const response = await axiosInstance.delete('/api/colony')
      console.log(response.data)
    } catch (error) {}
  },
}
