import type { Cell } from '@/components/cell/Cell'
import { CellApi } from '@/components/cell/CellApi'
import { defineStore } from 'pinia'

export const useCellStore = defineStore('cellStore', {
  state: () => ({
    cells: [] as Cell[][][]
  }),
  actions: {
    async getCells(idZone: number | undefined) {
      if(idZone == undefined) return
      if (!this.cells[idZone]) {
        this.cells[idZone] = await CellApi.getCellFromZone(idZone)
      }
    },
  },
})
