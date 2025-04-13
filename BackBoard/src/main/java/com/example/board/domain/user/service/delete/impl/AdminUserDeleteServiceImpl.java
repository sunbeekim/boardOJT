@Component
public class AdminUserDeleteServiceImpl implements UserDeleteService {

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public void delete(Long targetUserId, Long requesterUserId) {
        // 타인 강제 삭제 가능
        // delete logic
    }
}
