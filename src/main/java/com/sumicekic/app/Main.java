package com.sumicekic.app;

import com.sumicekic.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {
        System.out.println("-------------------------------------");
        System.out.println(" Veritabanı deneniyor.");

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = session.beginTransaction();

        System.out.println("Veritabanı oluşturuldu.");
        System.out.println("-------------------------------------");

        transaction.commit();
        session.close();
        HibernateUtil.shutdown();
    }
}