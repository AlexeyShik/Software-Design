package ru.shik.sd.SD12.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.shik.sd.SD12.currency.Currency;
import ru.shik.sd.SD12.service.UserService;
import ru.shik.sd.SD12.model.User;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public Mono<User> add(long id, String name, String currency) {
        return userService.existsById(id).flatMap(exists -> exists ? Mono.empty()
            : userService.addUser(id, name, Currency.valueOf(currency)));
    }

    @RequestMapping("/get")
    public Mono<User> get(long id) {
        return userService.findById(id);
    }

    @RequestMapping("/delete")
    public Mono<Void> delete(long id) {
        return userService.existsById(id).flatMap(exists -> exists ? userService.deleteById(id) : Mono.empty());
    }
}
