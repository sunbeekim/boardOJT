@Component
public class UserDeleteAction implements UserAction {

    private final UserServiceFactory userServiceFactory;

    @Override
    public String getKey() {
        return "delete";
    }

    @Override
    public void execute(JwtUserInfo userInfo) {
        String key = userInfo.getRole() + ":delete";
        UserDeleteService deleteService = userServiceFactory.getService(key, UserDeleteService.class);
        deleteService.delete(userInfo.getId(), userInfo.getId());
    }
}
