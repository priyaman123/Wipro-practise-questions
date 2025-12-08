const USER_BASE = "http://localhost:8081";
const ADMIN_BASE = "http://localhost:8082";

// Utility: Set message text
function setText(id, msg, isError = false) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = msg || "";
  if (isError) el.classList.add("error");
  else el.classList.remove("error");
}

// Generic fetch wrapper
async function doFetch(url, options = {}) {
  try {
    const headers = {
      "Content-Type": "application/json",
      ...(options.headers || {})
    };

    // Add JWT token only for USER API calls
    if (url.startsWith(USER_BASE)) {
      const token = localStorage.getItem("jwtToken");
      if (token) headers["Authorization"] = "Bearer " + token;
    }

    const res = await fetch(url, { ...options, headers });

    const text = await res.text();
    let data;
    try {
      data = text ? JSON.parse(text) : null;
    } catch {
      data = text;
    }

    if (!res.ok) {
      const msg = data && data.error ? data.error : res.status + " " + res.statusText;
      throw new Error(msg);
    }

    return data;
  } catch (err) {
    console.error("Request failed", url, err);
    throw err;
  }
}

// Detect page
document.addEventListener("DOMContentLoaded", () => {
  const bodyId = document.body.id;
  if (bodyId === "user-page") setupUserPage();
  else if (bodyId === "admin-page") setupAdminPage();
});


// =====================================================
// USER PAGE
// =====================================================
function setupUserPage() {
  const regForm = document.getElementById("user-register-form");
  const loginForm = document.getElementById("user-login-form");
  const loadSongsBtn = document.getElementById("load-songs-btn");
  const createPlaylistForm = document.getElementById("create-playlist-form");
  const loadPlaylistsBtn = document.getElementById("load-playlists-btn");
  const loadPlaylistDetailsBtn = document.getElementById("load-playlist-details-btn");
  const addSongToPlaylistForm = document.getElementById("add-song-to-playlist-form");
  const playbackButtons = document.querySelectorAll(".playback-btn");

  // USER REGISTER
  if (regForm) {
    regForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setText("register-status", "Registering...");
      const body = {
        name: document.getElementById("reg-name").value,
        email: document.getElementById("reg-email").value,
        password: document.getElementById("reg-password").value,
        phone: document.getElementById("reg-phone").value
      };

      try {
        const data = await doFetch(`${USER_BASE}/users/register`, {
          method: "POST",
          body: JSON.stringify(body)
        });
        setText("register-status", `Registered with ID ${data.id}`);
        regForm.reset();
      } catch (err) {
        setText("register-status", "Registration failed: " + err.message, true);
      }
    });
  }

  // USER LOGIN (JWT)
  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setText("login-status", "Logging in...");
      const body = {
        email: document.getElementById("login-email").value,
        password: document.getElementById("login-password").value
      };

      try {
        const data = await doFetch(`${USER_BASE}/users/login`, {
          method: "POST",
          body: JSON.stringify(body)
        });

        localStorage.setItem("jwtToken", data.token);
        setText("login-status", "Login successful. Token stored.");

        const uid = data.userId;
        const u1 = document.getElementById("playlist-userId");
        const u2 = document.getElementById("playlists-userId");
        if (u1 && !u1.value) u1.value = uid;
        if (u2 && !u2.value) u2.value = uid;
      } catch (err) {
        setText("login-status", "Login failed: " + err.message, true);
      }
    });
  }

  // LOAD SONGS
  if (loadSongsBtn) {
    loadSongsBtn.addEventListener("click", async () => {
      const tbody = document.querySelector("#songs-table tbody");
      if (tbody) tbody.innerHTML = "";
      setText("songs-error", "");

      try {
        const data = await doFetch(`${USER_BASE}/songs`);
        if (Array.isArray(data) && tbody) {
          data.forEach((s) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${s.id}</td><td>${s.name}</td><td>${s.singer}</td><td>${s.album}</td>`;
            tbody.appendChild(tr);
          });
        }
      } catch (err) {
        setText("songs-error", "Failed to load songs: " + err.message, true);
      }
    });
  }

  // PLAYLIST CREATE
  if (createPlaylistForm) {
    createPlaylistForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setText("playlist-create-status", "Creating playlist...");
      const body = {
        name: document.getElementById("playlist-name").value,
        userId: parseInt(document.getElementById("playlist-userId").value, 10)
      };

      try {
        const data = await doFetch(`${USER_BASE}/playlists`, {
          method: "POST",
          body: JSON.stringify(body)
        });

        setText("playlist-create-status", `Playlist created with ID ${data.id}`);
        createPlaylistForm.reset();
      } catch (err) {
        setText("playlist-create-status", "Failed: " + err.message, true);
      }
    });
  }

  // LOAD PLAYLISTS FOR USER
  if (loadPlaylistsBtn) {
    loadPlaylistsBtn.addEventListener("click", async () => {
      const userId = parseInt(document.getElementById("playlists-userId").value, 10);
      const tbody = document.querySelector("#playlists-table tbody");
      if (tbody) tbody.innerHTML = "";
      setText("playlists-error", "");

      if (!userId) {
        setText("playlists-error", "Enter User ID", true);
        return;
      }

      try {
        const data = await doFetch(`${USER_BASE}/playlists/user/${userId}`);
        if (Array.isArray(data) && tbody) {
          data.forEach((p) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${p.id}</td><td>${p.name}</td><td>${p.user ? p.user.id : ""}</td>`;
            tbody.appendChild(tr);
          });
        }
      } catch (err) {
        setText("playlists-error", "Failed to load playlists: " + err.message, true);
      }
    });
  }

  // LOAD PLAYLIST DETAILS
  if (loadPlaylistDetailsBtn) {
    loadPlaylistDetailsBtn.addEventListener("click", async () => {
      const playlistId = parseInt(document.getElementById("selected-playlist-id").value, 10);
      const tbody = document.querySelector("#playlist-songs-table tbody");
      if (tbody) tbody.innerHTML = "";
      setText("playlist-actions-status", "");

      if (!playlistId) {
        setText("playlist-actions-status", "Enter playlist ID", true);
        return;
      }

      try {
        const data = await doFetch(`${USER_BASE}/playlists/${playlistId}`);
        if (data && Array.isArray(data.songs) && tbody) {
          data.songs.forEach((s) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${s.id}</td><td>${s.name}</td><td>${s.singer}</td><td>${s.album}</td>`;
            tbody.appendChild(tr);
          });
        }
      } catch (err) {
        setText("playlist-actions-status", "Failed to load playlist: " + err.message, true);
      }
    });
  }

  // ADD SONG TO PLAYLIST
  if (addSongToPlaylistForm) {
    addSongToPlaylistForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const playlistId = parseInt(document.getElementById("addsong-playlist-id").value, 10);
      const songId = parseInt(document.getElementById("addsong-song-id").value, 10);

      if (!playlistId || !songId) {
        setText("playlist-actions-status", "Playlist ID and Song ID required", true);
        return;
      }

      try {
        await doFetch(`${USER_BASE}/playlists/${playlistId}/songs/${songId}`, {
          method: "POST",
          body: "{}"
        });
        setText("playlist-actions-status", "Song added to playlist");
      } catch (err) {
        setText("playlist-actions-status", "Failed: " + err.message, true);
      }
    });
  }

  // PLAYBACK ACTIONS
  playbackButtons.forEach((btn) => {
    btn.addEventListener("click", async () => {
      const action = btn.dataset.action;
      const playlistId = parseInt(document.getElementById("selected-playlist-id").value, 10);

      if (!playlistId) {
        setText("playlist-actions-status", "Enter playlist ID", true);
        return;
      }

      try {
        const data = await doFetch(`${USER_BASE}/playlists/${playlistId}/${action}`, {
          method: "POST",
          body: "{}"
        });
        setText("playlist-actions-status", `Action: ${data.status} on playlist ${data.playlistId}`);
      } catch (err) {
        setText("playlist-actions-status", "Failed: " + err.message, true);
      }
    });
  });
}


// =====================================================
// ADMIN PAGE
// =====================================================
// =====================================================
// ADMIN PAGE (FULLY FIXED)
// =====================================================
function setupAdminPage() {
  const loginForm = document.getElementById("admin-login-form");
  const loadUsersBtn = document.getElementById("load-users-btn");
  const loadSongsBtn = document.getElementById("load-songs-btn");
  const loadPlaylistsBtn = document.getElementById("load-playlists-btn");
  const loadPlaylistsByUserBtn = document.getElementById("load-playlists-user-btn");
  const deleteSongBtn = document.getElementById("delete-song-btn");
  const deletePlaylistBtn = document.getElementById("delete-playlist-btn");
  const addSongForm = document.getElementById("add-song-form");

  // ADMIN LOGIN
  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setText("admin-login-status", "Logging in...");

      const username = document.getElementById("admin-username").value;
      const password = document.getElementById("admin-password").value;

      try {
        await doFetch(`${ADMIN_BASE}/admin/login`, {
          method: "POST",
          body: JSON.stringify({ username, password })
        });
        setText("admin-login-status", "Login successful");
      } catch (err) {
        setText("admin-login-status", "Login failed: " + err.message, true);
      }
    });
  }

  // LOAD USERS
  if (loadUsersBtn) {
    loadUsersBtn.addEventListener("click", async () => {
      const tbody = document.querySelector("#users-table tbody");
      tbody.innerHTML = "";
      setText("user-count", "Loading...");

      try {
        const users = await doFetch(`${ADMIN_BASE}/admin/users`);
        users.forEach((u) => {
          const row = `<tr><td>${u.id}</td><td>${u.name}</td><td>${u.email}</td><td>${u.phone}</td></tr>`;
          tbody.insertAdjacentHTML("beforeend", row);
        });

        setText("user-count", `Total: ${users.length}`);
      } catch (err) {
        setText("users-error", "Failed to load users: " + err.message, true);
      }
    });
  }

  // LOAD SONGS
  if (loadSongsBtn) {
    loadSongsBtn.addEventListener("click", async () => {
      const tbody = document.querySelector("#admin-songs-table tbody");
      tbody.innerHTML = "";

      try {
        const songs = await doFetch(`${USER_BASE}/songs`);
        songs.forEach((s) => {
          const row = `<tr><td>${s.id}</td><td>${s.name}</td><td>${s.singer}</td><td>${s.album}</td></tr>`;
          tbody.insertAdjacentHTML("beforeend", row);
        });
      } catch (err) {
        setText("songs-error-admin", "Failed to load songs: " + err.message, true);
      }
    });
  }

  // ADD SONG — FIXED (ADMIN MUST ADD TO USER-SERVICE!)
  if (addSongForm) {
    addSongForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setText("song-add-status", "Adding song...");

      const body = {
        name: document.getElementById("song-name").value,
        singer: document.getElementById("song-singer").value,
        musicDirector: document.getElementById("song-director").value,
        album: document.getElementById("song-album").value,
        releaseDate: document.getElementById("song-releaseDate").value || null
      };

      try {
        const song = await doFetch(`${USER_BASE}/songs`, {
          method: "POST",
          body: JSON.stringify(body)
        });

        setText("song-add-status", `Song added with ID: ${song.id}`);
        addSongForm.reset();
      } catch (err) {
        setText("song-add-status", "Failed: " + err.message, true);
      }
    });
  }

  // LOAD ALL PLAYLISTS
  if (loadPlaylistsBtn) {
    loadPlaylistsBtn.addEventListener("click", async () => {
      const tbody = document.querySelector("#admin-playlists-table tbody");
      tbody.innerHTML = "";

      try {
        const playlists = await doFetch(`${USER_BASE}/playlists`);
        playlists.forEach((p) => {
          const row = `<tr><td>${p.id}</td><td>${p.name}</td><td>${p.userId}</td></tr>`;
          tbody.insertAdjacentHTML("beforeend", row);
        });
      } catch (err) {
        setText("playlist-error-admin", "Failed to load playlists: " + err.message, true);
      }
    });
  }

  // LOAD PLAYLISTS BY USER
  if (loadPlaylistsByUserBtn) {
    loadPlaylistsByUserBtn.addEventListener("click", async () => {
      const userId = document.getElementById("playlist-userId-admin").value;
      const tbody = document.querySelector("#admin-playlists-user-table tbody");
      tbody.innerHTML = "";

      try {
        const playlists = await doFetch(`${USER_BASE}/playlists/user/${userId}`);
        playlists.forEach((p) => {
          const row = `<tr><td>${p.id}</td><td>${p.name}</td><td>${p.userId}</td></tr>`;
          tbody.insertAdjacentHTML("beforeend", row);
        });
      } catch (err) {
        setText("playlist-user-error-admin", "Error: " + err.message, true);
      }
    });
  }

  // DELETE SONG
  if (deleteSongBtn) {
    deleteSongBtn.addEventListener("click", async () => {
      const songId = document.getElementById("delete-song-id").value;

      try {
        await doFetch(`${USER_BASE}/songs/${songId}`, { method: "DELETE" });
        setText("delete-song-status", "Song deleted");
      } catch (err) {
        setText("delete-song-status", "Failed: " + err.message, true);
      }
    });
  }

  // DELETE PLAYLIST
  if (deletePlaylistBtn) {
    deletePlaylistBtn.addEventListener("click", async () => {
      const playlistId = document.getElementById("delete-playlist-id").value;

      try {
        await doFetch(`${USER_BASE}/playlists/${playlistId}`, { method: "DELETE" });
        setText("delete-playlist-status", "Playlist deleted");
      } catch (err) {
        setText("delete-playlist-status", "Failed: " + err.message, true);
      }
    });
  }
}
