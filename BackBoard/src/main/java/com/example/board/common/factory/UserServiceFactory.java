package com.example.board.common.factory;

@Component
public class UserServiceFactory {

    private final Map<String, Object> serviceMap = new HashMap<>();

    @Autowired
    public UserServiceFactory(List<UserServiceRegistrar> registrars) {
        for (UserServiceRegistrar r : registrars) {
            r.register(this);
        }
    }

    public <T> void registerService(String key, T service) {
        serviceMap.put(key, service);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(String key, Class<T> type) {
        Object service = serviceMap.get(key);
        if (service == null) {
            throw new IllegalArgumentException("등록되지 않은 키입니다: " + key);
        }
        return type.cast(service);
    }
}

