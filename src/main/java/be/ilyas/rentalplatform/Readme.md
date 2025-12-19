# 🎬 Rental Platform – Materiaalreservatiesysteem

Dit project is een **webapplicatie voor het reserveren van materiaal** (zoals kabels, belichting en controlepanelen).  
Gebruikers kunnen items toevoegen aan een winkelmandje, per item een einddatum kiezen en een reservatie bevestigen.  
Alle reservaties zijn nadien raadpleegbaar via het dashboard en detailpagina’s.

---

## 📌 Functionaliteiten

### Voor gebruikers
- Registreren en inloggen
- Materiaal bekijken per categorie
- Items toevoegen aan een winkelmandje
- Per item een **einddatum** kiezen
- Automatische **prijsberekening per dag**
- Reservatie bevestigen
- Overzicht van alle reservaties
- Detailpagina per reservatie
- Dashboard met:
    - Aantal items in mandje
    - Aantal reservaties

### Automatisch
- Voorraad wordt verminderd bij reservatie
- Winkelmandje wordt leeggemaakt na bevestiging
- Data wordt automatisch geïnitialiseerd bij eerste start

---

## 🛠️ Technologieën

| Technologie | Gebruik |
|------------|--------|
| Java 17+ | Backend |
| Spring Boot 3 | Framework |
| Spring Data JPA | Database |
| Spring Security | Authenticatie |
| Thymeleaf | Templates |
| H2 Database | In-memory database |
| Hibernate | ORM |
| Maven | Build tool |

---

## 🗂️ Projectstructuur

# 🎬 Rental Platform – Materiaalreservatiesysteem

Dit project is een **webapplicatie voor het reserveren van materiaal** (zoals kabels, belichting en controlepanelen).  
Gebruikers kunnen items toevoegen aan een winkelmandje, per item een einddatum kiezen en een reservatie bevestigen.  
Alle reservaties zijn nadien raadpleegbaar via het dashboard en detailpagina’s.

---

## 📌 Functionaliteiten

### Voor gebruikers
- Registreren en inloggen
- Materiaal bekijken per categorie
- Items toevoegen aan een winkelmandje
- Per item een **einddatum** kiezen
- Automatische **prijsberekening per dag**
- Reservatie bevestigen
- Overzicht van alle reservaties
- Detailpagina per reservatie
- Dashboard met:
    - Aantal items in mandje
    - Aantal reservaties

### Automatisch
- Voorraad wordt verminderd bij reservatie
- Winkelmandje wordt leeggemaakt na bevestiging
- Data wordt automatisch geïnitialiseerd bij eerste start

---

## 🛠️ Technologieën

| Technologie | Gebruik |
|------------|--------|
| Java 17+ | Backend |
| Spring Boot 3 | Framework |
| Spring Data JPA | Database |
| Spring Security | Authenticatie |
| Thymeleaf | Templates |
| H2 Database | In-memory database |
| Hibernate | ORM |
| Maven | Build tool |

---
## 🧩 Datamodel

### Product
- name
- description
- dailyPrice
- stock
- category

### CartItem (session-based)
- product
- quantity
- endDate

### RentalOrder
- user
- createdAt
- totalPrice
- totalItems
- items (OrderItem)

### OrderItem
- product
- quantity
- endDate
- order

---

## 🛒 Winkelmandje

- Winkelmandje wordt opgeslagen in de **HTTP session**
- Elk item bevat een product, aantal en einddatum
- Prijsberekening: ## 🧩 Datamodel

### Product
- name
- description
- dailyPrice
- stock
- category

### CartItem (session-based)
- product
- quantity
- endDate

### RentalOrder
- user
- createdAt
- totalPrice
- totalItems
- items (OrderItem)

### OrderItem
- product
- quantity
- endDate
- order

---

## 🛒 Winkelmandje

- Winkelmandje wordt opgeslagen in de **HTTP session**
- Elk item bevat een product, aantal en einddatum
- Prijsberekening:prijs = dailyPrice × quantity × aantal dagen

---

## 📦 Reservatieproces

1. Gebruiker voegt items toe aan mandje
2. Gebruiker kiest per item een einddatum
3. Gebruiker bevestigt reservatie
4. `OrderService.createOrderFromCart()`:
- Controleert voorraad
- Vermindert voorraad
- Maakt `RentalOrder`
- Maakt `OrderItem` per item
- Berekent totaalprijs
- Slaat alles op
- Leegt winkelmandje
5. Gebruiker ziet reservatie in overzicht

---


## 📊 Dashboard

Het dashboard toont:
- **Items in mandje** via `CartService`
- **Aantal reservaties** via `RentalOrderRepository`

Deze waarden worden toegevoegd aan het model in de controller.

---

## 🔐 Security

De applicatie maakt gebruik van **Spring Security** voor authenticatie en autorisatie.

### Functionaliteit
- Gebruikers kunnen zich **registreren en inloggen**
- Elke **reservatie is gekoppeld aan één gebruiker**
- Een gebruiker kan **enkel zijn eigen reservaties bekijken**
- Beveiligde pagina’s zijn enkel toegankelijk na login

---
## 🚀 Project starten

### Vereisten
- **Java 17 of hoger**
- **Maven**
- Een IDE (bijvoorbeeld IntelliJ of VS Code)

### Starten van de applicatie

Open het project in je IDE en voer het volgende commando uit:
mvn spring-boot:run



## 🧪 Database (H2)

### Configuratie
```properties
spring.datasource.url=jdbc:h2:mem:rentaldb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

