<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>EventHub - Manage Your Events</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <style>
    :root {
      --primary: #2e3192;
      --secondary: #1bbc9b;
      --background: #f8f9fa;
      --text: #2c3e50;
      --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: 'Poppins', sans-serif;
    }

    body {
      background: var(--background);
      color: var(--text);
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem;
    }

    .header {
      background: var(--primary);
      color: white;
      padding: 1rem 0;
      margin-bottom: 2rem;
      box-shadow: var(--shadow);
    }

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .logo {
      font-size: 1.8rem;
      font-weight: 700;
    }

    .tabs {
      display: flex;
      gap: 1rem;
      margin-bottom: 2rem;
    }

    .tab {
      padding: 0.8rem 1.5rem;
      background: white;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: var(--shadow);
    }

    .tab.active {
      background: var(--secondary);
      color: white;
    }

    .events-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 2rem;
      margin-bottom: 2rem;
    }

    .event-card {
      background: white;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: var(--shadow);
      transition: transform 0.3s ease;
    }

    .event-card:hover {
      transform: translateY(-5px);
    }

    .event-image {
      height: 200px;
      background: var(--primary);
      position: relative;
    }

    .event-date {
      position: absolute;
      top: 1rem;
      right: 1rem;
      background: var(--secondary);
      color: white;
      padding: 0.5rem 1rem;
      border-radius: 20px;
      font-size: 0.9rem;
    }

    .event-content {
      padding: 1.5rem;
    }

    .event-title {
      font-size: 1.2rem;
      margin-bottom: 0.5rem;
      color: var(--primary);
    }

    .event-description {
      font-size: 0.9rem;
      color: #666;
      margin-bottom: 1rem;
    }

    .event-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem 1.5rem;
      background: #f8f9fa;
    }

    .create-event-btn {
      position: fixed;
      bottom: 2rem;
      right: 2rem;
      background: var(--secondary);
      color: white;
      border: none;
      border-radius: 50%;
      width: 60px;
      height: 60px;
      font-size: 2rem;
      cursor: pointer;
      box-shadow: var(--shadow);
      transition: all 0.3s ease;
    }

    .create-event-btn:hover {
      transform: scale(1.1);
    }

    .modal {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0, 0, 0, 0.5);
      justify-content: center;
      align-items: center;
    }

    .modal-content {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      width: 90%;
      max-width: 600px;
    }

    .form-group {
      margin-bottom: 1rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
    }

    .form-group input,
    .form-group textarea {
      width: 100%;
      padding: 0.8rem;
      border: 1px solid #ddd;
      border-radius: 4px;
    }

    .btn {
      padding: 0.8rem 1.5rem;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-primary {
      background: var(--primary);
      color: white;
    }

    .btn-secondary {
      background: #ddd;
      color: var(--text);
    }

    @media (max-width: 768px) {
      .events-grid {
        grid-template-columns: 1fr;
      }

      .header-content {
        flex-direction: column;
        text-align: center;
        gap: 1rem;
      }

      .tabs {
        flex-wrap: wrap;
        justify-content: center;
      }
    }
  </style>
</head>
<body>
<header class="header">
  <div class="container header-content">
    <div class="logo">EventHub</div>
    <div class="tabs">
      <button class="tab active" onclick="showEvents('all')">All Events</button>
      <button class="tab" onclick="showEvents('upcoming')">Upcoming Events</button>
    </div>
  </div>
</header>

<main class="container">
  <div class="events-grid" id="events-container">
    <!-- Events will be dynamically inserted here -->
  </div>
</main>

<button class="create-event-btn" onclick="showCreateEventModal()">+</button>

<div class="modal" id="createEventModal">
  <div class="modal-content">
    <h2>Create New Event</h2>
    <form id="createEventForm" onsubmit="createEvent(event)">
      <div class="form-group">
        <label for="eventName">Event Name</label>
        <input type="text" id="eventName" required>
      </div>
      <div class="form-group">
        <label for="eventDescription">Description</label>
        <textarea id="eventDescription" rows="4" required></textarea>
      </div>
      <div class="form-group">
        <label for="eventDate">Date</label>
        <input type="datetime-local" id="eventDate" required>
      </div>
      <div class="form-group">
        <label for="eventLocation">Location</label>
        <input type="text" id="eventLocation" required>
      </div>
      <div style="display: flex; gap: 1rem; justify-content: flex-end;">
        <button type="button" class="btn btn-secondary" onclick="hideCreateEventModal()">Cancel</button>
        <button type="submit" class="btn btn-primary">Create Event</button>
      </div>
    </form>
  </div>
</div>

<script>
  let currentEvents = [];

  async function loadEvents(type = 'all') {
    try {
      const endpoint = type === 'upcoming' ? '/WebSortingAlgorithm_war/events/upcoming' : '/WebSortingAlgorithm_war/events/all';
      const response = await fetch(endpoint);
      const events = await response.json();
      currentEvents = events;
      displayEvents(events);
    } catch (error) {
      console.error('Error loading events:', error);
    }
  }

  function displayEvents(events) {
    const container = document.getElementById('events-container');
    container.innerHTML = '';

    events.forEach(event => {
      const eventDate = new Date(event.eventDate);
      const card = document.createElement('div');
      card.className = 'event-card';
      card.innerHTML =
              '<div class="event-image">' +
              '<div class="event-date">' + eventDate.toLocaleDateString() + '</div>' +
              '</div>' +
              '<div class="event-content">' +
              '<h3 class="event-title">' + event.eventName + '</h3>' +
              '<p class="event-description">' + event.description + '</p>' +
              '</div>' +
              '<div class="event-footer">' +
              '<div>' + event.location + '</div>' +
              '<div>Organizer: ' + (event.organizer && event.organizer.name ? event.organizer.name : 'Unknown') + '</div>' +
              '</div>';

      container.appendChild(card);
    });
  }

  function showEvents(type) {
    const tabs = document.querySelectorAll('.tab');
    tabs.forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');
    loadEvents(type);
  }

  function showCreateEventModal() {
    document.getElementById('createEventModal').style.display = 'flex';
  }

  function hideCreateEventModal() {
    document.getElementById('createEventModal').style.display = 'none';
  }

  async function createEvent(event) {
    event.preventDefault();

    const eventData = {
      eventName: document.getElementById('eventName').value,
      description: document.getElementById('eventDescription').value,
      eventDate: new Date(document.getElementById('eventDate').value).toISOString(),
      location: document.getElementById('eventLocation').value
    };

    try {
      const response = await fetch('/WebSortingAlgorithm_war/events/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(eventData)
      });

      if (response.ok) {
        hideCreateEventModal();
        loadEvents();
        document.getElementById('createEventForm').reset();
      }
    } catch (error) {
      console.error('Error creating event:', error);
    }
  }

  // Initialize the page
  loadEvents();
</script>
</body>
</html>