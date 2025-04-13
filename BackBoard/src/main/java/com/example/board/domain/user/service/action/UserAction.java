public interface UserAction {
    String getKey(); // 예: "delete", "signup"
    void execute(JwtUserInfo userInfo);
}
