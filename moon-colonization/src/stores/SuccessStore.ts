import { successApi } from '@/components/success/successApi'
import { Success } from '@/components/success/success.ts'
import { defineStore } from 'pinia'

export const useSuccessStore = defineStore('successStore', {
  state: () => ({
    success: null as Success | null,
  }),
  actions: {
    async getSuccess() {
      const response = await successApi.getSuccessApi()
      if (response) {
        this.success = {
          successful: response.successful,
          mood: response.mood,
          contPeople: response.contPeople,
          needContPeople: response.needContPeople,
          resources: response.resources,
          central: response.central,
          search: response.search
        };
      }
    },
  },
})