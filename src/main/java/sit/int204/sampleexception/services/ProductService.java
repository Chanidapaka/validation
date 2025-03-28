package sit.int204.sampleexception.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sit.int204.sampleexception.entities.Product;
import sit.int204.sampleexception.exceptions.ItemNotFoundException;
import sit.int204.sampleexception.repositories.ProductRepo;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    public Page<Product> findAll(int page, int size) {
        return productRepo.findAll(PageRequest.of(page, size));
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }
    public Product findById(String productCode) {
        return productRepo.findById(productCode).orElseThrow(
                () -> new ItemNotFoundException("Product id "
                + productCode + " does not exist !!!")
        );
    }

}
