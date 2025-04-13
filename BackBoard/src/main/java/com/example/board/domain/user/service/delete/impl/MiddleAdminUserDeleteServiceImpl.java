@Component
public class MiddleAdminUserDeleteServiceImpl implements UserDeleteService {

    @Override
    public String getRole() {
        return "MIDDLEADMIN";
    }

    @Override
    public void delete(Long targetUserId, Long requesterUserId) {
        // 자신보다 권한이 낮은 타인 강제 삭제 가능
    }
}
