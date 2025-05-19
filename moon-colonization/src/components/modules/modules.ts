export interface ModuleModel {
  id: number
  idZone: number
  moduleType: number
  // координаты относительно канваса
  x: number
  y: number
}
export interface EngineModuleModel {
  // Модель данных для информации модуля для отрисовки
  x: number
  y: number
  // ссылка на тип модуля
}

export class Module implements ModuleModel {
  id: number
  idZone: number
  moduleType: number
  x: number
  y: number

  constructor(id: number, idZone: number, moduleType: number, x: number, y: number) {
    this.id = id
    this.idZone = idZone
    this.moduleType = moduleType
    this.x = x
    this.y = y
  }
}
