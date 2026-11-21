# Rajarao Eedi – Full Stack Portfolio

Spring Boot 3 (Java 17) REST API + React 18 (Vite) front-end.

```
portfolio/
├── backend/    Spring Boot API  (port 8080)
└── frontend/   React app        (port 5173)
```

## Run locally

**Prerequisites:** JDK 17+, Maven 3.9+, Node 18+

```bash
# 1) Backend
cd backend
mvn spring-boot:run

# 2) Frontend (new terminal)
cd frontend
npm install
npm run dev
```
Open http://localhost:5173

## API

| Method | Endpoint          | Description                     |
|--------|-------------------|---------------------------------|
| GET    | /api/profile      | Name, title, links, summary     |
| GET    | /api/experience   | Work history (newest first)     |
| GET    | /api/projects     | Featured projects               |
| GET    | /api/skills       | Skill groups                    |
| POST   | /api/contact      | Save and email a contact message |

Contact-form submissions are saved and emailed to the profile email address
through the Gmail API. Create Google OAuth credentials and a refresh token
authorized for the `https://www.googleapis.com/auth/gmail.send` scope. The
authorized Google account must match the profile email. Set these values before
starting the backend:

```powershell
$env:GMAIL_CLIENT_ID = "your-oauth-client-id"
$env:GMAIL_CLIENT_SECRET = "your-oauth-client-secret"
$env:GMAIL_REFRESH_TOKEN = "your-oauth-refresh-token"
cd backend
mvn spring-boot:run
```

To obtain the credentials:

1. Enable the Gmail API in a [Google Cloud project](https://console.cloud.google.com/).
2. Configure the OAuth consent screen and create a **Web application** OAuth
   client. Add `https://developers.google.com/oauthplayground` as an authorized
   redirect URI.
3. In the [OAuth 2.0 Playground](https://developers.google.com/oauthplayground/),
   enable **Use your own OAuth credentials**, enter the client ID and secret,
   authorize the scope `https://www.googleapis.com/auth/gmail.send`, and
   exchange the code for tokens.
4. Put the client ID, secret, and refresh token in the backend environment.

The authorized Google account must match the profile email. Keep all OAuth
values private. OAuth apps left in Testing mode issue refresh tokens that expire
after seven days; publish the consent screen to In production for a persistent
deployment. Google may show an unverified-app warning during personal use.

Never commit OAuth credentials or refresh tokens.

When deploying, add `GMAIL_CLIENT_ID`, `GMAIL_CLIENT_SECRET`, and
`GMAIL_REFRESH_TOKEN` to the **backend** service's environment variables (not
the frontend). Contact messages are delivered to `portfolio.profile.email` in
`backend/src/main/resources/application.yml`. Redeploy/restart the backend
after setting these values.

## Customising
- **Profile text & links:** `backend/src/main/resources/application.yml`
- **Experience, projects, skills:** `backend/.../config/DataSeeder.java`
- **Photo:** replace `frontend/public/photo.jpg`
- **Colours:** CSS variables at the top of `frontend/src/styles.css`

## Deploy
- **GitHub Pages:** In the repository's **Settings → Pages**, set the source
  to **GitHub Actions**. The workflow deploys the static portfolio to
  `https://rajaeedi20.github.io/MyPortfolio/` on pushes to `main`. GitHub Pages
  cannot run the Spring Boot backend, so the backend must be deployed separately
  to enable form submissions. The Pages workflow reads the API URL from the
  `VITE_API_URL` repository variable.
- **Backend on Render:** Create a Blueprint from this repository using
  `render.yaml`, then add the Google OAuth credentials in the backend service's
  environment settings. Add the
  deployed backend URL (without a trailing slash) as the `VITE_API_URL`
  repository variable under **Settings → Secrets and variables → Actions**,
  then rerun the Pages workflow.
- **Frontend:** `npm run build` → host `dist/` on Netlify / Vercel / S3.
  Set `VITE_API_URL` to your backend URL before building.
- **Backend:** `mvn clean package` → `java -jar target/portfolio-1.0.0.jar`
  (or Render / Railway / AWS). Add your site URL to `portfolio.cors-origins`.
- **Production DB:** uncomment the MySQL dependency in `pom.xml` and change the
  `spring.datasource` settings in `application.yml`.
