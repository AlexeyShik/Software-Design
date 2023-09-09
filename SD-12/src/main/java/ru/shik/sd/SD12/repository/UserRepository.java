package ru.shik.sd.SD12.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ru.shik.sd.SD12.model.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
