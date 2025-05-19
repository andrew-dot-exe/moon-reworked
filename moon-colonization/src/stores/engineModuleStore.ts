// Стор для хранения информации о модулях, которая нужна для их отрисовки

import { defineStore } from 'pinia'

export const useEngineModuleStore = defineStore('engineModuleStore', {
  state: () => ({
    // должно содержать размер, путь к модели, путь к текстурам (если есть)
  }),
  actions: {
    getModuleInfoByName() {},
  },
})
