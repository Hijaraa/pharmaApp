package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Repositories.AdminRepository;
import com.Pharmacy.Pharmacy.Services.AdminService;
import com.Pharmacy.Pharmacy.entities.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@ActiveProfiles("h2")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private AdminRepository adminRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Admin admin1;
    private Admin admin2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        admin1 = new Admin();
        admin1.setId(1L);
        admin1.setUsername("admin1");
        admin1.setPassword("password1");

        admin2 = new Admin();
        admin2.setId(2L);
        admin2.setUsername("admin2");
        admin2.setPassword("password2");
    }

    @Test
    void testGetAllAdmins() throws Exception {
        List<Admin> admins = Arrays.asList(admin1, admin2);
        when(adminService.getAllAdmins()).thenReturn(admins);

        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(adminService, times(1)).getAllAdmins();
    }

    @Test
    void testGetAdminById_Found() throws Exception {
        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin1));

        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("admin1")));

        verify(adminService, times(1)).getAdminById(1L);
    }

    @Test
    void testGetAdminById_NotFound() throws Exception {
        when(adminService.getAdminById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admins/3"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).getAdminById(3L);
    }

    @Test
    void testCreateAdmin() throws Exception {
        Admin newAdmin = new Admin();
        newAdmin.setUsername("newAdmin");
        newAdmin.setPassword("newPassword");

        when(adminService.saveAdmin(any(Admin.class))).thenReturn(newAdmin);

        mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).saveAdmin(any(Admin.class));
    }

    @Test
    void testUpdateAdmin_Found() throws Exception {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setId(1L);
        updatedAdmin.setUsername("updatedAdmin");
        updatedAdmin.setPassword("updatedPassword");

        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin1));
        when(adminService.updateAdmin(any(Admin.class))).thenReturn(updatedAdmin);

        mockMvc.perform(put("/api/admins/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("updatedAdmin")));

        verify(adminService, times(1)).getAdminById(1L);
        verify(adminService, times(1)).updateAdmin(any(Admin.class));
    }

    @Test
    void testUpdateAdmin_NotFound() throws Exception {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setId(3L);
        updatedAdmin.setUsername("updatedAdmin");
        updatedAdmin.setPassword("updatedPassword");

        when(adminService.getAdminById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/admins/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAdmin)))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).getAdminById(3L);
        verify(adminService, never()).updateAdmin(any(Admin.class));
    }

    @Test
    void testDeleteAdmin_Found() throws Exception {
        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin1));
        doNothing().when(adminService).deleteAdmin(1L);

        mockMvc.perform(delete("/api/admins/1"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).getAdminById(1L);
        verify(adminService, times(1)).deleteAdmin(1L);
    }

    @Test
    void testDeleteAdmin_NotFound() throws Exception {
        when(adminService.getAdminById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/admins/3"))
                .andExpect(status().isNotFound());

        verify(adminService, times(1)).getAdminById(3L);
        verify(adminService, never()).deleteAdmin(anyLong());
    }

    @Test
    void testAuthenticateAdmin_Success() throws Exception {
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUsername("admin1");
        admin.setPassword("password1");

        when(adminService.authenticate("admin1@example.com", "password1")).thenReturn(admin);

        mockMvc.perform(post("/api/admins/login")
                .param("email", "admin1@example.com")
                .param("password", "password1"))
                .andExpect(status().isOk());

        verify(adminService, times(1)).authenticate("admin1@example.com", "password1");
    }

    @Test
    void testAuthenticateAdmin_Failure() throws Exception {
        when(adminService.authenticate("admin1@example.com", "wrongPassword")).thenReturn(null);

        mockMvc.perform(post("/api/admins/login")
                .param("email", "admin1@example.com")
                .param("password", "wrongPassword"))
                .andExpect(status().isUnauthorized());

        verify(adminService, times(1)).authenticate("admin1@example.com", "wrongPassword");
    }
}
