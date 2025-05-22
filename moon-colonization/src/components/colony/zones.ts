import axiosInstance from '../engine/axios-instance'

export interface ZoneModel {
  id: number
  name: string
  widthSecond: number
  longitudeSecond: number
  ways: number[]
  size: number
}

export interface ZoneLinkModel {
  type: number
  id_zone1: number
  id_zone2: number
}

export class Zone implements ZoneModel {
  id: number
  name: string
  widthSecond: number
  longitudeSecond: number
  ways: number[]
  size: number

  constructor(
    id: number,
    name: string,
    widthSecond: number,
    longitudeSecond: number,
    ways: number[] = [],
  ) {
    this.id = id
    this.name = name
    this.widthSecond = widthSecond
    this.longitudeSecond = longitudeSecond
    this.ways = ways
    if (this.id % 2 != 0) {
      this.size = 20
    } else {
      this.size = 10
    }
  }
}

export class ZoneLink implements ZoneLinkModel {
  type: number
  id_zone1: number
  id_zone2: number

  constructor(type: number, id_zone1: number, id_zone2: number) {
    this.type = type
    this.id_zone1 = id_zone1
    this.id_zone2 = id_zone2
  }
}

export const zoneApi = {
  getAllZones: async () => {
    try {
      const response = await axiosInstance.get('/api/area/zones')
      return response.data as ZoneModel[]
    } catch (error) {
      console.log(error)
    }
  },
}
