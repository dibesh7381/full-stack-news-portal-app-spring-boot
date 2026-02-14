/* eslint-disable react-hooks/set-state-in-effect */
import { useEffect, useState, useRef } from "react";

const API = "http://localhost:8080/api/news";

export default function ReporterDashboard() {
  const [news, setNews] = useState([]);
  const [form, setForm] = useState({
    title: "",
    description: "",
    image: null,
  });
  const [editingId, setEditingId] = useState(null);

  const fileInputRef = useRef(null);
  const formRef = useRef(null);
  const token = localStorage.getItem("token");

  // format date
  const formatDate = (date) => {
    return new Date(date).toLocaleString("en-IN", {
      dateStyle: "medium",
      timeStyle: "short",
    });
  };

  // fetch my news
  const fetchMyNews = async () => {
    const res = await fetch(`${API}/my`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    const data = await res.json();
    setNews(data.data);
  };

  useEffect(() => {
    fetchMyNews();
  }, []);

  // handle input
  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "image") {
      setForm({ ...form, image: files[0] });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  // reset form
  const resetForm = () => {
    setForm({ title: "", description: "", image: null });
    setEditingId(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  // submit
  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("title", form.title);
    formData.append("description", form.description);
    if (form.image) formData.append("image", form.image);

    const url = editingId ? `${API}/${editingId}` : API;
    const method = editingId ? "PUT" : "POST";

    await fetch(url, {
      method,
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    });

    resetForm();
    fetchMyNews();
  };

  // delete
  const handleDelete = async (id) => {
    await fetch(`${API}/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    fetchMyNews();
  };

  // edit
  const handleEdit = (item) => {
    setForm({
      title: item.title,
      description: item.description,
      image: null,
    });
    setEditingId(item.id);

    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }

    formRef.current?.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  };

  return (
    <div className="min-h-screen bg-gray-100 p-4 md:p-8">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl text-center font-bold mb-6">
          Reporter Dashboard
        </h1>

        {/* Form */}
        <form
          ref={formRef}
          onSubmit={handleSubmit}
          className="bg-white p-6 rounded-2xl shadow mb-8 space-y-4"
        >
          <h2 className="text-xl font-semibold">
            {editingId ? "Update News" : "Add News"}
          </h2>

          <input
            type="text"
            name="title"
            placeholder="News title"
            value={form.title}
            onChange={handleChange}
            className="w-full border p-3 rounded-lg"
            required
          />

          <textarea
            name="description"
            placeholder="News description"
            value={form.description}
            onChange={handleChange}
            className="w-full border p-3 rounded-lg"
            rows="4"
            required
          />

          <input
            type="file"
            name="image"
            ref={fileInputRef}
            onChange={handleChange}
            className="w-full"
            accept="image/*"
            required={!editingId}
          />

          <div className="flex gap-3">
            <button
              type="submit"
              className="bg-black text-white px-6 py-2 rounded-lg hover:bg-gray-800"
            >
              {editingId ? "Update News" : "Add News"}
            </button>

            {editingId && (
              <button
                type="button"
                onClick={resetForm}
                className="bg-gray-300 text-black px-6 py-2 rounded-lg hover:bg-gray-400"
              >
                Cancel
              </button>
            )}
          </div>
        </form>

        {/* News Cards */}
        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {news.map((item) => (
            <div
              key={item.id}
              className="group relative bg-white p-4 rounded-2xl shadow hover:shadow-lg transition flex flex-col"
            >
              {/* Hover Time Badge */}
              <div className="absolute top-3 left-3 opacity-0 group-hover:opacity-100 transition">
                <span className="bg-black/70 text-white text-xs px-2 py-1 rounded">
                  {formatDate(item.createdAt)}
                </span>
              </div>

              <img
                src={item.imageUrl}
                alt={item.title}
                className="w-full h-48 rounded-2xl object-cover"
              />

              <div className="py-3 flex flex-col flex-1">
                <h3 className="text-xl font-semibold">
                  {item.title}
                </h3>

                <div className="mt-2 h-24 overflow-y-auto pr-1">
                  <p className="text-gray-600 text-sm leading-relaxed">
                    {item.description}
                  </p>
                </div>

                <div className="flex gap-3 mt-4">
                  <button
                    onClick={() => handleEdit(item)}
                    className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                  >
                    Update
                  </button>

                  <button
                    onClick={() => handleDelete(item.id)}
                    className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
                  >
                    Delete
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

