import { defineStore } from 'pinia'
import { User } from '../components/user/userInfo'
import userApi from '@/components/user/userApi'

const userInfoStore = defineStore('userInfoStore', {
  state: () => ({
    userInfo: {} as User,
  }),
  actions: {
    async fetchUserInfo() {
      const response = await userApi.get_info()
      if (response !== undefined) {
        this.userInfo = new User(
          response.data.name,
          response.data.curDay,
          response.data.dayBeforeDelivery,
          response.data.live,
          response.data.links,
          response.data.modules,
        )
      }
    },
  },
})
