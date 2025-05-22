import { zoneApi } from '../colony/zones'
import axiosInstance from '../engine/axios-instance'
import { Cell } from './Cell'


export const CellApi = {
  getCellFromZone: async (idZone: number) => {
    const response = await axiosInstance.get(`/api/area/cells/${idZone}`)
    return response.data as Cell[][]
  }
}
