$(document).ready(() => {
    let userIdCheck = false;

    // 아이디 입력 필드 변경 시 userIdCheck 초기화
    $('input[name="userId"]').on('input', (e) => {
        userIdCheck = false;
        validateInput(e.currentTarget);
    });

    // 모든 입력 필드에 대한 실시간 검증
    $('input[data-regex]').on('input', (e) => {
        validateInput(e.currentTarget);
    });

    // 입력 필드 포커스 아웃 시 검증
    $('input[data-regex]').on('blur', (e) => {
        validateInput(e.currentTarget);
    });

    // 입력 필드 검증 함수
    const validateInput = (input) => {
        const $input = $(input);
        const value = $input.val();
        const regex = new RegExp($input.data('regex'));
        const errorMessage = $input.data('error-message');
        const $errorMessage = $input.parent().next('.invalid-feedback');
        
        if (value === '') {
            $input.removeClass('is-valid is-invalid');
            $errorMessage.hide();
            return;
        }
        
        if (regex.test(value)) {
            $input.removeClass('is-invalid').addClass('is-valid');
            $errorMessage.hide();
        } else {
            $input.removeClass('is-valid').addClass('is-invalid');
            $errorMessage.text(errorMessage).show();
        }
    }

    // 비밀번호 확인 검증
    $('input[name="passwordConfirm"]').on('input', (e) => {
        const password = $('input[name="password"]').val();
        const confirmPassword = $(e.currentTarget).val();
        const $errorMessage = $(e.currentTarget).parent().next('.invalid-feedback');
        
        if (confirmPassword === '') {
            $(e.currentTarget).removeClass('is-valid is-invalid');
            $errorMessage.hide();
            return;
        }
        
        if (password === confirmPassword) {
            $(e.currentTarget).removeClass('is-invalid').addClass('is-valid');
            $errorMessage.hide();
        } else {
            $(e.currentTarget).removeClass('is-valid').addClass('is-invalid');
            $errorMessage.text('비밀번호가 일치하지 않습니다.').show();
        }
    });
});