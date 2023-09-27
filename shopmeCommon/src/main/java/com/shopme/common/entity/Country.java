package com.shopme.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 45, nullable = false)
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states;

    public Country() {
    }

    public Country(Integer id) {
        this.id = id;
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Country(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    public Set<State> getStates() {
        return states;
    }

    @JsonIgnore
    public void setStates(Set<State> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "Country{ id=" + id + ", name=" + name + ", code=" + code + "}";
    }
}
