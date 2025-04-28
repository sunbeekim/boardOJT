$(document).ready(() => {
    let userIdCheck = false;

    // 아이디 입력 필드 변경 시 userIdCheck 초기화
    $('input[name="userId"]').on('input', (e) => {
      userIdCheck = false;
      $(e.currentTarget).removeClass('is-valid is-invalid');
    });

    // 실시간 입력 검증
    $('input[data-regex]').on('input', (e) => {
        const $input = $(e.currentTarget);
        const value = $input.val();
        const regex = new RegExp($input.data('regex'));
        const errorMessage = $input.data('error-message');
      
        if (value === '') {
          $input.removeClass('is-valid is-invalid');
          $input.siblings('.invalid-feedback').remove();
          return;
        }
      
        if (regex.test(value)) {
          $input.removeClass('is-invalid').addClass('is-valid');
          $input.siblings('.invalid-feedback').remove();
        } else {
          $input.removeClass('is-valid').addClass('is-invalid');
          if ($input.siblings('.invalid-feedback').length === 0) {
            $input.after(`<div class="invalid-feedback">${errorMessage}</div>`);
          }
        }
      });
      
});