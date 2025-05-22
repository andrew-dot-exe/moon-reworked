export interface ModuleModel {
  idZone: number
  typeModule: number
  // координаты относительно канваса
  x: number
  y: number
}

export class Module implements ModuleModel {
  idZone: number
  typeModule: number
  x: number
  y: number

  constructor(idZone: number, typeModule: number, x: number, y: number) {
    this.idZone = idZone
    this.typeModule = typeModule
    this.x = x
    this.y = y
  }
}

export class ModulePlacePosibillity {
  possible: boolean
  relief: number
  rationality: number | undefined

  constructor(possible: boolean, relief: number, rationality?: number) {
    this.possible = possible
    this.relief = relief
    this.rationality = rationality
  }
}
