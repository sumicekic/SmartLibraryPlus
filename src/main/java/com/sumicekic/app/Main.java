package com.sumicekic.app;

import com.sumicekic.dao.BookDao;
import com.sumicekic.dao.LoanDao;
import com.sumicekic.dao.StudentDao;
import com.sumicekic.entity.Book;
import com.sumicekic.entity.Loan;
import com.sumicekic.entity.Student;
import com.sumicekic.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Veritabani islemleri icin DAO siniflarimizi cagiriyoruz
    private static final BookDao bookDao = new BookDao();
    private static final StudentDao studentDao = new StudentDao();
    private static final LoanDao loanDao = new LoanDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Otomasyon sistemi baslatiliyor...");

        while (true) {
            System.out.println("\n--- KUTUPHANE YONETIM SISTEMI ---");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Kitaplari Listele");
            System.out.println("3. Ogrenci Ekle");
            System.out.println("4. Ogrencileri Listele");
            System.out.println("5. Kitap Odunc Ver");
            System.out.println("6. Odunc Listesini Goruntule");
            System.out.println("7. Kitap Iade Al");
            System.out.println("0. Cikis");
            System.out.print("Seciminiz: ");

            int choice = -1;
            try {
                // Sayi girilmezse hata vermesin diye try-catch kullandim
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Hatali giris! Lutfen sadece rakam giriniz.");
                continue;
            }

            switch (choice) {
                case 1 -> addBook();
                case 2 -> listBooks();
                case 3 -> addStudent();
                case 4 -> listStudents();
                case 5 -> borrowBook();
                case 6 -> listLoans();
                case 7 -> returnBook();
                case 0 -> {
                    System.out.println("Programdan cikiliyor...");
                    HibernateUtil.shutdown();
                    System.exit(0);
                }
                default -> System.out.println("Gecersiz bir secim yaptiniz, tekrar deneyin.");
            }
        }
    }

    // 1. KİTAP EKLEME
    private static void addBook() {
        System.out.println("\n--- Yeni Kitap Kaydi ---");
        System.out.print("Kitap Adi: ");
        String title = scanner.nextLine();
        System.out.print("Yazar Adi: ");
        String author = scanner.nextLine();

        System.out.print("Basim Yili: ");
        int year = 0;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Yil bilgisi hatali, 2000 olarak varsayildi.");
            year = 2000;
        }

        Book book = new Book(title, author, year);
        bookDao.save(book);
        System.out.println("Kitap basariyla kaydedildi.");
    }

    // 2. KİTAP LİSTELEME
    private static void listBooks() {
        System.out.println("\n--- Mevcut Kitaplar ---");
        List<Book> books = bookDao.getAll();

        if (books.isEmpty()) {
            System.out.println("Sistemde kayitli kitap bulunamadi.");
        } else {
            System.out.printf("%-5s %-30s %-20s %-10s %-15s%n", "ID", "KITAP ADI", "YAZAR", "YIL", "DURUM");
            System.out.println("--------------------------------------------------------------------------------");
            for (Book b : books) {
                System.out.printf("%-5d %-30s %-20s %-10d %-15s%n",
                        b.getId(), b.getTitle(), b.getAuthor(), b.getYear(), b.getStatus());
            }
        }
    }

    // 3. ÖĞRENCİ EKLEME
    private static void addStudent() {
        System.out.println("\n--- Yeni Ogrenci Kaydi ---");
        System.out.print("Ogrenci Ad Soyad: ");
        String name = scanner.nextLine();
        System.out.print("Bolum: ");
        String dept = scanner.nextLine();

        Student student = new Student(name, dept);
        studentDao.save(student);
        System.out.println("Ogrenci kaydi olusturuldu.");
    }

    // 4. ÖĞRENCİ LİSTELEME
    private static void listStudents() {
        System.out.println("\n--- Kayitli Ogrenciler ---");
        List<Student> students = studentDao.getAll();
        if (students.isEmpty()) {
            System.out.println("Kayitli ogrenci yok.");
        } else {
            for (Student s : students) {
                System.out.println("ID: " + s.getId() + " - " + s.getName() + " (" + s.getDepartment() + ")");
            }
        }
    }

    // 5. ÖDÜNÇ VERME İŞLEMİ
    private static void borrowBook() {
        System.out.println("\n--- Kitap Odunc Verme ---");

        // Kullanıcıya kolaylık olsun diye önce listeleri gösteriyoruz
        listBooks();
        System.out.print("\nOdunc verilecek Kitap ID: ");
        Long bookId;
        try {
            bookId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hatali ID girisi.");
            return;
        }

        Book book = bookDao.getById(bookId);

        if (book == null) {
            System.out.println("Hata: Bu ID ile bir kitap bulunamadi.");
            return;
        }

        if (book.getStatus() == Book.Status.BORROWED) {
            System.out.println("Uyari: Bu kitap su an baskasinda, odunc verilemez.");
            return;
        }

        listStudents();
        System.out.print("\nKitabi alacak Ogrenci ID: ");
        Long studentId;
        try {
            studentId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hatali ID girisi.");
            return;
        }

        Student student = studentDao.getById(studentId);

        if (student == null) {
            System.out.println("Hata: Ogrenci bulunamadi.");
            return;
        }

        Loan loan = new Loan(student, book);
        loanDao.save(loan);

        // Kitap durumunu güncelle
        book.setStatus(Book.Status.BORROWED);
        bookDao.update(book);

        System.out.println("Odunc verme islemi tamamlandi.");
    }

    // 6. ÖDÜNÇ LİSTESİ
    private static void listLoans() {
        System.out.println("\n--- Odunc Hareketleri ---");
        List<Loan> loans = loanDao.getAll();

        if (loans.isEmpty()) {
            System.out.println("Hicbir odunc kaydi bulunmamaktadir.");
        } else {
            for (Loan l : loans) {
                String durum = (l.getReturnDate() == null) ? "Teslim Edilmedi" : "Teslim Tarihi: " + l.getReturnDate();
                System.out.println("Islem ID: " + l.getId() +
                        " | Kitap: " + l.getBook().getTitle() +
                        " | Ogrenci: " + l.getStudent().getName() +
                        " | Verilis: " + l.getBorrowDate() +
                        " | Durum: " + durum);
            }
        }
    }

    // 7. İADE ALMA İŞLEMİ
    private static void returnBook() {
        System.out.println("\n--- Kitap Iade Islemi ---");
        System.out.print("Iade edilecek Kitap ID: ");
        Long bookId;
        try {
            bookId = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Hatali giris.");
            return;
        }

        Book book = bookDao.getById(bookId);
        if (book == null) {
            System.out.println("Kitap bulunamadi.");
            return;
        }

        if (book.getStatus() == Book.Status.AVAILABLE) {
            System.out.println("Hata: Bu kitap zaten kutuphanede gorunuyor.");
            return;
        }

        // Aktif ödünç kaydını bul (iade tarihi null olan)
        List<Loan> loans = loanDao.getAll();
        Loan activeLoan = null;

        for (Loan l : loans) {
            if (l.getBook().getId().equals(bookId) && l.getReturnDate() == null) {
                activeLoan = l;
                break;
            }
        }

        if (activeLoan != null) {
            activeLoan.setReturnDate(LocalDate.now());
            loanDao.update(activeLoan);

            book.setStatus(Book.Status.AVAILABLE);
            bookDao.update(book);

            System.out.println("Iade islemi basariyla yapildi.");
        } else {
            System.out.println("Sistem hatasi: Aktif odunc kaydi bulunamadi.");
        }
    }
}