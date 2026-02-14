import { Link, useNavigate } from "react-router-dom";
import { Menu, X } from "lucide-react";
import { useState } from "react";
import { jwtDecode } from "jwt-decode";

export default function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const [open, setOpen] = useState(false);

  let role = null;

  if (token) {
    try {
      const decoded = jwtDecode(token);
      role = decoded.role;
    } catch {
      role = null;
    }
  }

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/login");
    setOpen(false);
  };

  return (
    <>
      {/* Top Navbar */}
      <nav className="fixed top-0 left-0 w-full bg-white shadow-md z-50">
        <div className="max-w-6xl mx-auto px-4 py-5 flex justify-between items-center">
          <Link to="/" className="text-xl font-bold text-blue-600">
            NewsPortal
          </Link>

          {/* Desktop menu */}
          <div className="hidden md:flex space-x-6 items-center">
            <Link to="/" className="text-gray-700 hover:text-blue-600">
              Home
            </Link>

            {!token ? (
              <>
                <Link to="/login" className="text-gray-700 hover:text-blue-600">
                  Login
                </Link>
                <Link to="/signup" className="text-gray-700 hover:text-blue-600">
                  Signup
                </Link>
              </>
            ) : (
              <>
                {/* All News for logged-in users */}
                <Link
                  to="/news"
                  className="text-gray-700 hover:text-blue-600"
                >
                  All News
                </Link>

                <Link
                  to="/profile"
                  className="text-gray-700 hover:text-blue-600"
                >
                  Profile
                </Link>

                {/* CUSTOMER */}
                {role === "CUSTOMER" && (
                  <Link
                    to="/become-reporter"
                    className="text-blue-600 font-semibold hover:text-blue-700"
                  >
                    Become Reporter
                  </Link>
                )}

                {/* REPORTER */}
                {role === "REPORTER" && (
                  <Link
                    to="/reporter"
                    className="text-blue-600 font-semibold hover:text-blue-700"
                  >
                    Reporter Dashboard
                  </Link>
                )}

                <button
                  onClick={logout}
                  className="text-red-500 font-semibold"
                >
                  Logout
                </button>
              </>
            )}
          </div>

          {/* Mobile hamburger */}
          <button
            className="md:hidden"
            onClick={() => setOpen(true)}
          >
            <Menu size={28} />
          </button>
        </div>
      </nav>

      {/* Sidebar overlay */}
      {open && (
        <div
          className="fixed inset-0 bg-black/40 z-40"
          onClick={() => setOpen(false)}
        />
      )}

      {/* Sidebar */}
      <div
        className={`fixed top-0 right-0 h-full w-64 bg-white shadow-lg z-50 transform transition-transform duration-300 ${
          open ? "translate-x-0" : "translate-x-full"
        }`}
      >
        <div className="flex justify-between items-center p-4 border-b">
          <span className="text-lg font-bold">Menu</span>
          <button onClick={() => setOpen(false)}>
            <X size={28} />
          </button>
        </div>

        <div className="flex flex-col p-4 space-y-4">
          <Link
            to="/"
            onClick={() => setOpen(false)}
            className="text-gray-700 hover:text-blue-600"
          >
            Home
          </Link>

          {!token ? (
            <>
              <Link
                to="/login"
                onClick={() => setOpen(false)}
                className="text-gray-700 hover:text-blue-600"
              >
                Login
              </Link>
              <Link
                to="/signup"
                onClick={() => setOpen(false)}
                className="text-gray-700 hover:text-blue-600"
              >
                Signup
              </Link>
            </>
          ) : (
            <>
              {/* All News */}
              <Link
                to="/news"
                onClick={() => setOpen(false)}
                className="text-gray-700 hover:text-blue-600"
              >
                All News
              </Link>

              <Link
                to="/profile"
                onClick={() => setOpen(false)}
                className="text-gray-700 hover:text-blue-600"
              >
                Profile
              </Link>

              {/* CUSTOMER */}
              {role === "CUSTOMER" && (
                <Link
                  to="/become-reporter"
                  onClick={() => setOpen(false)}
                  className="text-blue-600 font-semibold"
                >
                  Become Reporter
                </Link>
              )}

              {/* REPORTER */}
              {role === "REPORTER" && (
                <Link
                  to="/reporter"
                  onClick={() => setOpen(false)}
                  className="text-blue-600 font-semibold"
                >
                  Reporter Dashboard
                </Link>
              )}

              <button
                onClick={logout}
                className="text-left text-red-500 font-semibold"
              >
                Logout
              </button>
            </>
          )}
        </div>
      </div>

      {/* Spacer for fixed navbar */}
      <div className="h-16" />
    </>
  );
}

