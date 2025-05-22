import axiosInstance from '../engine/axios-instance'
import { useResourceStore } from '@/stores/resourceStore'
import { userInfoStore } from '@/stores/userInfoStore'


export const dayApi = {
  nextDay: async () => {
    const resourceStore = useResourceStore()
    const uInfo = userInfoStore()
     const response = await axiosInstance.get('/api/day')
     if(response.data){
      await resourceStore.getResources()
      await uInfo.fetchUserInfo()
     }
     return;
  },
}
