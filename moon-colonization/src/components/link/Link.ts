

export class PrimaryKey {
    type: number
  id_zone1: number
  id_zone2: number

  constructor(type: number, id_zone1: number, id_zone2: number) {
    this.type = type
    this.id_zone1 = id_zone1
    this.id_zone2 = id_zone2
  }
}

export class Link  {
  primaryKey: PrimaryKey

  constructor(primaryKey: PrimaryKey) {
    this.primaryKey = primaryKey
  }
}
