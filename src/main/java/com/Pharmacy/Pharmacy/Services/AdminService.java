package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.AdminRepository;
import com.Pharmacy.Pharmacy.entities.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // CRUD operations
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    // Authentication
    public Admin authenticate(String email, String password) {
        return adminRepository.findByEmailAndPassword(email, password);
    }

    public Admin authenticateAdmin(String username, String password) {
        return adminRepository.findByUsernameAndPassword(username, password);
    }

    // Additional business logic
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
