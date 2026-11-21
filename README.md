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

Contact-form submissions are saved and emailed to the profile email address.
For Gmail delivery, enable 2-Step Verification and create an App Password, then
set the password in the same PowerShell window before starting the backend. The
sender defaults to `rajaraoeedi20@gmail.com`; set `MAIL_USERNAME` only if you
want to use a different SMTP account:

```powershell
$env:MAIL_PASSWORD = "your-16-character-app-password"
cd backend
mvn spring-boot:run
```

Do not commit SMTP credentials. Other SMTP providers can be configured with
`MAIL_HOST` and `MAIL_PORT` as well.

When deploying, add `MAIL_PASSWORD` to the **backend** service's environment
variables (not the frontend). The default `MAIL_USERNAME` is
`rajaraoeedi20@gmail.com`; override it only if it is not the mailbox authorized
to send through your SMTP provider. Contact-form
messages are delivered to `portfolio.profile.email` in
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
  cannot run the Spring Boot backend, so this hosted version shows portfolio
  data bundled with the frontend and provides an email link instead of the
  submission form. Deploy the backend separately to enable form submissions.
- **Frontend:** `npm run build` → host `dist/` on Netlify / Vercel / S3.
  Set `VITE_API_URL` to your backend URL before building.
- **Backend:** `mvn clean package` → `java -jar target/portfolio-1.0.0.jar`
  (or Render / Railway / AWS). Add your site URL to `portfolio.cors-origins`.
- **Production DB:** uncomment the MySQL dependency in `pom.xml` and change the
  `spring.datasource` settings in `application.yml`.
