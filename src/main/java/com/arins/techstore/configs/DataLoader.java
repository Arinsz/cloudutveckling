package com.arins.techstore.configs;

import com.arins.techstore.aws.Xray.XRayTimed;
import com.arins.techstore.entitys.Product;
import com.arins.techstore.entitys.Supplier;

import com.arins.techstore.services.ProductService;

import com.arins.techstore.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
public class DataLoader {

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;


    @XRayTimed(segmentName = "Insert Products When Application Starts")
    @Bean
    public CommandLineRunner loadData() {
        return args -> {


            Supplier supplier1 = new Supplier("Komplett", "987-654-3210", "456 Komplett Street");
            Supplier supplier2 = new Supplier("Gamer's Haven", "987-654-3210", "456 Gamer Street");

            // Save Suppliers
            supplierService.saveSupplier(supplier1);
            supplierService.saveSupplier(supplier2);

            // Create products and associate them with suppliers
            Product laptop1 = createProduct("Gaming Laptop XYZ", "High performance gaming laptop with 16GB RAM and 1TB SSD.", 399, "/images/laptop1.jpg", "user123", supplier1);
            Product laptop2 = createProduct("Business Laptop ABC", "Reliable business laptop with 8GB RAM and 256GB SSD.", 499, "/images/laptop2.jpg", "user123", supplier1);
            Product laptop3 = createProduct("Ultrabook DEF", "Ultra-thin ultrabook with 8GB RAM and 512GB SSD.", 799, "/images/laptop3.jpg", "user123", supplier2);

            Product gamingPC1 = createProduct("Gaming PC Titan", "Powerful gaming PC with RTX 3080 and 32GB RAM.", 999, "/images/gamingPC1.jpg", "user123", supplier2);
            Product gamingPC2 = createProduct("Gaming PC Elite", "High-end gaming PC with RTX 3070 and 16GB RAM.", 699, "/images/gamingPC2.jpg", "user123", supplier2);
            Product gamingPC3 = createProduct("Gaming PC Pro", "Mid-range gaming PC with GTX 1660 and 16GB RAM.", 1299, "/images/gamingPC3.jpg", "user123", supplier1);

            // Save products to the database
            productService.addProduct(laptop1);
            productService.addProduct(laptop2);
            productService.addProduct(laptop3);
            productService.addProduct(gamingPC1);
            productService.addProduct(gamingPC2);
            productService.addProduct(gamingPC3);
        };
    }

    private Product createProduct(String title, String description, double price, String imagePath, String userId, Supplier supplier) {
        // Create and return Product with URL or path to the image and associated supplier
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setImageUrl(imagePath);  // Use the image path or URL from your database
        product.setUserId(userId);
        product.setPrice(price);
        product.setSupplier(supplier);  // Associate the supplier
        return product;
    }
}
