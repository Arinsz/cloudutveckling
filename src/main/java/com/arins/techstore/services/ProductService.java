package com.arins.techstore.services;

import com.arins.techstore.aws.S3Service;
import com.arins.techstore.aws.Xray.XRayTimed;
import com.arins.techstore.entitys.Product;
import com.arins.techstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private S3Service s3Service;



    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    public void updateProduct(Product product) {
        // Ensure the product exists before updating
        if (!productRepository.existsById(product.getId())) {
            throw new NoSuchElementException("Product not found with id: " + product.getId());
        }
        productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @XRayTimed(segmentName = "Get Product by User ID")
    public List<Product> getProductByUserId(String userId) {
        return productRepository.findByUserId(userId);
    }


    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        // Retrieve the product to get the image URL
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Extract the image URL and determine the S3 key if imageUrl is not null
        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            // Delete the image from S3
            s3Service.deleteFile(key);
        }

        // Delete the product from the database
        productRepository.deleteById(id);
    }

    public void deleteProductsByUserId(String userId) {
        List<Product> products = productRepository.findProductByUserId(userId);
        for (Product toDelete : products) {
            // Extract the image URL and determine the S3 key
            String imageUrl = toDelete.getImageUrl();
            String key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            // Delete the product from the database
            productRepository.deleteById(toDelete.getId());

            // Delete the image from S3
            s3Service.deleteFile(key);
        }
    }
}




