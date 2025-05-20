import { defineStore } from 'pinia'
import type { MoonCell } from '@/components/engine/map-renderer/map'

export const useSelectedCellStore = defineStore('selectedCell', {
  state: () => ({
    selectedCell: null as MoonCell | null,
  }),
  actions: {
    setSelectedCell(cell: MoonCell) {
      this.selectedCell = cell
    },
    clearSelectedCell() {
      this.selectedCell = null
    },
  },
})
