
@Component
public class UserUserDeleteServiceImpl implements UserDeleteService {

    @Override
    public String getRole() {
        return "USER";
    }

    @Override
    public void delete(Long targetUserId, Long requesterUserId) {
        if (!targetUserId.equals(requesterUserId)) {
            throw new ForbiddenException("자신만 삭제할 수 있습니다.");
        }

        // 일반사용자 삭제 로직
    }
}
