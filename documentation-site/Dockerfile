# Production build and serve with nginx
FROM node:24-alpine3.20 AS build
WORKDIR /app
COPY . .
RUN npm install && npx vitepress build docs

FROM nginx:1.25-alpine
COPY --from=build /app/docs/.vitepress/dist /usr/share/nginx/html
EXPOSE 80
