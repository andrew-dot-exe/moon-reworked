import type { ZoneLink } from '../colony/zones'
import type { Module } from '../modules/modules'

export interface User {
  name: string
  currentDay: number // день по счету
  dayBeforeDelivery: number
  live: boolean
  links: ZoneLink[]
  modules: Module[]
}

// save in Pinia
export const createUser = (
  name: string,
  currentDay: number,
  dayBeforeDelivery: number,
  live: boolean,
  links: ZoneLink[],
  modules: Module[],
): User => {
  return {
    name: name,
    currentDay: currentDay,
    dayBeforeDelivery: dayBeforeDelivery,
    live: live,
    links: links,
    modules: modules,
  }
}
// user resources here
// get and state current user
