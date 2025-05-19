// Стор для хранения информации о модулях, которая нужна для их отрисовки

import { defineStore } from 'pinia'

export const onMapModuleStore = defineStore('onMapModuleStore', {
  state: () => ({}),
  actions: {
    async getZones() {
      // получаем зоны с отдельного стора под них
    },

    getModuleInfoByName() {},
  },
})
