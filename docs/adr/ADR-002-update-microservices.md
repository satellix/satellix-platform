# ADR-002: Removing the user management microservice in favor of a cleaner 2-microservice approach

Status: accepted\
Supersedes: ADR-001\
Date: 09-11-2026

## Context

After individual consultations with the TA we have reached the conclusion that a 2-microservice design is a better
fit for the Satellix platform.

## Decision

We are dropping the user managment microservice and keeping the existing satellite tracking and predictions service as
well as the notification service. Instead of user accounts the notifications will simply require an email address.

## Consequences

\+ Easier design\
\+ Better horizontal scaling\
\+ Easier unit tests

\- Possibly open to misuse (if rate limiting not properly implemented)\
\- Harder rate limiting
