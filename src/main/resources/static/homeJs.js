// loginStatus.js

// 페이지 로드 후 실행
document.addEventListener("DOMContentLoaded", function () {
    const loginLink = document.getElementById("loginLink"); // 로그인/로그아웃 링크
    const token = localStorage.getItem("jwtToken"); // 로컬 스토리지에서 토큰 확인

    if (token) {
        // 토큰이 있으면 로그인이 되어 있는 상태
        loginLink.textContent = "로그아웃"; // 링크 텍스트를 '로그아웃'으로 변경
        loginLink.addEventListener("click", function (event) {
            event.preventDefault();
            // 로그아웃 처리: 로컬 스토리지에서 토큰 삭제
            localStorage.removeItem("jwtToken");
            // 로그아웃 후 페이지 새로고침
            window.location.reload();
        });
    } else {
        // 토큰이 없으면 로그인 상태
        loginLink.textContent = "로그인"; // 링크 텍스트를 '로그인'으로 변경
        loginLink.addEventListener("click", function (event) {
            event.preventDefault();
            // 로그인 페이지로 이동
            window.location.href = "/api/users/signIn";
        });
    }
});
