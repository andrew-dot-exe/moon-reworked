import { defineStore } from 'pinia'
import { User } from '../components/user/userInfo'
import userApi from '@/components/user/userApi'

export const userInfoStore = defineStore('userInfoStore', {
  state: () => ({
    userInfo: {} as User, // Чистая реактивность без ref
    lastFetchTime: 0, // Время последнего запроса
    cacheDuration: 5000, // 5 секунд в миллисекундах
  }),
  actions: {
    async fetchUserInfo() {
      const now = Date.now()
      if (now - this.lastFetchTime < this.cacheDuration) {
        return
      }
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
          this.lastFetchTime = now
        }
      } catch (error) {
        console.error('Failed to fetch user info:', error)
        throw error // Можно перебросить для обработки в компоненте
      }
    }
  }
})