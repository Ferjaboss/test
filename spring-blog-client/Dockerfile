# Stage 1: Build the Angular application
FROM node:18.13 AS builder

WORKDIR /app

COPY package*.json /app/

RUN npm install -g @angular/cli@17.1.1 && \
    npm install

COPY . /app/

RUN npm run build


FROM nginx:1.21.1-alpine

COPY --from=builder /app/dist/spring-blog-client /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]