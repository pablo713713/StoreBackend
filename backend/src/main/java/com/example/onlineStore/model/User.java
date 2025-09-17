package com.example.onlineStore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 80)
    private String firstLastName;

    @Column(nullable = false, length = 80)
    private String secondLastName;

    protected User() {
        // requerido por JPA
    }

    public User(String name, String firstLastName, String secondLastName) {
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    @Override
    public String toString() {
        return name + " " + firstLastName + " " + secondLastName;
    }
}
