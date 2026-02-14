import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function BecomeReporter() {
  const navigate = useNavigate();

  const handleBecomeReporter = async () => {
    try {
      const token = localStorage.getItem("token");

      const res = await axios.put(
        "http://localhost:8080/api/auth/become-reporter",
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (res.data.success) {
        // replace old token with new one
        localStorage.setItem("token", res.data.data.token);

        navigate("/profile");
      }
    } catch (err) {
      alert(err.response?.data?.message || "Action failed");
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-64px)] px-4">
      <div className="bg-white shadow-xl rounded-3xl p-8 w-full max-w-md text-center">
        
        <h2 className="text-2xl font-bold mb-2">
          Become a Reporter
        </h2>

        <p className="text-gray-600 mb-6">
          Upgrade your account to reporter and start publishing news.
        </p>

        <button
          onClick={handleBecomeReporter}
          className="w-full bg-blue-600 text-white p-3 rounded-xl font-semibold hover:bg-blue-700 transition"
        >
          Become Reporter
        </button>
      </div>
    </div>
  );
}
