/* eslint-disable react-hooks/error-boundaries */
import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

export default function ReporterRoute({ children }) {
  try {
    const token = localStorage.getItem("token");
    const decoded = jwtDecode(token);

    if (decoded.role === "REPORTER") {
      return children;
    }

    return <Navigate to="/" replace />;
  } catch {
    return <Navigate to="/" replace />;
  }
}
