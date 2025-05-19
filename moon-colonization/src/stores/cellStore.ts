import { defineStore } from 'pinia'

export const useCellStore = defineStore('cellStore', {
  state: () => ({
    selectedCellCoords: { x: null as number | null, z: null as number | null },
  }),
  actions: {
    selectCell(x: number, z: number) {
      this.selectedCellCoords = { x, z }
    },
  },
})
