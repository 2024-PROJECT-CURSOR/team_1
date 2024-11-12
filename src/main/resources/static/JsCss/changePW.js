document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 방지

    const username = document.getElementById('current-username').value;
    const currentPassword = document.getElementById('current-password').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    if (newPassword !== confirmPassword) {
        alert("새로운 비밀번호가 일치하지 않습니다.");
        return false;
    }
    const data = {
        username: username,
        currentPassword: currentPassword,
        newPassword: newPassword
    };


    fetch('/api/users/changePW', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())  // 서버로부터 받은 메시지를 텍스트로 처리
        .then(message => {
            if (message === "변경 성공") {
                alert('비밀번호가 성공적으로 변경되었습니다!');
            } else {
                alert(`비밀번호 변경 실패: ${message}`);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        });

});
