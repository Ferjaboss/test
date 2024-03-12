FROM node:18.13 AS builder

WORKDIR /app

COPY package*.json /app/

RUN npm install -g @angular/cli@17.1.1 && \
    npm install

COPY . /app/

RUN npm run build

FROM node:18.13-alpine

WORKDIR /app

COPY --from=builder /app/dist /app/dist

EXPOSE 4200

CMD ["npm", "start"]