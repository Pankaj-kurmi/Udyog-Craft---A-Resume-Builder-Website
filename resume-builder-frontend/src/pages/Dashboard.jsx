import { Link } from "react-router-dom";

export default function Dashboard() {
  return (
    <div style={{ padding: 40 }}>
      <h2>Dashboard</h2>
      <ul>
        <li><Link to="/templates">Browse Templates</Link></li>
        <li><Link to="/payment">Upgrade / Payment</Link></li>
      </ul>
    </div>
  );
}