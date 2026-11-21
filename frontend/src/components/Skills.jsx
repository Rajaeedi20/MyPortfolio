export default function Skills({ skills }) {
  return (
    <section id="skills" className="section alt">
      <div className="container">
        <h3 className="title" data-reveal>Technical skills</h3>
        <div className="skill-grid">
          {skills.map((g) => (
            <div className="card" key={g.id} data-reveal>
              <h4>{g.category}</h4>
              <div className="chips">
                {g.items.split(",").map((s) => (
                  <span className="chip" key={s}>{s}</span>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
