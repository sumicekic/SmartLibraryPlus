package com.sumicekic.dao;

import com.sumicekic.entity.Loan;
import com.sumicekic.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LoanDao {

    public void save(Loan loan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(loan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Loan loan) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(loan);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Loan> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Ödünç listesini çekerken Kitap ve Öğrenci bilgilerini de getirir
            return session.createQuery("from Loan", Loan.class).list();
        }
    }

    // Öğrenciye ait aktif (iade edilmemiş) ödünç kaydını bulmak için
    public List<Loan> getActiveLoansByStudentId(Long studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Loan L WHERE L.student.id = :studentId AND L.returnDate IS NULL";
            return session.createQuery(hql, Loan.class)
                    .setParameter("studentId", studentId)
                    .list();
        }
    }
}