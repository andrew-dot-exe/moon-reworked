import { userInfoStore } from './userInfoStore'
import { Link, PrimaryKey } from '@/components/link/Link'
import { defineStore } from 'pinia'

export const useLinkStore= defineStore('LinkStore', {
  state: () => ({
    links: [] as PrimaryKey[],
  }),
  actions: {
    async getLink() {
        const userStore = userInfoStore()
        await userStore.fetchUserInfo()
        
        if (userStore.userInfo?.links) {
          this.links = userStore.userInfo.links
        }
    },
  },
})