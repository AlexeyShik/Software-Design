package ru.akirakozov.sd.refactoring.dao;

import java.util.List;
import java.util.Optional;

public interface IDaoService {

    void addProduct(ProductEntry entry);

    List<ProductEntry> getProducts();

    Optional<ProductEntry> getMaxProduct();

    Optional<ProductEntry> getMinProduct();

    Optional<Integer> getPricesSum();

    Optional<Integer> getProductsCount();
}
