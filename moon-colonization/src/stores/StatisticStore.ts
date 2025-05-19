import { statisticApi } from '@/components/statistic/statisticApi'
import { Statistic } from '@/components/statistic/statistic'
import { ZoneProduction } from '@/components/statistic/ZoneProduction'
import { defineStore } from 'pinia'

export const statisticStore = defineStore('statisticStore', {
  state: () => ({
    statistic: [] as Statistic[],
  }),
  actions: {
    async getStatistic() {
      const response = await statisticApi.getStatistic()
      if (response !== undefined) {
        // map
        response.forEach((item: Statistic) => {
            const zones = [] as ZoneProduction[];
            for(let i = 0; i < item.zoneProductions.length; i++){
                zones.push(new ZoneProduction(item.zoneProductions[i].id, item.zoneProductions[i].production, item.zoneProductions[i].consumption))
            }
          this.statistic.push(new Statistic(item.countDay, item.successful,
             item.countResources, item.sumProduction, item.sumConsumption, zones))
        })
      }
    },
  },
})