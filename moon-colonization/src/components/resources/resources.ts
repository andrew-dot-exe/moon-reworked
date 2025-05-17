export interface ResourceModel {
  type: number
  count: number
  production: number
}

export class ResourceModel {
  type: number
  count: number
  production: number

  constructor(type: number, count: number, production: number) {
    this.type = type
    this.count = count
    this.production = production
  }
}
