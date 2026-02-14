/* eslint-disable no-unused-vars */
/* eslint-disable react-hooks/set-state-in-effect */
import { useEffect, useState } from "react";
import axios from "axios";
import { jwtDecode } from "jwt-decode";

const API = "http://localhost:8080/api/news";
const REACTION_API = "http://localhost:8080/api/reactions";
const COMMENT_API = "http://localhost:8080/api/news";

export default function AllNews() {
  const [news, setNews] = useState([]);
  const [reactions, setReactions] = useState({});
  const [userReactions, setUserReactions] = useState({});
  const [comments, setComments] = useState({});
  const [openComments, setOpenComments] = useState({});
  const [newComment, setNewComment] = useState({});
  const [editingComment, setEditingComment] = useState(null);
  const [editText, setEditText] = useState("");

  const token = localStorage.getItem("token");

  let userId = null;
  if (token) {
    try {
      const decoded = jwtDecode(token);
      userId = decoded.sub;
    } catch (error) {
      console.log(error);
    }
  }

  const headers = {
    Authorization: `Bearer ${token}`,
  };

  // ---------- FETCH NEWS ----------
  const fetchAllNews = async () => {
    try {
      const res = await axios.get(API, { headers });
      const newsData = res.data.data;
      setNews(newsData);

      newsData.forEach(async (item) => {
        const r = await axios.get(`${REACTION_API}/${item.id}`, {
          headers,
        });
        setReactions((prev) => ({
          ...prev,
          [item.id]: r.data.data,
        }));
      });
    } catch (err) {
      console.error("Failed to fetch news", err);
    }
  };

  useEffect(() => {
    fetchAllNews();
  }, []);

  // ---------- REACTIONS ----------
  const react = async (newsId, type) => {
    try {
      const currentReaction = userReactions[newsId];

      const res = await axios.post(
        `${REACTION_API}/${newsId}`,
        { reaction: type },
        { headers },
      );

      setReactions((prev) => ({
        ...prev,
        [newsId]: res.data.data,
      }));

      setUserReactions((prev) => {
        const updated = { ...prev };

        if (currentReaction === type) {
          delete updated[newsId];
        } else {
          updated[newsId] = type;
        }

        return updated;
      });
    } catch (err) {
      console.error("Reaction failed", err);
    }
  };

  // ---------- COMMENTS ----------
  const toggleComments = async (newsId) => {
    const isOpen = openComments[newsId];

    if (!isOpen) {
      const res = await axios.get(`${COMMENT_API}/${newsId}/comments`, {
        headers,
      });

      setComments((prev) => ({
        ...prev,
        [newsId]: res.data.data,
      }));
    }

    setOpenComments((prev) => ({
      ...prev,
      [newsId]: !isOpen,
    }));
  };

  const addComment = async (newsId) => {
    const content = newComment[newsId];
    if (!content) return;

    await axios.post(
      `${COMMENT_API}/${newsId}/comments`,
      { content },
      { headers },
    );

    const res = await axios.get(`${COMMENT_API}/${newsId}/comments`, {
      headers,
    });

    setComments((prev) => ({
      ...prev,
      [newsId]: res.data.data,
    }));

    setNewComment((prev) => ({ ...prev, [newsId]: "" }));
  };

  const deleteComment = async (newsId, commentId) => {
    await axios.delete(`${COMMENT_API}/${newsId}/comments/${commentId}`, {
      headers,
    });

    setComments((prev) => ({
      ...prev,
      [newsId]: prev[newsId].filter((c) => c.id !== commentId),
    }));
  };

  const startEdit = (comment) => {
    setEditingComment(comment.id);
    setEditText(comment.content);
  };

  const saveEdit = async (newsId, commentId) => {
    const res = await axios.put(
      `${COMMENT_API}/${newsId}/comments/${commentId}`,
      { content: editText },
      { headers },
    );

    setComments((prev) => ({
      ...prev,
      [newsId]: prev[newsId].map((c) =>
        c.id === commentId ? res.data.data : c,
      ),
    }));

    setEditingComment(null);
  };

  // ---------- DATE FORMAT ----------
  const formatDateTime = (dateString) => {
    if (!dateString) return "";
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return "";

    return date.toLocaleString("en-IN", {
      timeZone: "Asia/Kolkata",
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="min-h-screen bg-gray-50 py-6 px-3 sm:px-6">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-2xl sm:text-3xl md:text-4xl text-center font-bold mb-8">
          Latest News
        </h1>

        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6 items-start">
          {news.map((item) => {
            const counts = reactions[item.id] || {
              likes: 0,
              dislikes: 0,
            };

            const userReaction = userReactions[item.id];
            const isOwner = Number(userId) === Number(item.reporterId);
            const newsComments = comments[item.id] || [];

            return (
              <div
                key={item.id}
                className="relative bg-white rounded-3xl shadow-md hover:shadow-xl transition p-4 flex flex-col"
              >
                {/* Time Badge */}
                <div className="absolute top-3 left-3">
                  <span className="bg-black/80 text-white text-xs px-3 py-1 rounded-full shadow">
                    {formatDateTime(item.createdAt)}
                  </span>
                </div>

                {/* Your News Badge */}
                {isOwner && (
                  <div className="absolute top-3 right-3">
                    <span className="bg-blue-600 text-white text-xs px-3 py-1 rounded-full shadow">
                      Your News
                    </span>
                  </div>
                )}

                <img
                  src={item.imageUrl}
                  alt={item.title}
                  className="w-full h-48 object-cover rounded-2xl"
                />

                <h2 className="text-lg font-semibold mt-3">{item.title}</h2>

                <p className="text-gray-600 text-sm mt-2 leading-relaxed">
                  {item.description}
                </p>

                {/* Reactions */}
                <div className="flex gap-2 mt-4 items-center">
                  <button
                    onClick={() => react(item.id, "LIKE")}
                    disabled={isOwner}
                    className={`w-20 px-3 py-1.5 rounded-full text-xs font-medium flex items-center justify-center gap-1 ${
                      userReaction === "LIKE" ? "bg-green-200" : "bg-green-100"
                    }`}
                  >
                    👍 <span className="w-5 text-center">{counts.likes}</span>
                  </button>

                  <button
                    onClick={() => react(item.id, "DISLIKE")}
                    disabled={isOwner}
                    className={`w-20 px-3 py-1.5 rounded-full text-xs font-medium flex items-center justify-center gap-1 ${
                      userReaction === "DISLIKE" ? "bg-red-200" : "bg-red-100"
                    }`}
                  >
                    👎{" "}
                    <span className="w-5 text-center">{counts.dislikes}</span>
                  </button>

                  <button
                    onClick={() => toggleComments(item.id)}
                    className="ml-auto text-xs text-blue-600 font-medium"
                  >
                    {openComments[item.id] ? "Hide" : "Comments"}
                  </button>
                </div>

                {/* Comments */}
                {openComments[item.id] && (
                  <div className="mt-4 border-t pt-3">
                    {!isOwner && (
                      <div className="flex items-center gap-3 mb-4">
                        <input
                          value={newComment[item.id] || ""}
                          onChange={(e) =>
                            setNewComment((prev) => ({
                              ...prev,
                              [item.id]: e.target.value,
                            }))
                          }
                          placeholder="Write a comment..."
                          className="flex-1 bg-transparent border-b border-gray-300 focus:border-blue-500 outline-none text-sm py-1"
                        />
                        <button
                          onClick={() => addComment(item.id)}
                          className="text-blue-600 text-sm font-medium hover:underline"
                        >
                          Post
                        </button>
                      </div>
                    )}

                    <div className="max-h-32 overflow-y-auto space-y-3 pr-1">
                      {newsComments.length === 0 ? (
                        <p className="text-xs text-gray-500 text-center py-3">
                          No comments yet
                        </p>
                      ) : (
                        newsComments.map((c) => {
                          const isMyComment =
                            Number(userId) === Number(c.userId);

                          return (
                            <div key={c.id} className="flex gap-2">
                              <div className="w-7 h-7 rounded-full bg-blue-500 text-white flex items-center justify-center text-xs font-semibold">
                                {c.username.charAt(0).toUpperCase()}
                              </div>

                              <div className="flex-1">
                                <div className="flex justify-between">
                                  <span className="font-semibold text-xs">
                                    {c.username}
                                  </span>
                                  <span className="text-[10px] text-gray-500">
                                    {formatDateTime(c.createdAt)}
                                  </span>
                                </div>

                                {editingComment === c.id ? (
                                  <div className="flex items-center gap-2 mt-1">
                                    <input
                                      value={editText}
                                      onChange={(e) =>
                                        setEditText(e.target.value)
                                      }
                                      className="flex-1 bg-transparent border-b border-gray-300 focus:border-blue-500 outline-none text-xs py-1"
                                    />
                                    <button
                                      onClick={() => saveEdit(item.id, c.id)}
                                      className="px-2 py-1 text-[10px] bg-green-100 text-green-700 rounded"
                                    >
                                      Save
                                    </button>
                                    <button
                                      onClick={() => setEditingComment(null)}
                                      className="px-2 py-1 text-[10px] bg-gray-200 text-gray-700 rounded"
                                    >
                                      Cancel
                                    </button>
                                  </div>
                                ) : (
                                  <p className="text-xs mt-1">{c.content}</p>
                                )}

                                {isMyComment && editingComment !== c.id && (
                                  <div className="flex gap-2 mt-2">
                                    <button
                                      onClick={() => startEdit(c)}
                                      className="px-2 py-1 text-[10px] bg-blue-100 text-blue-700 rounded"
                                    >
                                      Edit
                                    </button>
                                    <button
                                      onClick={() =>
                                        deleteComment(item.id, c.id)
                                      }
                                      className="px-2 py-1 text-[10px] bg-red-100 text-red-700 rounded"
                                    >
                                      Delete
                                    </button>
                                  </div>
                                )}
                              </div>
                            </div>
                          );
                        })
                      )}
                    </div>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
