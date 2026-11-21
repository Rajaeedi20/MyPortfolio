import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// In dev, /api calls are proxied to the Spring Boot server.
export default defineConfig({
  base: process.env.GITHUB_ACTIONS === "true" ? "/MyPortfolio/" : "/",
  plugins: [react()],
  server: {
    port: 5173,
    proxy: { "/api": "http://localhost:8080" },
  },
});
