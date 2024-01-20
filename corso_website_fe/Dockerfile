# Stage 1 - Build the React application
FROM node:18-alpine as react-build
WORKDIR /corso_website_fe/
COPY public /corso_website_fe/public/
COPY src/ /corso_website_fe/src
COPY package.json /frontend/
COPY package.json .
COPY package-lock.json .
COPY tsconfig.json .
ENV REACT_APP_BE_HOST https://api.corsoelectriqueinc.tech/
RUN npm install
RUN npm run build

# Stage 2 - Serve the React application using NGINX
FROM nginx:alpine

# copy the .conf template
COPY ./nginx.conf /etc/nginx/nginx.conf

## Remove default nginx index page and replace it with the static files we created in the first step
RUN rm -rf /usr/share/nginx/html/*
COPY --from=react-build /corso_website_fe/build /usr/share/nginx/html
EXPOSE 80

CMD nginx -g 'daemon off;'

