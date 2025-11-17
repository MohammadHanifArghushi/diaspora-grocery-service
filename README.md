This project, Diaspora Grocery Service, is a Spring Boot app I built for my Backend Programming course and is part of my thesis at Haaga helia UAS too. The main idea is to help Afghans living abroad buy groceries online for their families back home.

So far, I’ve finished setting up the backend with product and category management, secure login (Spring Security), and a full REST API for admins and users. The database uses PostgreSQL, and everything runs smoothly locally.

Next, I’ll be working on the order management system and later integrating Stripe payments and email notifications. Deployment to the Haaga-Helia cloud is also planned once all the main features are done.

Current status: Backend core and security done, Working on order management after this
___________________________________________________________________________

I built the full order management system. Users can now place orders, and admins have a complete dashboard to view, search, and update them.

I added new domain models, Order, Recipient, OrderItem, and OrderStatus, to handle delivery details, product snapshots, and order status tracking. The OrderService takes care of stock validation, total calculation, and saving all relationships correctly.

Both users and admins can interact with the new REST API endpoints. Users can create and view their orders, while admins can list all orders and change their statuses. The admin web interface now includes search, filters, and detailed order views with a clean Bootstrap layout.

All endpoints are secured, role-based, and tested successfully.
Current status: Order management complete, next step is integrating Stripe payments and email notifications.
___________________________________________________________________________

I added online payments using Stripe and automatic emails for order confirmations, payments, and deliveries.

The app now uses the Stripe Java SDK for handling payments and Spring Mail for sending emails through Gmail. When a user places an order, the system creates a Stripe PaymentIntent and sends a confirmation email. Once the payment goes through, a Stripe webhook updates the order status to PAID and sends another email to confirm it.

Admins also trigger emails when updating order statuses, like when an order gets delivered. Everything runs smoothly in test mode, and it’s ready for production once the frontend checkout page is connected.

Current status: Payments and email features done, next up: frontend Stripe checkout and cloud deployment.





