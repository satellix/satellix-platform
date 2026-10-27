# ADR-001: Splitting Satellix platform into 3 separate microservices

Status: accepted\
Date: 27-10-2026

## Context

The subjects dictates that we must use microservice architecture. The following split of
responsibilities allows us to avoid tight coupling.

## Decision

We introduce Identitiy/Profile service, Satellite tracking and prediction service and Notification service.
Each microservice has its own database and a OPENAPI contract. Current plan is to use Keycloak for authentication,
PostgreSQL for the database of each of the microservices.

Identity/Profile service's db hold the user information.
Satellite tracking and prediction service's database holds information about all the satellites and predictions.
Notification service's db holds information of current notification requests, a periodical worker visits the unfinished entries and sends the notification.

## Consequences

\+ Clear separation of concerns\
\+ Independant deployment of individual microservices\
\+ Easier unit tests

\- Higher operational complexity\
\- Harder CI/CD
