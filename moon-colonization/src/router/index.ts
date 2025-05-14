import { createRouter, createWebHistory } from 'vue-router'
import ZoneChooser from '@/views/ZoneChooser.vue'
import MapView from '@/views/MapView.vue'
import LoginView from '@/views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: LoginView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/map',
      name: 'map',
      component: MapView,
    },
  ],
})

export default router
