package com.sumicekic.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate borrowDate; // Alış Tarihi

    private LocalDate returnDate; // İade Tarihi (Boş olabilir)

    // --- İLİŞKİLER ---

    // Bir ödünç işlemi bir öğrenciye aittir.
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Bir ödünç işlemi (o an) tek bir kitaba aittir.
    @OneToOne
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    // --- Constructorlar ---
    public Loan() {
    }

    public Loan(Student student, Book book) {
        this.student = student;
        this.book = book;
        this.borrowDate = LocalDate.now(); // Bugünün tarihini otomatik atar
    }

    // --- Getter ve Setterlar ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    @Override
    public String toString() {
        return "Loan [id=" + id + ", borrowDate=" + borrowDate + ", book=" + book.getTitle() + "]";
    }
}