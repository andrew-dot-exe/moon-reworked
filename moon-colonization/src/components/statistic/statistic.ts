import {ZoneProduction} from '@/components/statistic/ZoneProduction.ts'

export class Statistic {
    countDay: number;
    successful: number;
    countResources: number [];
    sumProduction: number[];
    sumConsumption: number[];
    zoneProductions: ZoneProduction[];
  
    constructor(countDay: number, successful: number, countResources: number [], sumProduction: number[], sumConsumption: number[], zoneProductions: ZoneProduction[]) {
      this.countDay = countDay
      this.successful = successful
      this.countResources = countResources
      this.sumProduction = sumProduction
      this.sumConsumption = sumConsumption
      this.zoneProductions = zoneProductions
    }
}