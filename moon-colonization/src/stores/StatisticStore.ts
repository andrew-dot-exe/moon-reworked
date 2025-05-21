import { statisticApi } from '@/components/statistic/statisticApi'
import { Statistic } from '@/components/statistic/statistic'
import { defineStore } from 'pinia'

function delenie(arr: number[]){
  for(let i = 0; i < arr.length; i++){
    arr[i] = Math.floor(arr[i] / 1000)
  }
  return arr
}

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
          countResources: delenie(response.countResources) || [],
          sumConsumption: delenie(response.sumConsumption) || [],
          sumProduction:  delenie(response.sumProduction) || [],
          zoneProductions: response.zoneProductions.map(zone => ({
            id: zone.id,
            production:  Math.floor(zone.production /1000),
            consumption:  Math.floor(zone.consumption /1000)
          }))
        };
      }
    },
  },
})