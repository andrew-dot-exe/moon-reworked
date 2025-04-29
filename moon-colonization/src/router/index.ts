import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import ZoneChooser from '@/views/ZoneChooser.vue'
import MapView from '@/views/MapView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: ZoneChooser
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/map',
      name: 'map',
      component: MapView
    }
  ]
})

export default router
