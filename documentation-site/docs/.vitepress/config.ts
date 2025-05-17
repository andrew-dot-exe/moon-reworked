import { defineConfig } from 'vitepress'
import markdownItMathjax3 from 'markdown-it-mathjax3'

// https://vitepress.vuejs.org/config/app-configs
export default defineConfig({
    title: "Documetation",
    themeConfig: {
        /*nav: [ вверхняя шапка
            {text: 'Main', link: '/markdown/Пользовательская документация' }
        ]*/
        sidebar: [
            {
              text: 'Страницы', // Заголовок раздела
              items: [
                { text: 'Модули', link: '/pages/Модули' },
                { text: 'Области', link: '/pages/Области' },
                { text: 'Ресурсы', link: '/pages/Ресурсы' },
                { text: 'Успешность колонии', link: '/pages/Успешность' },
                { text: 'Карта (расчет)', link: '/pages/Карта' },
                { text: 'Используемые ссылки', link: '/pages/ссылки' }
              ]
            }
        ]
    },
    markdown: {
      config: (md) => {
        md.use(markdownItMathjax3)
      }
    },
    head: [
      ['link', { 
        rel: 'stylesheet', 
        href: 'https://cdn.jsdelivr.net/npm/katex@0.15.2/dist/katex.min.css' 
      }]
    ]
})
