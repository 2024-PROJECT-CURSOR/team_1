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
        .then(response => {
            if (response.ok) {
                return response.text(); // JWT 토큰 반환
            } else {
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
        })
        .then(token => {
            console.log("JWT Token:", token); // 콘솔에 JWT 토큰 출력 (디버깅용)
            localStorage.setItem("jwtToken", token); // JWT 토큰을 로컬 스토리지에 저장
            document.getElementById("message").style.color = "green";
            document.getElementById("message").textContent = "로그인 성공!";
            window.location.href = "/"; // 홈 페이지로 이동
        })
        .catch(error => {
            console.error("Error:", error);
            document.getElementById("message").style.color = "red";
            document.getElementById("message").textContent = `로그인 실패: ${error.message}`;
        });
}
