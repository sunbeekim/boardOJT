
package com.example.board.service;

@Service
@RequiredArgsConstructor
public class UserServiceFacade {

    private final UserServiceFactory userServiceFactory;

    public void deleteUser(Long targetUserId, JwtUserInfo userInfo) {
        String role = userInfo.getRole();
        Long requesterId = userInfo.getId();

        UserDeleteService deleteService = userServiceFactory.getDeleteService(role);
        deleteService.delete(targetUserId, requesterId);
    }
}