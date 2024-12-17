package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();


    public Collection<User> findAll() {
        return users.values();
    }

    public User create (User user){
        String email = String.valueOf(users.containsKey(user.getEmail()));
        if (user.getEmail() == null || user.getPassword().isBlank()){
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (user.getEmail().equals(email)){
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setEmail(user.getEmail());
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User put (User user){
        String email = String.valueOf(users.containsKey(user.getEmail()));
        if (user.getId() == null){
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (user.getPassword().equals(email)){
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        if (user.getPassword() == null || user.getUsername() == null || user.getEmail() == null){
            return user;
        }
        user.setEmail(user.getEmail());
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public Optional <User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

