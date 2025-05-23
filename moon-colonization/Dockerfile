# Production build and serve with nginx
FROM node:24-alpine3.20 AS build
WORKDIR /app
COPY . .
RUN npm install && npm run build

FROM nginx:1.25-alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
