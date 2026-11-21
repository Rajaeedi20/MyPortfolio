export default function Hero({ profile }) {
  return (
    <section id="top" className="hero">
      <div className="container hero-grid">
        <div data-reveal>
          <p className="eyebrow">Hello, I'm</p>
          <h1>{profile.name}</h1>
          <h2>
            {profile.title} <span className="at">@ {profile.company}</span>
          </h2>
          <p className="lead">{profile.tagline}</p>
          <div className="cta">
            <a className="btn primary" href="#contact">Get in touch</a>
            <a className="btn" href="#projects">View work</a>
            <a className="btn ghost" href={profile.github} target="_blank" rel="noreferrer">GitHub</a>
            <a className="btn ghost" href={profile.linkedin} target="_blank" rel="noreferrer">LinkedIn</a>
          </div>
        </div>
        <div className="photo-wrap" data-reveal>
          <img src={`${import.meta.env.BASE_URL}photo.jpg`} alt={profile.name} className="photo" />
          <span className="badge">Open to collaborate</span>
        </div>
      </div>
    </section>
  );
}
