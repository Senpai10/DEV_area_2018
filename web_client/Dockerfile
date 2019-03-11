FROM node:latest
WORKDIR /app
COPY package.json /app
RUN npm install
COPY /frontend /app
COPY . /app
CMD node index.js
EXPOSE 8081