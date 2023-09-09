package ru.shik.sd.SD12.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.shik.sd.SD12.currency.Currency;
import ru.shik.sd.SD12.currency.CurrencyConverter;
import ru.shik.sd.SD12.model.ProductInfo;
import ru.shik.sd.SD12.repository.ProductRepository;
import ru.shik.sd.SD12.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final CurrencyConverter currencyConverter = new CurrencyConverter();

    public Mono<Product> addProduct(long id, String name, double price, Currency currency) {
        double priceRUB = currencyConverter.convertToRUB(price, currency);
        return productRepository.save(new Product(id, name, priceRUB));
    }

    public Mono<Void> deleteProduct(long id) {
        return productRepository.deleteById(id);
    }

    public Mono<Boolean> existsById(long id) {
        return productRepository.existsById(id);
    }

    public Flux<ProductInfo> getProducts(Currency currency) {
        return productRepository.findAll().map(product -> buildProductInfo(product,  currency));
    }

    private ProductInfo buildProductInfo(Product product, Currency currency) {
        double price = currencyConverter.convertFromRUB(product.getPriceRUB(), currency);
        return new ProductInfo(product.getName(), price, currency);
    }
}
