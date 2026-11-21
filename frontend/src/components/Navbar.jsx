import { useState } from "react";

const links = ["about", "skills", "experience", "projects", "contact"];

export default function Navbar({ name }) {
  const [open, setOpen] = useState(false);

  return (
    <header className="nav">
      <div className="container nav-inner">
        <a href="#top" className="logo">{name}<span>.</span></a>
        <button className="burger" onClick={() => setOpen(!open)} aria-label="Toggle menu">☰</button>
        <nav className={open ? "open" : ""}>
          {links.map((l) => (
            <a key={l} href={`#${l}`} onClick={() => setOpen(false)}>{l}</a>
          ))}
        </nav>
      </div>
    </header>
  );
}
