package com.Pharmacy.Pharmacy.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String telephone;

    @OneToMany(mappedBy = "fournisseur")
    private List<Medicament> medicaments;

    @OneToMany(mappedBy = "fournisseur")
    private List<Achat> achats;

    // Constructors
    public Fournisseur() {}

    public Fournisseur(String nom, String email, String telephone) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    public List<Achat> getAchats() {
        return achats;
    }

    public void setAchats(List<Achat> achats) {
        this.achats = achats;
    }
}
