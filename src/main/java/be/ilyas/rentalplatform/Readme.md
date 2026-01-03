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

## Bronnen

- Java Brains – Spring Security Authentication & Authorization  
  https://www.youtube.com/watch?v=PhG5p_yv0zs
- Amigoscode – Spring Boot Security Full Course  
https://www.youtube.com/watch?v=her_7pa0vrg
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Spring Security Reference: https://spring.io/projects/spring-security
- Thymeleaf Documentation: https://www.thymeleaf.org/documentation.html
- H2 Database Documentation: https://www.h2database.com

## Gebruik van AI-tools

Tijdens de ontwikkeling van dit project werd gebruikgemaakt van AI-ondersteuning in de vorm van **GitHub Copilot**.

GitHub Copilot werd ingezet als:
- hulpmiddel voor code-suggesties in de IDE
- ondersteuning bij het schrijven van standaardstructuren (controllers, services, repositories)
- hulp bij het begrijpen en verbeteren van bestaande code
- Troubleshooting van runtime errors (zoals 500 Internal Server Errors)
- Verbeteren van error handling (null-checks, validaties en veilige fallbacks)
- verfijnen van service- en controllerlogica

GitHub Copilot werkt contextueel binnen de code-editor en maakt **geen gebruik van een afzonderlijke chatinterface**.  
Hierdoor is het **niet mogelijk om een exporteerbare chatgeschiedenis of exacte prompts te delen**.

Alle gegenereerde suggesties werden steeds:
- kritisch geëvalueerd
- aangepast waar nodig
- volledig begrepen en geïntegreerd door de student zelf

De uiteindelijke implementatie, structuur en logica van het project zijn mijn eigen keuzes en verantwoordelijkheid.

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

