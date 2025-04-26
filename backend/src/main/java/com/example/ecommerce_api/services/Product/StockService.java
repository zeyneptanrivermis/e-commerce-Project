package com.example.ecommerce_api.services.Product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.ProductEntity.Stock;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.ProductRepository.StockRepository;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + id));
    }

    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock updatedStock) {
        Stock stock = getStockById(id);
        stock.setQuantity(updatedStock.getQuantity());
        stock.setProduct(updatedStock.getProduct());
        stock.setSeller(updatedStock.getSeller());
        stock.setRestockedDate(updatedStock.getRestockedDate());

        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    public List<Stock> getStocksBySeller(Seller seller) {
        return stockRepository.findBySeller(seller);
    }
}
