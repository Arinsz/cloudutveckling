package com.arins.techstore.services;

import com.arins.techstore.entitys.Supplier;
import com.arins.techstore.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Method to save a supplier
    public void saveSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    // Method to get all suppliers
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Supplier not found with id: " + id));
    }


}