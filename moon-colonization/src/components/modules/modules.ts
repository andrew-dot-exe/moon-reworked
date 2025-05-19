import type { TypeModule, TypeModuleModel } from '../typeModules/typeModules'
import { ModuleMesh } from '../engine/map-renderer/baseModule'

export interface ModuleModel {
  id: number
  idZone: number
  moduleType: number
  // координаты относительно канваса
  x: number
  y: number
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
