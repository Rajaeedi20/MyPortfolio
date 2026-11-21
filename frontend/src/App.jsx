import { useEffect, useState } from "react";
import { getProfile, getExperience, getProjects, getSkills } from "./api";
import Navbar from "./components/Navbar.jsx";
import Hero from "./components/Hero.jsx";
import About from "./components/About.jsx";
import Skills from "./components/Skills.jsx";
import Experience from "./components/Experience.jsx";
import Projects from "./components/Projects.jsx";
import Contact from "./components/Contact.jsx";

export default function App() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getProfile(), getExperience(), getProjects(), getSkills()])
      .then(([profile, experience, projects, skills]) =>
        setData({ profile, experience, projects, skills })
      )
      .catch(() => setError("Could not reach the API. Is the Spring Boot server running on port 8080?"));
  }, []);

  useEffect(() => {
    if (!data) return undefined;

    const targets = document.querySelectorAll("[data-reveal]");
    targets.forEach((target) => target.classList.add("reveal"));

    if (!("IntersectionObserver" in window)) {
      targets.forEach((target) => target.classList.add("is-visible"));
      return undefined;
    }

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("is-visible");
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.12 }
    );

    targets.forEach((target) => observer.observe(target));
    return () => observer.disconnect();
  }, [data]);

  if (error) return <div className="status">{error}</div>;
  if (!data) return <div className="status">Loading portfolio…</div>;

  const { profile, experience, projects, skills } = data;
  return (
    <>
      <Navbar name={profile.name} />
      <main>
        <Hero profile={profile} />
        <About profile={profile} />
        <Skills skills={skills} />
        <Experience items={experience} />
        <Projects items={projects} />
        <Contact profile={profile} />
      </main>
      <footer className="footer">
        © {new Date().getFullYear()} {profile.name} · Built with Spring Boot &amp; React
      </footer>
    </>
  );
}
