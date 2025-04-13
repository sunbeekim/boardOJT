@Component
public class UserActionFactory {

    private final Map<String, UserAction> actionMap = new HashMap<>();

    @Autowired
    public UserActionFactory(List<UserAction> actions) {
        for (UserAction action : actions) {
            actionMap.put(action.getKey(), action);
        }
    }

    public UserAction getAction(String key) {
        UserAction action = actionMap.get(key);
        if (action == null) {
            throw new IllegalArgumentException("지원하지 않는 사용자 기능입니다: " + key);
        }
        return action;
    }
}
