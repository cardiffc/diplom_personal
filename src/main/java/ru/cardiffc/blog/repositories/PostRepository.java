package ru.cardiffc.blog.repositories;

import ru.cardiffc.blog.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post , Integer > {
}
