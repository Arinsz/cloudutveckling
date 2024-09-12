package com.arins.techstore.controllers;

import com.arins.techstore.aws.Xray.XRayTimed;
import com.arins.techstore.entitys.Product;
import com.arins.techstore.entitys.Supplier;
import com.arins.techstore.services.ProductService;
import com.arins.techstore.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @XRayTimed(segmentName = "GetHomePage")
    @GetMapping("/home")
    public String getHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // Fetch products for the current user
            String userId = userDetails.getUsername(); // Assuming username is used as userId
            List<Product> products = productService.getProductByUserId(userId);
            model.addAttribute("products", products);
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            model.addAttribute("suppliers", suppliers);
        }
        return "home"; // Ensure this matches the name of your Thymeleaf template
    }
}