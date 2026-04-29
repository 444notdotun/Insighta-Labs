# Insighta Labs — 
 
A GitHub OAuth-powered intelligence query platform with role-based access control, JWT authentication, rate limiting, and a CLI + web portal interface.
 
**Live Backend:** https://insighta-labs-183135031185.us-central1.run.app  
**GitHub:** https://github.com/444notdotun/Insighta-Labs
 
---
 
## Tech Stack
 
- Java 21, Spring Boot
- PostgreSQL (AlwaysData)
- JWT (3 min access token) + Refresh Token (5 min) with rotation
- GitHub OAuth 2.0 + PKCE
- Docker, Google Cloud Run
- Node.js CLI
- React Web Portal
---
 
## Authentication Flow (OAuth 2.0 + PKCE)
 
1. Client generates a `code_verifier` and `code_challenge` (SHA-256)
2. Client calls `GET /auth/github` with the `code_challenge`
3. User is redirected to GitHub to authorize
4. GitHub redirects back to `/auth/github/callback` with a `code`
5. Client sends `code` + `code_verifier` to the callback endpoint
6. Server exchanges with GitHub, creates/retrieves user, returns JWT + refresh token
7. On expiry, client calls `POST /auth/refresh` with the refresh token to get a new pair
---
 
## Environment Variables
 
| Variable | Description |
|---|---|
| `POSTGRES_URL` | PostgreSQL JDBC connection URL |
| `POSTGRES_USERNAME` | Database username |
| `POSTGRES_PASSWORD` | Database password |
| `JWT_SECRET_KEY` | Base64-encoded JWT signing secret |
| `JWT_EXPIRATION_TIME` | JWT expiry in milliseconds (180000 = 3 min) |
| `CLIENT_ID` | GitHub OAuth App Client ID |
| `CLIENT_SECRET` | GitHub OAuth App Client Secret |
| `GITHUB_REDIRECT_URI` | GitHub OAuth callback URL |
 
---
 
## API Endpoints
 
### Auth
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/auth/github` | Get GitHub OAuth redirect URL | No |
| GET | `/auth/github/callback` | Handle GitHub OAuth callback | No |
| POST | `/auth/refresh` | Refresh access token | No |
 
### Profiles
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/profiles` | Get all profiles (paginated) | Yes |
| GET | `/api/profiles/{id}` | Get profile by ID | Yes |
| POST | `/api/profiles` | Create profile | Yes (ADMIN) |
| DELETE | `/api/profiles/{id}` | Delete profile | Yes (ADMIN) |
| GET | `/api/profiles/export` | Export profiles as CSV | Yes |
 
### Rate Limits
- Auth endpoints: 10 requests/minute
- API endpoints: 60 requests/minute
---
 
## Roles
 
| Role | Access |
|---|---|
| `ANALYST` | Default role on registration. Read access to profiles. |
| `ADMIN` | Full access including create and delete. |
 
Role is embedded in the JWT — no DB hit required on every request.
 
---
 
## Running Locally
 
**Prerequisites:** Java 21, Maven, PostgreSQL
 
```bash
git clone https://github.com/444notdotun/Insighta-Labs.git
cd Insighta-Labs/insighta-labs
```
 
Create a `.env` file or set the following environment variables:
 
```env
POSTGRES_URL=jdbc:postgresql://localhost:5432/intelligence_db
POSTGRES_USERNAME=your_username
POSTGRES_PASSWORD=your_password
JWT_SECRET_KEY=your_base64_secret
JWT_EXPIRATION_TIME=180000
CLIENT_ID=your_github_client_id
CLIENT_SECRET=your_github_client_secret
GITHUB_REDIRECT_URI=http://localhost:8080/auth/github/callback
```
 
```bash
mvn spring-boot:run
```
 
---
 
## Running with Docker
 
```bash
docker build -t insighta-labs .
docker run -p 8080:8080 \
  -e POSTGRES_URL=your_url \
  -e POSTGRES_USERNAME=your_username \
  -e POSTGRES_PASSWORD=your_password \
  -e JWT_SECRET_KEY=your_secret \
  -e JWT_EXPIRATION_TIME=180000 \
  -e CLIENT_ID=your_client_id \
  -e CLIENT_SECRET=your_client_secret \
  -e GITHUB_REDIRECT_URI=your_redirect_uri \
  insighta-labs
```
 
---
 
## CLI Usage
 
```bash
# Login via GitHub OAuth
insighta login
 
# Query profiles
insighta query --filter "name=John"
 
# Export profiles to CSV
insighta export --output profiles.csv
 
# Check current user
insighta whoami
 
# Logout
insighta logout
```
 
---
 
## Web Portal
 
The React web portal provides a dashboard for querying and managing profiles.
 
- Login via GitHub OAuth
- View and search profiles
- Export data as CSV
- Role-based UI (ADMIN sees additional controls)
---
 
## CI/CD
 
GitHub Actions pipeline runs on every push to `main`:
- Builds the project with Maven
- Runs tests
- Validates Docker build
Pipeline config: `.github/workflows/ci.yml`
 
---
 
## Security
 
- PKCE prevents authorization code interception attacks
- JWT is stateless — role verified from token claim, no DB hit per request
- Refresh tokens stored in PostgreSQL with `isRevoked` flag for audit trail
- Rate limiting via `ConcurrentHashMap` — per IP, per endpoint type
- `userId` always extracted from JWT, never trusted from request body
---
 
## Author
 
**Adedotun Adewole Stephen**  
Software Engineering Intern — Semicolon Africa, Lagos  
GitHub: [444notdotun](https://github.com/444notdotun)
