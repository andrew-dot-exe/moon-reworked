// Хранилище компонентов, необхожимое для их переключения в зависимости от состояний Pinia
// @andrew-dot-exe, 2025
// stores/component.ts
import { defineStore } from 'pinia'
import { markRaw, type Component } from 'vue'
import LoginView from '@/views/LoginView.vue'
import ZoneChooserView from '@/views/ZoneChooserView.vue'
import ZoneChooseMapView from '@/views/ZoneChooseMapView.vue'
import MapView from '@/views/MapView.vue'

type Views = {
  login: typeof LoginView
  zoneChooser: typeof ZoneChooseMapView
}
export const useComponentStore = defineStore('component', {
  state: () => ({
    current: markRaw(LoginView) as Component, // Компонент, а не строка
    views: {
      login: markRaw(LoginView),
      zoneChooser: markRaw(ZoneChooseMapView),
      colonization: markRaw(MapView),
    },
  }),
  actions: {
    setComponent(viewName: keyof typeof this.views) {
      this.current = this.views[viewName] || (this.views.login as Component)
    },
  },
})
