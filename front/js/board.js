/* user 정보 menu 보이도록 */
function showMenu() {
    const userInfoDropDown = $(".user_info .dropdown-menu");
    userInfoDropDown.toggleClass("show");
}

$(document).ready(function() {
    // 검색 영역 클릭시 체크되도록 이벤트 등록
    $(".icheckbox_flat-blue").on("click", (e) => {
        const iCheckbox = $(e.target).hasClass("search-range-ins") ? $(e.target).parent() : $(e.target);
        // if (!$("#searchTotal").is(":checked")) {
        //     $(".icheckbox_flat-blue:not(:checked)").removeClass("checked");
        // } else {
            $(iCheckbox).toggleClass("checked");
        // }
    });

    // datepicker 설정(등록일)
    $( "#prev_date" ).datepicker();
    $( "#after_date" ).datepicker();
})