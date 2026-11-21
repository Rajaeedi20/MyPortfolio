export default function Experience({ items }) {
  return (
    <section id="experience" className="section">
      <div className="container">
        <h3 className="title" data-reveal>Experience</h3>
        <div className="timeline">
          {items.map((e) => (
            <article className={`tl-item ${e.current ? "current" : ""}`} key={e.id} data-reveal>
              <span className="dot" />
              <div className="card">
                <div className="tl-head">
                  <div>
                    <h4>{e.role}</h4>
                    <p className="company">{e.company} · {e.location}</p>
                  </div>
                  <span className="period">
                    {e.period}
                    {e.current && <em className="now">Current</em>}
                  </span>
                </div>
                <p className="domain">{e.domain}</p>
                <ul>
                  {e.highlights.map((h, i) => <li key={i}>{h}</li>)}
                </ul>
                <div className="chips">
                  {e.tech.split(",").map((t) => <span className="chip sm" key={t}>{t}</span>)}
                </div>
              </div>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
