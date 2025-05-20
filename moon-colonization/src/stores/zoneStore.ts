import { Zone, zoneApi } from '@/components/colony/zones'
import { defineStore } from 'pinia'

// all info for colony
export const useZoneStore = defineStore('zoneStore', {
  state: () => ({
    zones: [] as Zone[],
    current_zone: {} as Zone,
  }),
  actions: {
    async fetchAllZones() {
      if(this.zones === null || this.zones === undefined || this.zones.length == 0){
        const response = await zoneApi.getAllZones()
        if (response !== undefined) {
          this.zones = response
        }
      }
    },
    selectZone(id: number) {
      this.current_zone = this.zones[id]
    },
  },
})
