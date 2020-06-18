# FabFlix  

## Description  
FabFlix is an undergraduate project of the course CS122B - Project in Database Management.  
FabFlix is a single-page E-commerce web application that mimic Amazon website in a smaller and simpler scale. FabFlix uses REST architecture style, which separates the back-end into 4 microservices: *IDM, Movies, Billing and Payment, API Gateway*. The back-end is implemented in Java with Grizzly to serve as web framework, Jackson to converts JSON to Java Object and vice versa, Jersey to support REST annotation. It uses MySQL for the database. The front-end is designed by the author and implemented in pure HTML, CSS and JavaScript with JQuery and AJAX for DOM manipulation.  

This project is an individual project, built in 12 weeks by the author.  
### IDM  
Identity Management is responsible for user registration, user login and priviledge validation. This microservice also generates user token to manage the session of the user. Each user token only has a certain amount of time being valid. If the user is inactive, the user will have to re-login.  
### Movies  
Movies is responsible for storing all movies information including movie stars, ratings and genres.  
### Billing and Payment  
Billing and Payment is responsible for price calculation and number of items. The process of paying for the items is implemented using PayPal SDK to enhance security and avoid reinventing the wheel.  
### API Gateway  
API Gateway serves as the front door which verifies the header of every request. The request that can be redirected to its desired endpoint has a valid user token and appropriate HTTP Method.  

## Author  
Huy Minh Tran
