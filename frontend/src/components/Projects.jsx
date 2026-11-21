export default function Projects({ items }) {
  return (
    <section id="projects" className="section alt">
      <div className="container">
        <h3 className="title" data-reveal>Featured projects</h3>
        <div className="project-grid">
          {items.map((p) => (
            <div className="card project" key={p.id} data-reveal>
              <p className="company">{p.company}</p>
              <h4>{p.title}</h4>
              <p>{p.description}</p>
              <p className="impact">{p.impact}</p>
              <div className="chips">
                {p.tech.split(",").map((t) => <span className="chip sm" key={t}>{t}</span>)}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
