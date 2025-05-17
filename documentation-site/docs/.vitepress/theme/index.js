import DefaultTheme from 'vitepress/theme'
import Collapse from '../components/Collapse.vue' // Путь к компоненту

export default {
  ...DefaultTheme,
  enhanceApp({ app }) {
    app.component('Collapse', Collapse) // Регистрация компонента
  }
}