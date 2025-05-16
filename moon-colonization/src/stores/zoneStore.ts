import { Zone, zoneApi } from '@/components/colony/zones'
import { defineStore } from 'pinia'

// all info for colony
export const useZoneStore = defineStore('zoneStore', {
  state: () => ({
    zones: [] as Zone[],
  }),
  actions: {
    async fetchAllZones() {
      const response = await zoneApi.getAllZones()
      if (response !== undefined) {
        this.zones = response
      }
    },
  },
})
