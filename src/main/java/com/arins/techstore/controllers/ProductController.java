package com.arins.techstore.controllers;

import com.arins.techstore.aws.S3Service;
import com.arins.techstore.aws.Xray.XRayTimed;
import com.arins.techstore.cognito.CognitoHandler;
import com.arins.techstore.entitys.Product;
import com.arins.techstore.entitys.Supplier;
import com.arins.techstore.services.ProductService;
import com.arins.techstore.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    S3Service s3Service;

    @Autowired
    SupplierService supplierService;


    @Value("${cognito.userPoolId}") // Make sure to set this in application.properties
    private String userPoolId;

    @GetMapping("/all")
    public String getAllProducts(Model model, Authentication authentication) {
        String username = authentication.getName();

        // Check if user is in the "Admins" group
        boolean isAdmin = CognitoHandler.isUserInGroup(username, userPoolId, "Admins");

        List<Product> products = productService.getAllProducts();
        List<Supplier> suppliers = supplierService.getAllSuppliers();

        model.addAttribute("products", products);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("isAdmin", isAdmin); // Pass admin status to the view

        return "all-products";
    }


    @GetMapping
    public String getProducts(Model model) {
        String userId = productService.getCurrentUserId();
        List<Product> products = productService.getProductByUserId(userId);
        model.addAttribute("products", products);
        return "userproducts"; // Make sure this matches the name of your Thymeleaf template for user products
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String title,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) Double price,
                             @RequestParam(required = false) Long supplierId,
                             @RequestParam("image") MultipartFile image,  // Tar emot bildfil från formuläret
                             Model model) {
        String userId = productService.getCurrentUserId();
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setUserId(userId);

        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            product.setSupplier(supplier);
        }

        // Hantera bilduppladdning till S3
        try {
            if (!image.isEmpty()) {
                // Ladda upp bilden till S3 och få tillbaka URL:en
                String imageUrl = s3Service.uploadImage(image);
                product.setImageUrl(imageUrl);  // Spara URL:en i produkten
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Hantera fel vid bilduppladdning, t.ex. visa ett felmeddelande
            model.addAttribute("errorMessage", "Error uploading image.");
            return "home"; // Returnera till samma sida vid fel
        }

        // Spara produkten i databasen
        productService.addProduct(product);

        // Hämta den uppdaterade produktlistan efter att produkten lagts till
        List<Product> products = productService.getProductByUserId(userId);
        model.addAttribute("products", products);

        // Returnera samma sida med den uppdaterade produktlistan
        return "userproducts"; // Se till att "home" matchar namnet på din Thymeleaf-mall
    }




    @PostMapping("/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String title,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) Double price,
                                @RequestParam(required = false) Long supplierId,
                                @RequestParam(required = false) MultipartFile image,
                                Model model) {
        try {
            Product product = productService.getProductById(id);
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);
            if (supplierId != null) {
                Supplier supplier = supplierService.getSupplierById(supplierId);
                product.setSupplier(supplier);
            }

            if (image != null && !image.isEmpty()) {
                String imageUrl = s3Service.uploadImage(image);
                product.setImageUrl(imageUrl);
            }

            productService.updateProduct(product);
            model.addAttribute("products", productService.getProductByUserId(product.getUserId()));

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating product.");
        }
        return "redirect:/products/all"; // Redirect to the product list or user products page
    }


    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("product", product);
        model.addAttribute("suppliers", suppliers);
        return "edit-product"; // Create this Thymeleaf template
    }


    @PostMapping("/allproducts/delete")
    public String deleteProductfromAll(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the product.");
        }
        return "redirect:/products/all"; // Redirect to the page where products are listed
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the product.");
        }
        return "redirect:/products"; // Redirect to the page where products are listed
    }
}
