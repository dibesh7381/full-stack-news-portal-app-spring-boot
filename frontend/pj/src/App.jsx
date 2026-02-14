import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile";
import BecomeReporter from "./pages/BecomeReporter";
import ReporterDashboard from "./pages/ReporterDashboard";
import AllNews from "./pages/AllNews";
import Navbar from "./components/Navbar";

import PrivateRoute from "./routes/PrivateRoute";
import CustomerRoute from "./routes/CustomerRoute";
import ReporterRoute from "./routes/ReporterRoute";

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-100">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />

          <Route
            path="/profile"
            element={
              <PrivateRoute>
                <Profile />
              </PrivateRoute>
            }
          />

          <Route
            path="/become-reporter"
            element={
              <PrivateRoute>
                <CustomerRoute>
                  <BecomeReporter />
                </CustomerRoute>
              </PrivateRoute>
            }
          />

          {/* All News (for logged-in users) */}
          <Route
            path="/news"
            element={
              <PrivateRoute>
                <AllNews />
              </PrivateRoute>
            }
          />

          {/* Reporter Dashboard */}
          <Route
            path="/reporter"
            element={
              <PrivateRoute>
                <ReporterRoute>
                  <ReporterDashboard />
                </ReporterRoute>
              </PrivateRoute>
            }
          />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;


