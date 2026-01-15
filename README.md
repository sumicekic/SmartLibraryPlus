Bu proje, Nesneye Dayalı Programlama-II dersi final ödevi için hazırladığım, Hibernate teknolojisi kullanan veritabanı destekli bir kütüphane yönetim uygulamasıdır.

Projenin temel amacı, derste öğrendiğimiz ORM (Object Relational Mapping) yapısını kullanarak SQL kodlarıyla boğuşmadan, nesne mantığıyla veritabanı işlemlerini gerçekleştirmektir.

Proje Maven standartlarına uygun oluşturulduğu için, istenilen entity, dao, util ve app paketleri src/main/java dizini altında; hibernate.cfg.xml dosyası ise src/main/resources içerisinde yer almaktadır.

Çalışma Prensibi Şöyledir:
Bağlantı ve Tablo Kurulumu: Programda HibernateUtil sınıfı kullandım. Program çalıştırıldığında bu sınıf devreye giriyor ve SQLite veritabanı dosyasını (smartlibrary.db) otomatik buluyor. Eğer tablolar (Kitaplar, Öğrenciler, Ödünçler) henüz yoksa, Hibernate bunları kodlarımdaki sınıflara bakarak otomatik oluşturuyor.

Entity (Varlık) Yapısı: Eskiden veriler için tek tek SQL yazardık. Bu projede @Entity yapısı kullandım. Yani yazdığım Book ve Student sınıflarını Hibernate otomatik olarak veritabanı tablosuna dönüştürüyor. Ben kod içinde nesne ekliyorum, arka planda tabloya satır ekleniyor.

DAO (İşçi Sınıflar): Kodlar ana dosyada (Main) birbirine girmesin diye her işi ayırdım.

Kitap işlerini BookDao

Öğrenci işlerini StudentDao

Ödünç alma/verme işlerini LoanDao yapıyor. Bu sayede ana menüde sadece "Kitap Ekle" diyorum, arka plandaki karmaşık işleri bu sınıflar hallediyor.

Menü Sistemi:
Kullanıcının rahat işlem yapabilmesi için konsol ekranında bir menü tasarladım. Switch-Case yapısı ile kullanıcının seçimini (1-7 arası) alıyorum. Sonsuz döngü (while) sayesinde kullanıcı "Çıkış" diyene kadar program kapanmıyor ve işlem yapmaya devam edebiliyorsunuz.

Programın Özellikleri:
Program açılınca veritabanı ve tablolar otomatik kurulur.

Kütüphaneye yeni kitap ve yeni öğrenci ekleyebilirsiniz.

Kitapların ve öğrencilerin listesini görebilirsiniz.

Ödünç Sistemi: Bir kitabı bir öğrenciye ödünç verebilirsiniz. (Sistem, kitap zaten başkasındaysa uyarı verir ve işlemi engeller).

İade Sistemi: Ödünç verilen kitabı geri teslim alabilirsiniz. Sistem otomatik olarak iade tarihini günceller ve kitabı tekrar "Müsait" duruma getirir.

Kurulum ve Çalıştırma:
Projeyi IntelliJ IDEA ile açtıktan sonra Maven'ın kütüphaneleri indirmesini bekleyin. Ardından src/main/java/com/sumicekic/app altındaki Main sınıfını açıp "Run" (Başlat) tuşuna basmanız yeterlidir.

Hazırlayan: Sümeyye Çekiç - 20230108003 - BIP2
