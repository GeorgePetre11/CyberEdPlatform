# ⚠️ BRANCH PROTECTION NOTICE

**This is the Milestone 4 submission branch - DO NOT MERGE TO MAIN**

This branch contains the complete Milestone 4 microservices implementation and should remain separate from the main branch.

## Purpose

This branch demonstrates:
- Microservices architecture decomposition
- Inter-service communication via REST APIs
- Docker containerization
- Complete implementation of 3 independent services

## Branch Structure

- **Branch Name:** `milestone4-microservices`
- **Status:** Submission/Archive Branch
- **Merge Policy:** NOT TO BE MERGED

## Why Not Merge?

This branch represents a complete architectural shift from monolithic to microservices. It should be kept separate to:

1. Preserve the original monolithic implementation in `main`
2. Allow easy comparison between architectures
3. Serve as a standalone milestone submission
4. Maintain clear project history

## To Review This Milestone

```bash
git checkout milestone4-microservices
cd CyberEdPlatform-Microservices
docker-compose up --build
```

Then visit:
- User Service: http://localhost:8081
- Course Service: http://localhost:8082
- Order Service: http://localhost:8083

---

**For questions, contact the team:**
- Petre George-Alexandru
- Ionescu Rares-Andrei
- Leonte Robert
