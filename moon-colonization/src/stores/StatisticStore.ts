import { statisticApi } from '@/components/statistic/statisticApi'
import { Statistic } from '@/components/statistic/statistic'
import { ZoneProduction } from '@/components/statistic/ZoneProduction'
import { defineStore } from 'pinia'

export const statisticStore = defineStore('statisticStore', {
  state: () => ({
    statistic: null as Statistic | null,
  }),
  actions: {
    async getStatistic() {
      const response = await statisticApi.getStatistic()
      console.log("API Response:", response); // Что пришло с бэкенда?
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
        console.log('Processed data:', this.statistic); 
        // map
        /*response.forEach((item: Statistic) => {
            const zones = [] as ZoneProduction[];
            for(let i = 0; i < item.zoneProductions.length; i++){
                zones.push(new ZoneProduction(item.zoneProductions[i].id, item.zoneProductions[i].production, item.zoneProductions[i].consumption))
            }
          this.statistic.push(new Statistic(item.countDay, item.successful,
             item.countResources, item.sumProduction, item.sumConsumption, zones))
        })*/
      }
    },
  },
})