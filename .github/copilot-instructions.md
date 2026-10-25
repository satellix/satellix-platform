# Satellix — GitHub Copilot Instructions

## Project Context

Satellix is a satellite tracking and prediction platform developed as a university software engineering project.

The application uses a microservice-oriented architecture.

The planned MVP consists of three services:

1. **Identity Service** — Authentication, authorization, and user management.
2. **Satellite Service** — Communication with external satellite data providers, satellite tracking, and pass predictions.
3. **Notification Service** — User subscriptions and notifications for events such as upcoming satellite passes over selected locations.

The project emphasizes software engineering best practices, maintainability, testability, and clear architectural boundaries.

## Code Review Guidelines

When reviewing pull requests:

### Correctness

- Identify potential bugs, incorrect assumptions, and unhandled edge cases.
- Check error handling and input validation.
- Look for concurrency issues and potential race conditions where applicable.

### Architecture

- Verify that changes respect microservice boundaries.
- Flag unnecessary coupling between services.
- Check that service responsibilities remain clearly separated.
- Prefer well-defined API contracts over dependencies on another service's internal implementation.

### Security

- Identify hardcoded credentials, secrets, and API keys.
- Check authentication and authorization logic.
- Identify insecure handling of user data.
- Review external API calls for security and reliability risks.

### API Design

- Check consistency with the OpenAPI specification.
- Review HTTP methods, status codes, request validation, and error responses.
- Flag unintended breaking changes to public API contracts.

### Maintainability

- Prefer simple, readable, and maintainable implementations.
- Identify unnecessary complexity and duplication.
- Flag violations of established project conventions.

### Testing

- Check whether new behavior is covered by meaningful automated tests.
- Look for missing negative tests and boundary cases.
- Identify tests that do not actually verify their intended behavior.

## Review Behavior

- Prioritize correctness, security, architecture, and maintainability over stylistic preferences.
- Focus on actionable problems introduced by the pull request.
- Explain why an identified problem matters.
- Suggest concrete improvements where possible.
- Avoid speculative issues without supporting evidence.
- Do not request unnecessary abstractions or enterprise-level complexity for this MVP.
- Do not flag intentional architectural decisions merely because alternatives exist.
- Keep feedback concise and constructive.
