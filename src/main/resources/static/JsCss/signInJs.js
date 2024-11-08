function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/users/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    })
        .then(response => response.text())
        .then(result => {
            const messageElement = document.getElementById("message");
            console.log("result = "+result);
            if (result === "로그인 성공") {
                messageElement.style.color = "green";
                messageElement.textContent = "로그인 성공!";
                // 로그인 성공 시 페이지 이동 예시 (home.html로 이동)
                // window.location.href = "/home";
            } else {
                messageElement.style.color = "red";
                messageElement.textContent = "로그인 실패: 아이디 또는 비밀번호를 확인하세요.";
            }
        })
        .catch(error => {
            console.error("Error:", error);
            document.getElementById("message").textContent = "서버 오류가 발생했습니다.";
        });
}
