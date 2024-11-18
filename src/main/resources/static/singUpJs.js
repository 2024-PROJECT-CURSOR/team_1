document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼의 기본 제출 방지

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;

    const data = {
        username: username,
        password: password,
        email: email
    };


    fetch('/api/users/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                alert('회원가입이 성공적으로 완료되었습니다!');
            } else {
                alert('회원가입에 실패했습니다. 다시 시도해주세요.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다. 다시 시도해주세요.');
        });
});
