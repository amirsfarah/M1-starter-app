ARG PORT

FROM node:20.6-alpine

WORKDIR /app/backend

COPY backend/package*json backend/tsconfig.json ./

RUN npm ci

COPY backend ./ 
RUN npm run build  

EXPOSE $PORT

CMD ["npm", "start"]