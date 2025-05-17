import DefaultTheme from 'vitepress/theme'
import Collapse from '../components/Collapse.vue' // Путь к компоненту
import ThemeIcon from '../components/ThemeIcon.vue'

export default {
  ...DefaultTheme,
  enhanceApp({ app }) {
    app.component('Collapse', Collapse) // Регистрация компонента
    app.component('ThemeIcon', ThemeIcon)
  }
}