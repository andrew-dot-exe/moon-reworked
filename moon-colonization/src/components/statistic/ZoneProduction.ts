export class ZoneProduction {
    id: number;
    production: number[];
    consumption: number[];
  
    constructor(id: number, production: number[], consumption: number[]) {
      this.id = id
      this.production = production
      this.consumption = consumption
    }
}