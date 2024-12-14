package ru.yandex.practicum.catsgram.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create (@RequestBody User user){
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
    public User put (@RequestBody User user){
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



    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
