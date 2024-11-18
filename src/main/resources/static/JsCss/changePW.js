window.onload = function() {
    const token = localStorage.getItem("jwtToken");

    if(token == null){
        alert("로그인 먼저 해주세요.");
        window.location.href = "/"
    }
};
document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 방지
    const token = localStorage.getItem("jwtToken");
    const currentPassword = document.getElementById('current-password').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    console.log(localStorage.getItem("jwtToken"));
    if (newPassword !== confirmPassword) {
        alert("새로운 비밀번호가 일치하지 않습니다.");
        return false;
    }
    const data = {
        currentPassword: currentPassword,
        newPassword: newPassword
    };


    fetch('/api/users/changePW', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(data)
    })
        .then(response =>
            response.text())  // JSON 응답을 처리
        .then(data => {
            console.log(data);
            if (data === "변경 성공") {
                alert('비밀번호가 성공적으로 변경되었습니다!');
            } else {
                alert(`비밀번호 변경 실패: ${data}`);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        });


});
