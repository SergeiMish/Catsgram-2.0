package ru.yandex.practicum.catsgram.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {

    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(int size, int from, @NonNull String sort) {
        SortOrder sortOrder = SortOrder.from(sort);

        return posts.values().stream()
                .sorted((p1, p2) -> {
                    if (sortOrder == SortOrder.ASCENDING) {
                        return p1.getPostDate().compareTo(p2.getPostDate());
                    } else {
                        return p2.getPostDate().compareTo(p1.getPostDate());
                    }
                })
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }
    public enum SortOrder {
        ASCENDING, DESCENDING;

        // Преобразует строку в элемент перечисления
        public static SortOrder from(String order) {
            switch (order.toLowerCase()) {
                case "ascending":
                case "asc":
                    return ASCENDING;
                case "descending":
                case "desc":
                    return DESCENDING;
                default: return null;
            }
        }
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (userService.findUserById(post.getAuthorId()).isEmpty()){
            throw new ConditionsNotMetException("Автор с id = "+ post.getAuthorId() + " не найден");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Optional<Post> findPostById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}