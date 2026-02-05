import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Templates() {
  const [templates, setTemplates] = useState([]);

  useEffect(() => {
    api.get("/templates").then((res) => setTemplates(res.data));
  }, []);

  return (
    <div style={{ padding: 40 }}>
      <h2>Templates</h2>
      {templates.map((t) => (
        <div key={t.id} style={{ border: "1px solid #ccc", margin: 10, padding: 10 }}>
          <h4>{t.name}</h4>
          <button>Use Template</button>
        </div>
      ))}
    </div>
  );
}