const apiUrl = "http://localhost:8080/api/todos";

// 1. Todo 리스트 가져오기
async function fetchTodos() {
    try {
        const token = localStorage.getItem("jwtToken"); // 저장된 JWT 토큰 가져오기
        const response = await fetch(apiUrl, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        const todos = await response.json();
        displayTodos(todos);
    } catch (error) {
        console.error('Error fetching todos:', error);
        alert('An error occurred while fetching the todos.');
    }
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
    const newTodo = { title, completed: false };

    try {
        const token = localStorage.getItem("jwtToken");
        await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newTodo)
        });

        document.getElementById("todo-title").value = "";
        fetchTodos(); // Todo 리스트 다시 불러오기
    } catch (error) {
        console.error('Error creating todo:', error);
        alert('An error occurred while creating the todo.');
    }
}

// 4. Todo 상태 변경하기
async function toggleTodo(id) {
    try {
        const token = localStorage.getItem("jwtToken");
        const todoResponse = await fetch(`${apiUrl}/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        const todo = await todoResponse.json();
        todo.completed = !todo.completed;

        // 상태 변경 후 PUT 요청
        await fetch(`${apiUrl}/${id}`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(todo)
        });

        fetchTodos(); // Todo 리스트 다시 불러오기
    } catch (error) {
        console.error('Error toggling todo:', error);
        alert('An error occurred while updating the todo.');
    }
}

// 5. Todo 삭제하기
async function deleteTodo(id) {
    try {
        const token = localStorage.getItem("jwtToken");
        await fetch(`${apiUrl}/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        fetchTodos(); // Todo 리스트 다시 불러오기
    } catch (error) {
        console.error('Error deleting todo:', error);
        alert('An error occurred while deleting the todo.');
    }
}

// 6. Todo 검색하기
async function searchTodos() {
    const title = document.getElementById("search-title").value;
    try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(`${apiUrl}/search?title=${encodeURIComponent(title)}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        const todos = await response.json();
        displayTodos(todos);
    } catch (error) {
        console.error('Error searching todos:', error);
        alert('An error occurred while searching the todos.');
    }
}

// 7. 사용자 프로필 불러오기 (JWT 토큰을 이용한 인증)
async function fetchUserProfile() {
    try {
        const token = localStorage.getItem("jwtToken");

        // 사용자 프로필을 가져오기 위한 요청
        const response = await fetch('http://localhost:8080/api/users/profile', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const user = await response.json();
        if (user) {
            document.getElementById('profile_nickname').textContent = user.username;
        } else {
            console.log("User not found");
        }
    } catch (error) {
        console.error('Error fetching profile:', error);
        alert('An error occurred while fetching the profile.');
    }
}

// 페이지 로드 시 사용자 정보 불러오기
fetchUserProfile();

// 페이지 로드 시 Todo 리스트 불러오기
fetchTodos();

const calendarDays = document.getElementById('calendarDays');
const currentMonthElement = document.getElementById('dateTitle');
const prevMonthButton = document.getElementById('prevMonth');
const nextMonthButton = document.getElementById('nextMonth');

let currentDate = new Date();


function renderCalendar() {
    calendarDays.innerHTML = '';
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    currentMonthElement.textContent = `${year}년 ${month + 1}월`;

    // 빈 칸 채우기
    for (let i = 0; i < firstDay; i++) {
        const emptyCell = document.createElement('div');
        calendarDays.appendChild(emptyCell);
    }

    // 날짜 채우기
    for (let day = 1; day <= lastDate; day++) {
        const dateCell = document.createElement('div');
        dateCell.textContent = day;
        dateCell.dataset.year = year;
        dateCell.dataset.month = month + 1; // 월은 0부터 시작하므로 +1
        dateCell.dataset.day = day;

        dateCell.addEventListener('click', () => {
            // 이전 선택된 날짜 초기화
            document.querySelectorAll('.selected').forEach(cell => cell.classList.remove('selected'));
            dateCell.classList.add('selected');

            // 선택된 날짜 데이터 출력
            console.log(`선택된 날짜: ${year}-${month + 1}-${day}`);
            handleDateClick(year, month + 1, day);
        });

        calendarDays.appendChild(dateCell);
    }
}

prevMonthButton.addEventListener('click', () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar();
});

nextMonthButton.addEventListener('click', () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar();
});

// 초기 렌더링
renderCalendar();