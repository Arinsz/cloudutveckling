package com.arins.techstore.controllers;

import com.arins.techstore.entitys.Supplier;
import com.arins.techstore.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    SupplierService supplierService;
    @GetMapping()
    public List<Supplier> getAllSuppliers() {
        // Replace with your actual service call
        return supplierService.getAllSuppliers();
    }
}