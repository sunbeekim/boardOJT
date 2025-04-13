package com.example.board.domain.service.register;


@Component
public class UserDeleteServiceRegistrar implements UserServiceRegistrar {

    private final UserDeleteService userDeleteService;

    public UserDeleteServiceRegistrar(
        UserDeleteService userDeleteService,       
    ) {
        this.userDeleteService = userDeleteService;       
    }

    @Override
    public void register(UserServiceFactory factory) {
        factory.registerService("USER:delete", userDeleteService); 
    }
}
