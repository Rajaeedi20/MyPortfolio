export default function About({ profile }) {
  const stats = [
    ["3+", "Years experience"],
    ["20+", "REST APIs built"],
    ["1,000+", "Platform users"],
    ["40%", "Faster releases"],
  ];
  return (
    <section id="about" className="section">
      <div className="container">
        <h3 className="title" data-reveal>About me</h3>
        <p className="about-text" data-reveal>{profile.summary}</p>
        <div className="stats">
          {stats.map(([n, l]) => (
            <div className="stat" key={l} data-reveal>
              <strong>{n}</strong>
              <span>{l}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
