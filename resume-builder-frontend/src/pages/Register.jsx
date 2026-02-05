import { useState } from "react";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    await api.post("/auth/register", form);
    alert("Registered! Please verify your email.");
    navigate("/");
  };

  return (
    <div style={{ padding: 40 }}>
      <h2>Register</h2>
      <form onSubmit={submit}>
        <input placeholder="Name" onChange={(e) => setForm({ ...form, name: e.target.value })} /><br/>
        <input placeholder="Email" onChange={(e) => setForm({ ...form, email: e.target.value })} /><br/>
        <input type="password" placeholder="Password" onChange={(e) => setForm({ ...form, password: e.target.value })} /><br/>
        <button>Register</button>
      </form>
    </div>
  );
}