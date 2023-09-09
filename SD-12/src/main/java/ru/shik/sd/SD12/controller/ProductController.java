package ru.shik.sd.SD12.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shik.sd.SD12.currency.Currency;
import ru.shik.sd.SD12.model.Product;
import ru.shik.sd.SD12.model.ProductInfo;
import ru.shik.sd.SD12.service.ProductService;
import ru.shik.sd.SD12.service.UserService;

@RestController()
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public Mono<Product> add(@RequestParam long id, @RequestParam String name,
                             @RequestParam double price, @RequestParam String currency) {
        return productService.existsById(id).flatMap(exists -> exists ? Mono.empty()
            : productService.addProduct(id, name, price, Currency.valueOf(currency)));
    }

    @RequestMapping("/delete")
    public Mono<Void> delete(@RequestParam long id) {
        return productService.existsById(id).flatMap(exists -> exists ? productService.deleteProduct(id): Mono.empty());
    }

    @RequestMapping("/get")
    public Flux<ProductInfo> getAll(@RequestParam long userId) {
        return userService
            .existsById(userId)
            .flatMap(exists -> exists ? userService.findById(userId) : Mono.empty())
            .flatMapMany(user -> productService.getProducts(Currency.valueOf(user.getCurrency())));
    }
}
