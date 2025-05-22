import { Zone, zoneApi } from '@/components/colony/zones'
import { defineStore } from 'pinia'

// all info for colony
export const useZoneStore = defineStore('zoneStore', {
  state: () => ({
    zones: [] as Zone[],
    current_zone: null as Zone | null,
  }),
  actions: {
    async fetchAllZones() {
      if (this.zones === null || this.zones === undefined || this.zones.length == 0) {
        const response = await zoneApi.getAllZones()
        if (response !== undefined) {
          // Преобразуем объекты из API в объекты класса Zone
          this.zones = response.map(
            (z) => new Zone(z.id, z.name, z.widthSecond, z.longitudeSecond, z.ways),
          )
        }
      }
    },
    selectZone(id: number) {
      const temp = this.zones[id]
      this.current_zone = new Zone(
        temp.id,
        temp.name,
        temp.widthSecond,
        temp.longitudeSecond,
        temp.ways,
      )
    },
    selectZoneByName(name: string) {
      const zone = this.zones.find((z) => z.name === name)
      if (zone) {
        this.current_zone = new Zone(
          zone.id,
          zone.name,
          zone.widthSecond,
          zone.longitudeSecond,
          zone.ways,
        )
      }
    },
  },
})
