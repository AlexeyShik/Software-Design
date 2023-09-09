package ru.shik.sd.SD12.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.shik.sd.SD12.currency.Currency;
import ru.shik.sd.SD12.repository.UserRepository;
import ru.shik.sd.SD12.model.User;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> addUser(long id, String name, Currency currency) {
        return userRepository.save(new User(id, name, currency.name()));
    }

    public Mono<User> findById(long id) {
        return userRepository.findById(id);
    }

    public Mono<Void> deleteById(long id) {
        return userRepository.deleteById(id);
    }

    public Mono<Boolean> existsById(long id) {
        return userRepository.existsById(id);
    }
}
