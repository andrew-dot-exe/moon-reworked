import axiosInstance from '../engine/axios-instance'
import type { Link, PrimaryKey } from './Link'

export const linkApi = {
  createLink: async (link: Link) => {
     const response = await axiosInstance.post('/api/link', link)
     return response.data as number
  }
}
