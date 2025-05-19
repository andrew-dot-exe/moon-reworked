export interface TypeModuleModel {
    type: number
    name: String
    people: number
    cost: number
    live: boolean
  }
  
  export class TypeModule implements TypeModuleModel {
    type: number
    name: String
    people: number
    cost: number
    live: boolean
  
    constructor(type: number, name: String, people: number, cost: number, live: boolean) {
      this.type = type
      this.name = name
      this.people = people
      this.cost = cost
      this.live = live
    }
  }
  