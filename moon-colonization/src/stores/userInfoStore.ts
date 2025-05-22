import { defineStore } from 'pinia'
import { User } from '../components/user/userInfo'
import userApi from '@/components/user/userApi'

export const userInfoStore = defineStore('userInfoStore', {
  state: () => ({
    userInfo: {} as User, // Чистая реактивность без ref
  }),
  actions: {
    async fetchUserInfo() {
      try {
        const response = await userApi.get_info()
        if (response) {
          // Создаем новый объект User (иммутабельное обновление)
          this.userInfo = new User(
            response.name,
            response.currentDay,
            response.dayBeforeDelivery,
            response.live,
            response.links,
            response.modules
          )
        }
      } catch (error) {
        console.error('Failed to fetch user info:', error)
        throw error // Можно перебросить для обработки в компоненте
      }
    }
  }
})