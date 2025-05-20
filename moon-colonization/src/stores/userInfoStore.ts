import { defineStore } from 'pinia'
import { User } from '../components/user/userInfo'
import userApi from '@/components/user/userApi'

export const userInfoStore = defineStore('userInfoStore', {
  state: () => ({
    userInfo: {} as User,
  }),
  actions: {
    async fetchUserInfo() {
      const response = await userApi.get_info()
      if (response !== undefined) {
        this.userInfo = new User(
          response.name,
          response.curDay,
          response.dayBeforeDelivery,
          response.live,
          response.links,
          response.modules,
        )
      }
    },
  },
})
