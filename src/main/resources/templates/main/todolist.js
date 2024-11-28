const apiUrl = "http://localhost:8080/api/todos";

// 1. Todo 리스트 가져오기
async function fetchTodos() {
    const response = await fetch(apiUrl);
    const todos = await response.json();
    displayTodos(todos);
}

// 2. Todo 리스트 화면에 표시
function displayTodos(todos) {
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML = "";
    todos.forEach(todo => {
        const todoItem = document.createElement("div");
        todoItem.innerHTML = `
                    
                    <input type="checkbox" ${todo.completed ? "checked" : ""} onchange="toggleTodo('${todo.id}')">
                    <span>${todo.title}</span>
                    <button onclick="deleteTodo('${todo.id}')">Delete</button>
                    
                `;
        todoList.appendChild(todoItem);
    });
}

// 3. Todo 생성하기
async function createTodo() {
    const title = document.getElementById("todo-title").value;
    const newTodo = {title, completed: false};

    await fetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(newTodo)
    });

    document.getElementById("todo-title").value = "";
    fetchTodos();
}

// 4. Todo 상태 변경하기
async function toggleTodo(id) {
    const todoResponse = await fetch(`${apiUrl}/${id}`);
    const todo = await todoResponse.json();
    todo.completed = !todo.completed;

    await fetch(`${apiUrl}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(todo)
    });

    fetchTodos();
}

// 5. Todo 삭제하기
async function deleteTodo(id) {
    await fetch(`${apiUrl}/${id}`, {
        method: "DELETE"
    });

    fetchTodos();
}

// 6. Todo 검색하기
async function searchTodos() {
    const title = document.getElementById("search-title").value;

    const response = await fetch(`${apiUrl}/search?title=${encodeURIComponent(title)}`);
    const todos = await response.json();
    displayTodos(todos);
}

async function fetchUserProfile() {
    try {
        // Assuming you store the JWT token in localStorage
        const token = localStorage.getItem("jwtToken");

        // Fetch user profile from the backend
        const response = await fetch('/api/users/profile', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        // If the response is not okay (status code not 200), throw an error
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Parse the response to get the user data
        const user = await response.json();

        // Check if user is null or undefined
        if (user) {
            // If the user is found, display the username in the #profile_nickname div
            document.getElementById('profile_nickname').textContent = user.username;
        } else {
            console.log("User not found");
        }

    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while fetching the profile.');
    }
}


// 페이지 로드 시 사용자 정보 불러오기
fetchUserProfile();

// 페이지 로드 시 Todo 리스트 불러오기
fetchTodos();


const makeCalendar = (date) => {
    const currentYear = new Date(date).getFullYear();
    const currentMonth = new Date(date).getMonth() + 1;

    const firstDay = new Date(date.setDate(1)).getDay();
    const lastDay = new Date(currentYear, currentMonth, 0).getDate();

    const limitDay = firstDay + lastDay;
    const nextDay = Math.ceil(limitDay / 7) * 7;

    let htmlDummy = '';

    for (let i = 0; i < firstDay; i++) {
        htmlDummy += `<div class="noColor"></div>`;
    }

    for (let i = 1; i <= lastDay; i++) {
        htmlDummy += `<div>${i}</div>`;
    }

    for (let i = limitDay; i < nextDay; i++) {
        htmlDummy += `<div class="noColor"></div>`;
    }

    document.querySelector(`.dateBoard`).innerHTML = htmlDummy;
    document.querySelector(`.dateTitle`).innerText = `${currentYear}년 ${currentMonth}월`;
}


const date = new Date();

makeCalendar(date);

// 이전달 이동
document.querySelector(`.prevDay`).onclick = () => {
    makeCalendar(new Date(date.setMonth(date.getMonth() - 1)));
}

// 다음달 이동
document.querySelector(`.nextDay`).onclick = () => {
    makeCalendar(new Date(date.setMonth(date.getMonth() + 1)));
}