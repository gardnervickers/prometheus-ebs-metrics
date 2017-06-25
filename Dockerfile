FROM node:6.11.0-alpine
ADD target/main.js main.js
ENTRYPOINT node main.js