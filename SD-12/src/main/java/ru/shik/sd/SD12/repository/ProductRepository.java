package ru.shik.sd.SD12.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import ru.shik.sd.SD12.model.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
}