package product;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final List<ProductDTO> products = new ArrayList<>();

    public ProductService() {
        products.add(new ProductDTO(1L, "노트북", 1200000));
        products.add(new ProductDTO(2L, "마우스", 25000));
        products.add(new ProductDTO(3L, "키보드", 45000));
        products.add(new ProductDTO(4L, "모니터", 300000));
        products.add(new ProductDTO(5L, "헤드셋", 80000));
        products.add(new ProductDTO(6L, "스피커", 65000));
        products.add(new ProductDTO(7L, "웹캠", 55000));
        products.add(new ProductDTO(8L, "USB 허브", 15000));
        products.add(new ProductDTO(9L, "외장하드", 110000));
        products.add(new ProductDTO(10L, "프린터", 220000));
    }

    public ProductDTO getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<ProductDTO> getAllProducts() {
        return products;
    }

    public List<ProductDTO> getProductsByName(String name) {
        return products.stream()
                .filter(p -> p.getName().contains(name))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<ProductDTO> getProductsByIdAndName(Long id, String name) {
        return products.stream()
                .filter(p -> (p.getId().equals(id) || p.getName().contains(name)))
                .collect(java.util.stream.Collectors.toList());
    }
}