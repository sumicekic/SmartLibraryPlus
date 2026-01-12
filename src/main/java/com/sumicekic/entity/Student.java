package com.sumicekic.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String department;

    // --- OneToMany ---
    // Bir öğrencinin birden fazla ödünç alma işlemi (Loan) olabilir.
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    // --- Constructorlar ---
    public Student() {
    }

    public Student(String name, String department) {
        this.name = name;
        this.department = department;
    }

    // --- Getter ve Setterlar ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public List<Loan> getLoans() { return loans; }
    public void setLoans(List<Loan> loans) { this.loans = loans; }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", department=" + department + "]";
    }
}