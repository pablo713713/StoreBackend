package com.example.onlineStore.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private List<Category> listCategories = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    protected Inventory() {
        // requerido por JPA
    }

    public Inventory(List<Category> listCategories) {
        this.listCategories = listCategories;
        this.lastUpdate = new Date();
    }

    public Long getId() {
        return id;
    }

    public List<Category> getListCategories() {
        return listCategories;
    }

    public void setListCategories(List<Category> listCategories) {
        this.listCategories = listCategories;
        this.lastUpdate = new Date();
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void addCategory(Category category) {
        listCategories.add(category);
        this.lastUpdate = new Date();
    }

    public void removeCategory(Category category) {
        listCategories.remove(category);
        this.lastUpdate = new Date();
    }
}
