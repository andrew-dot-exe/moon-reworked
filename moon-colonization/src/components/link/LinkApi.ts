import axiosInstance from '../engine/axios-instance'
import type { Link, PrimaryKey } from './Link'
import { useResourceStore } from '@/stores/resourceStore'
import { userInfoStore } from '@/stores/userInfoStore'


export const linkApi = {
  createLink: async (link: Link) => {
    const resourceStore = useResourceStore()
    const uInfo = userInfoStore()
     const response = await axiosInstance.post('/api/link', link)
     if(response.data){
      if(response.data > resourceStore.resources[7]) await uInfo.fetchUserInfo()
      else await resourceStore.getResources()
     }

     return response.data as number
  }
}
