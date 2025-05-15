import { defineConfig } from 'vitepress'

// https://vitepress.vuejs.org/config/app-configs
export default defineConfig({
    title: "Documetation",
    themeConfig: {
        nav: [
            {text: 'Main', link: '/markdown/Пользовательская документация' }
        ]
    }
})
