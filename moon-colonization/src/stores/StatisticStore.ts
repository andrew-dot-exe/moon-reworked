import { statisticApi } from '@/components/statistic/statisticApi'
import { Statistic } from '@/components/statistic/statistic'
import { defineStore } from 'pinia'

export const statisticStore = defineStore('statisticStore', {
  state: () => ({
    statistic: null as Statistic | null,
  }),
  actions: {
    async getStatistic() {
      const response = await statisticApi.getStatistic()
      if (response) {
        this.statistic = {
          countDay: response.countDay,
          successful: response.successful,
          countResources: response.countResources || [],
          sumConsumption: response.sumConsumption || [],
          sumProduction: response.sumProduction || [],
          zoneProductions: response.zoneProductions.map(zone => ({
            id: zone.id,
            production: zone.production,
            consumption: zone.consumption
          }))
        };
      }
    },
  },
})