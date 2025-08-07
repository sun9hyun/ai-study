package product;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private int price;
}