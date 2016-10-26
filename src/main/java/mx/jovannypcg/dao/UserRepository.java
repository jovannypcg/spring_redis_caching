package mx.jovannypcg.dao;

import mx.jovannypcg.dto.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();
    void flushCache();
}
