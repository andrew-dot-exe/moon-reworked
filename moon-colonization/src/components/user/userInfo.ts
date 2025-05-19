import type { ZoneLink } from '../colony/zones'
import type { Module } from '../typeModules/modules'

export interface UserInfo {
  name: string
  currentDay: number // день по счету
  dayBeforeDelivery: number
  live: boolean
  links: ZoneLink[]
  modules: Module[]
}

export class User {
  private _name: string
  private _currentDay: number
  private _dayBeforeDelivery: number
  private _live: boolean
  private _links: ZoneLink[]
  private _modules: Module[]

  constructor(
    name: string,
    currentDay = 1,
    dayBeforeDelivery = 0,
    live = true,
    links: ZoneLink[] = [],
    modules: Module[] = [],
  ) {
    this._name = name
    this._currentDay = currentDay
    this._dayBeforeDelivery = dayBeforeDelivery
    this._live = live
    this._links = links
    this._modules = modules
  }

  get name(): string {
    return this._name
  }

  get currentDay(): number {
    return this._currentDay
  }

  set currentDay(value: number) {
    this._currentDay = value
  }

  get dayBeforeDelivery(): number {
    return this._dayBeforeDelivery
  }

  set dayBeforeDelivery(value: number) {
    this._dayBeforeDelivery = value
  }

  get live(): boolean {
    return this._live
  }

  set live(value: boolean) {
    this._live = value
  }

  get links(): ZoneLink[] {
    return [...this._links]
  }

  set links(value: ZoneLink[]) {
    this._links = [...value]
  }

  get modules(): Module[] {
    return [...this._modules]
  }

  set modules(value: Module[]) {
    this._modules = [...value]
  }
}

// user resources here
// get and state current user
