import { useEffect, useState } from "react";
import { sendContact } from "../api";

export default function Contact({ profile }) {
  const [form, setForm] = useState({ name: "", email: "", message: "" });
  const [state, setState] = useState({ type: "idle", text: "" });

  useEffect(() => {
    if (state.type !== "ok") return undefined;
    const timer = window.setTimeout(() => setState({ type: "idle", text: "" }), 3000);
    return () => window.clearTimeout(timer);
  }, [state.type]);

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setState({ type: "loading", text: "Sending…" });
    try {
      const res = await sendContact(form);
      const emailSent = res.emailSent === "true";
      setState({
        type: emailSent ? "ok" : "warning",
        text: emailSent ? "Thanks! Your message has been sent to Rajarao Eedi." : res.message,
      });
      setForm({ name: "", email: "", message: "" });
    } catch (err) {
      setState({ type: "error", text: err.message });
    }
  };

  return (
    <section id="contact" className="section">
      <div className="container contact-grid">
        <div data-reveal>
          <h3 className="title">Let's work together</h3>
          <p className="about-text">
            Have a project, an opportunity or just want to say hi? Send a message and I'll reply soon.
          </p>
          <ul className="contact-list">
            <li>✉️ <a href={`mailto:${profile.email}`}>Rajarao Eedi</a></li>
            <li>📞 <a href={`tel:${profile.phone}`}>{profile.phone}</a></li>
            <li>📍 {profile.location}</li>
          </ul>
        </div>

        <form className="card form" onSubmit={submit} data-reveal>
          <label>Name
            <input name="name" value={form.name} onChange={change} required maxLength={100} />
          </label>
          <label>Email
            <input type="email" name="email" value={form.email} onChange={change} required maxLength={150} />
          </label>
          <label>Message
            <textarea name="message" rows="5" value={form.message} onChange={change} required minLength={10} maxLength={2000} />
          </label>
          <button className="btn primary" disabled={state.type === "loading"}>
            {state.type === "loading" ? "Sending…" : "Send message"}
          </button>
          {state.text && <p className={`msg ${state.type}`}>{state.text}</p>}
        </form>
      </div>
    </section>
  );
}
