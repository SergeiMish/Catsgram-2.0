package ru.yandex.practicum.catsgram.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.dal.mappers.UserRowMapper;
import ru.yandex.practicum.catsgram.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, mapper);
    }

    public Optional<User> findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try {
            User result = jdbc.queryForObject(query, mapper, email);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
    public Optional<User> findByEmail(long userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            User result = jdbc.queryForObject(query, mapper, userId);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}
