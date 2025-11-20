# CyberEd Platform - Architecture Analysis

**Course:** Software Design Techniques  
**Team Members:**
- Petre George-Alexandru, group 1241EB
- Ionescu Rares-Andrei, group 1241EB
- Leonte Robert, group 1241EB

**Date:** November 20, 2025

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture 1: Monolithic Architecture](#architecture-1-monolithic-architecture)
3. [Architecture 2: Microservices Architecture](#architecture-2-microservices-architecture)
4. [Architecture 3: Event-Driven Architecture](#architecture-3-event-driven-architecture)
5. [Architecture Comparison](#architecture-comparison)
6. [Recommendation](#recommendation)

---

## Project Overview

### CyberEd Platform

The **CyberEd Platform** is a comprehensive web-based educational system for cybersecurity learning with the following core features:

**Core Functionalities:**
- Course Management System
- Interactive Challenge System
- E-Commerce and Shopping Cart
- Community Forum
- User Management and Authentication
- Administrative Dashboard

**Technical Stack:**
- Backend: Spring Boot 3.4.5, Spring Security 6, Spring Data JPA
- Frontend: Thymeleaf
- Database: MariaDB 11
- Containerization: Docker
- Build Tool: Maven

---

## Architecture 1: Monolithic Architecture

### Overview

<!-- Add monolithic architecture overview here -->

### Structure

<!-- Describe the layered structure: Presentation, Business Logic, Data Access -->

### Component Diagram

<!-- Insert monolithic component diagram here -->

![Monolithic Component Diagram](diagrams/monolithic-component.png)

### Deployment Diagram

<!-- Insert monolithic deployment diagram here -->

![Monolithic Deployment Diagram](diagrams/monolithic-deployment.png)

### Advantages

<!-- List monolithic architecture advantages -->

### Disadvantages

<!-- List monolithic architecture disadvantages -->

### Suitability for CyberEd Platform

<!-- Analyze how well monolithic architecture fits the project -->

---

## Architecture 2: Microservices Architecture

### Overview

<!-- Add microservices architecture overview here -->

### Structure

<!-- Describe the service decomposition and communication patterns -->

### Component Diagram

<!-- Insert microservices component diagram here -->

![Microservices Component Diagram](diagrams/microservices-component.png)

### Deployment Diagram

<!-- Insert microservices deployment diagram here -->

![Microservices Deployment Diagram](diagrams/microservices-deployment.png)

### Advantages

<!-- List microservices architecture advantages -->

### Disadvantages

<!-- List microservices architecture disadvantages -->

### Suitability for CyberEd Platform

<!-- Analyze how well microservices architecture fits the project -->

---

## Architecture 3: Event-Driven Architecture

### Overview

Event-Driven Architecture is a pattern where system components communicate asynchronously through events rather than direct calls. Event producers publish events to an event broker, and event consumers subscribe to and process these events independently.

Key components:
- Event Producers: Business services that publish events
- Event Broker: Apache Kafka cluster for event routing
- Event Consumers: Specialized processors that react to events

### Structure

#### Event Producers

Producer services handle user requests and publish events when state changes occur:

**User Service**
- Events: UserRegistered, UserLoggedIn, ProfileUpdated
- Database: PostgreSQL

**Course Service**
- Events: CourseCreated, CourseUpdated, InventoryChanged
- Database: PostgreSQL

**Payment Service**
- Events: PaymentCompleted, PaymentFailed, RefundProcessed
- Database: PostgreSQL

**Challenge Service**
- Events: ChallengeStarted, ChallengeCompleted, SubmissionGraded
- Database: MongoDB

**Forum Service**
- Events: PostCreated, CommentAdded, PostModerated
- Database: PostgreSQL

#### Event Broker

Apache Kafka cluster with:
- 3 broker nodes for high availability
- Topics organized by domain
- 7-day event retention
- Throughput: 50,000+ messages/second

#### Event Consumers

**Notification Processor**
- Sends emails, push notifications
- Database: MongoDB

**Inventory Processor**
- Updates course stock
- Database: PostgreSQL

**Analytics Processor**
- Real-time metrics and dashboards
- Database: ClickHouse, Redis

**Recommendation Processor**
- Generates personalized recommendations
- Database: PostgreSQL

**Audit Processor**
- Writes immutable audit logs
- Database: PostgreSQL

### Event Flow Example

Course purchase flow:
1. User submits purchase
2. Payment Service processes payment synchronously
3. Payment Service publishes PaymentCompleted event
4. User receives response (300-500ms)
5. Consumers process event in parallel:
   - Notification: Send email
   - Inventory: Update stock
   - Analytics: Update metrics
   - Audit: Log transaction

### Component Diagram

![Event-Driven Component Diagram](diagrams/event-driven-component.png)

### Deployment Diagram

![Event-Driven Deployment Diagram](diagrams/event-driven-deployment.png)

### Advantages

**Loose Coupling**
- Producers don't know about consumers
- Easy to add new consumers without modifying producers
- Independent development and deployment

**Scalability**
- Consumers scale independently based on load
- Parallel event processing
- Fast user response times (300ms vs 2-3 seconds)

**Fault Tolerance**
- No cascading failures
- Event persistence allows recovery
- Automatic retry mechanisms

**Asynchronous Processing**
- Non-blocking operations
- Background processing for long-running tasks
- Improved user experience

**Audit Trail**
- Complete event history
- Event replay capability
- Time travel debugging

**Extensibility**
- Easy to add new features
- A/B testing support
- Gradual feature rollout

### Disadvantages

**Eventual Consistency**
- Data may be stale for 2-5 seconds
- Complex conflict resolution needed
- Problematic for transactional operations

**Increased Complexity**
- Distributed system patterns required
- Difficult debugging across multiple services
- Steep learning curve for team

**Infrastructure Overhead**
- Kafka cluster management required
- Kubernetes orchestration complexity
- Extensive monitoring needed

**High Costs**
- Infrastructure: $8,000/month vs $55/month for monolithic
- Requires dedicated DevOps team
- 145x more expensive at small scale

**Message Duplication**
- At-least-once delivery guarantees
- All consumers must be idempotent
- Complex deduplication logic required

**Schema Evolution**
- Breaking changes affect multiple consumers
- Version management complexity
- Coordination overhead between teams

**Testing Complexity**
- Async testing challenges
- Complex test setup requirements
- Timing-dependent, flaky tests

### Suitability for CyberEd Platform

**Not Recommended for Current Stage**

Event-driven architecture is not suitable for CyberEd Platform's current needs:

- Scale mismatch: Optimized for 10,000+ concurrent users, platform has 100-1,000 users
- Team capacity: 3 developers lack distributed systems expertise
- Consistency needs: Core features require ACID transactions
- Cost: $8,000/month vs $55/month for monolithic (145x more expensive)
- Feature fit: 80% of features need strong consistency, only 20% benefit from async

**Future Consideration**

Event-driven becomes viable when:
- Scale reaches 5,000+ concurrent users
- Processing 10,000+ transactions/day
- Team has 10+ developers with distributed systems expertise
- Budget supports infrastructure costs

---

## Architecture Comparison

### Comparison Criteria

<!-- Add comparison table here comparing all three architectures across multiple dimensions -->

### Scalability Analysis

<!-- Compare scalability characteristics -->

### Complexity Assessment

<!-- Compare development and operational complexity -->

### Cost Analysis

<!-- Compare infrastructure and operational costs at different scales -->

### Performance Comparison

<!-- Compare response times and throughput -->

### Consistency Models

<!-- Compare data consistency guarantees -->

### Deployment and Operations

<!-- Compare deployment processes and operational overhead -->

### Team Size and Skills

<!-- Compare team requirements and skill sets needed -->

---

## Recommendation

### Recommended Architecture: [TO BE DETERMINED]

<!-- Add final recommendation with detailed justification -->

### Justification

<!-- Explain why the recommended architecture best fits CyberEd Platform's needs -->


---

## References

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Event-Driven Architecture Patterns](https://martinfowler.com/articles/201701-event-driven.html)
- [Microservices Patterns by Chris Richardson](https://microservices.io/patterns/)
- [Building Event-Driven Microservices by Adam Bellemare](https://www.oreilly.com/library/view/building-event-driven-microservices/9781492057888/)

---

**Last Updated:** November 20, 2025
