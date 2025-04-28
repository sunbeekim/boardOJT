const userAPI = {
    checkId: (email) => {
        return $.ajax({
          url: '/api/users/check/email',
          method: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({ email })
        });
      },
}
$(document).ready(() => {
// 아이디 중복 체크
$('#checkIdBtn').on('click', (e) => {
    const $userIdInput = $('input[name="userId"]');
    const userId = $userIdInput.val();

    if (!userId) {
      alert('아이디를 입력해주세요.');
      return;
    }
    
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(userId)) {
      alert('아이디는 이메일 형식이어야 합니다.');
      return;
    }

    userAPI.checkId(userId)
      .then(() => {
        userIdCheck = true;
        $userIdInput.removeClass('is-invalid').addClass('is-valid');
        alert('사용 가능한 아이디입니다.');
      })
      .catch(() => {
        userIdCheck = false;
        $userIdInput.removeClass('is-valid').addClass('is-invalid');
        alert('이미 사용중인 아이디입니다.');
      });
  });
});