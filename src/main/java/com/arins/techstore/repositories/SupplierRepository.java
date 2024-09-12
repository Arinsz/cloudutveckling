package com.arins.techstore.repositories;

import com.arins.techstore.entitys.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
