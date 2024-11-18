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
    await fetch('/api/users/username', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
    })
        .then(response =>
            response.json())  // JSON 응답을 처리
        .then(data => {
            console.log(data.username);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        });
}







// 페이지 로드 시 Todo 리스트 불러오기
fetchTodos();
fetchUserProfile();