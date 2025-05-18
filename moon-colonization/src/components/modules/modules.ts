export interface Module {
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
