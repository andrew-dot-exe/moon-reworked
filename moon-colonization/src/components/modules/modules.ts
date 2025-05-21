import type { TypeModule, TypeModuleModel } from '../typeModules/typeModules'
import { ModuleMesh } from '../engine/map-renderer/baseModule'

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

export class MeshedModule {
  // Хранит в себе и модель для карты, и информацию (неоптимально)

  moduleMesh: ModuleMesh // мэш модуля, который будет расположен на карте
  moduleInfo: TypeModule

  constructor(moduleInfo: TypeModule) {
    this.moduleInfo = moduleInfo
    // информация о меше будет привязана по названию самого модуля
    this.moduleMesh = new ModuleMesh(moduleInfo.name)
  }

  getMesh() {
    return this.moduleMesh.getMesh()
  }
}
