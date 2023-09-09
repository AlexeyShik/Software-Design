package ru.shik.sd.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.shik.sd.dao.MarketDao;
import ru.shik.sd.model.Stock;

@RestController()
@RequestMapping("/market")
public class MarketController {

    private final MarketDao marketDao;

    public MarketController(MarketDao marketDao) {
        this.marketDao = marketDao;
    }

    @RequestMapping("/company/add")
    public ResponseEntity<Object> addCompany(@RequestParam String name) {
        if (marketDao.getCompany(name).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        marketDao.addCompany(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stock/add")
    public ResponseEntity<Object> addStock(@RequestParam int id, @RequestParam String name, @RequestParam String companyName,
                                           @RequestParam long amount, @RequestParam double price) {
        if (marketDao.getStock(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (marketDao.getCompany(companyName).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (price <= 0 || amount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        marketDao.addStock(id, name, companyName, amount, price);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stock/info")
    public ResponseEntity<Object> infoStock(@RequestParam String name) {
        Optional<Stock> stock = marketDao.getStock(name);
        if (stock.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stock.get(), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stock/get")
    public ResponseEntity<Object> getStock(@RequestParam int id) {
        Optional<Stock> stock = marketDao.getStock(id);
        if (stock.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(stock.get(), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stock/buy")
    public ResponseEntity<Object> buyStock(@RequestParam int id, @RequestParam long amount) {
        Optional<Stock> stock = marketDao.getStock(id);
        if (stock.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (stock.get().getAmount() < amount) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        marketDao.changeStockAmount(id, stock.get().getAmount() - amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stock/sell")
    public ResponseEntity<Object> sellStock(@RequestParam int id, @RequestParam long amount) {
        Optional<Stock> stock = marketDao.getStock(id);
        if (stock.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        marketDao.changeStockAmount(id, stock.get().getAmount() + amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/stock/admin/price")
    public ResponseEntity<Object> priceStock(@RequestParam int id, @RequestParam double price) {
        Optional<Stock> stock = marketDao.getStock(id);
        if (stock.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        marketDao.changeStockPrice(id, price);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
