# 📈 Brokerage API

Spring Boot tabanlı bu API, bir brokerlık firması için müşteri adına hisse alım-satım emirlerinin oluşturulmasını, listelenmesini ve iptal edilmesini sağlar. JWT tabanlı kimlik doğrulama içerir.

---

## 🧰 Teknolojiler

- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA (H2 veritabanı)
- Lombok
- Swagger / OpenAPI (v3)
- Maven

---

## 🔐 Kimlik Doğrulama

Tüm endpoint'ler JWT ile korunmaktadır. Aşağıdaki adımları takip edin:

1. `/auth/register` ile kullanıcı oluşturun
2. `/auth/login` üzerinden JWT token alın
3. Swagger'da `Authorize` butonuna `Bearer <token>` yazın

---

## 🔁 Endpoint'ler

### ✅ Auth

- `POST /auth/register`: Yeni kullanıcı oluşturur
- `POST /auth/login`: JWT token üretir

### 📝 Orders

- `POST /orders`: Yeni bir hisse alım/satım emri oluşturur
- `GET /orders`: Kullanıcının tüm emirlerini listeler
- `DELETE /orders/{id}`: Beklemedeki emri iptal eder

### 📊 Assets

- `GET /assets`: Kullanıcının sahip olduğu varlıkları listeler

---

## 🧪 Testler

- `OrderServiceTest`: Servis katmanı unit testleri
- `AuthControllerTest`: JWT login doğrulama testi

Test çalıştırmak için:
```bash
./mvnw test
