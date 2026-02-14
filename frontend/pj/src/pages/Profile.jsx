import { useEffect, useState } from "react";
import axios from "axios";

export default function Profile() {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");

    axios
      .get("http://localhost:8080/api/auth/profile", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => setProfile(res.data.data))
      .catch(() => alert("Unauthorized"));
  }, []);

  if (!profile)
    return (
      <div className="flex items-center justify-center min-h-[calc(100vh-64px)]">
        <p>Loading...</p>
      </div>
    );

  return (
    <div className="flex items-center justify-center min-h-[calc(100vh-64px)] px-4">
      <div className="bg-white shadow-xl rounded-3xl p-8 w-full max-w-md text-center">
        
        {/* Avatar */}
        <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-blue-100 flex items-center justify-center text-2xl font-bold text-blue-600">
          {profile.username.charAt(0).toUpperCase()}
        </div>

        <h2 className="text-2xl font-bold mb-1">
          {profile.username}
        </h2>
        <p className="text-gray-500 mb-6">{profile.email}</p>

        {/* Info section */}
        <div className="bg-gray-50 rounded-xl p-4 text-left space-y-3">
          <div className="flex justify-between">
            <span className="text-gray-500">Username</span>
            <span className="font-semibold">{profile.username}</span>
          </div>

          <div className="flex justify-between">
            <span className="text-gray-500">Email</span>
            <span className="font-semibold">{profile.email}</span>
          </div>

          <div className="flex justify-between">
            <span className="text-gray-500">Role</span>
            <span className="font-semibold text-blue-600">
              {profile.role}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
