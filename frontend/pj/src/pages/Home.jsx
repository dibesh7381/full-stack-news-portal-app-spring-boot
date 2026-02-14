import { useEffect, useState } from "react";
import axios from "axios";

export default function Home() {
  const [home, setHome] = useState(null);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/auth/home")
      .then((res) => setHome(res.data.data))
      .catch(() => alert("Error loading home"));
  }, []);

  if (!home)
    return (
      <div className="flex items-center justify-center min-h-screen">
        <p>Loading...</p>
      </div>
    );

  return (
    <div className="flex items-center justify-center min-h-screen px-4">
      <div className="bg-white shadow-lg rounded-2xl p-8 max-w-md w-full text-center">
        <h1 className="text-2xl font-bold mb-4">{home.title}</h1>
        <p className="text-gray-600">{home.content}</p>
      </div>
    </div>
  );
}

