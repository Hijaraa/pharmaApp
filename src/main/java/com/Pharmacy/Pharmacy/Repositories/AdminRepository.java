package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find admin by email
    Admin findByEmail(String email);

    // Find admin by email and password (for authentication)
    Admin findByEmailAndPassword(String email, String password);

    // Find admin by username and password (for authentication)
    Admin findByUsernameAndPassword(String username, String password);
}
