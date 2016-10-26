package mx.jovannypcg.dao;

import mx.jovannypcg.dto.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @CacheEvict(allEntries = true, value = "caching:users")
    @Override
    public void flushCache() {
        // Intentionally left blank
    }

    @Override
    @Cacheable(value = "caching:users")
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User jovanny = new User();
        User pablo = new User();

        jovanny.setUsername("jovannypcg");
        jovanny.setFirstName("Jovanny");
        jovanny.setLastName("Cruz");
        jovanny.setAdult(true);
        jovanny.setAge(24);

        pablo.setUsername("pmarmol");
        pablo.setFirstName("Pablo");
        pablo.setLastName("Marmol");
        pablo.setAdult(false);
        pablo.setAge(17);

        users.add(jovanny);
        users.add(pablo);

        return users;
    }
}
