# Documentatie – Rental Platform

Deze documentatie beschrijft de belangrijkste onderdelen en functies van het Rental Platform.
De uitleg focust op de kernlogica en vermeldt expliciet de gebruikte functies (methoden),
zodat een developer snel begrijpt waar welke functionaliteit geïmplementeerd is.

---

## 1. Algemene werking van de applicatie

Het Rental Platform is een webapplicatie waarmee gebruikers materiaal kunnen reserveren
voor een bepaalde periode.

Globale flow:
1. Registratie van een gebruiker via `AuthController.handleRegister(...)`
2. Login via Spring Security en `SecurityConfig`
3. Producten bekijken via `CatalogController.showCatalog(...)`
4. Producten toevoegen aan de winkelmand via `CartService.addToCart(...)`
5. Einddatum instellen via `CartService.updateEndDate(...)`
6. Checkout en reservatie bevestigen via `OrderService.createOrderFromCart(...)`

---

## 2. Authenticatie en registratie

### 2.1 Registratie

Registratie gebeurt via de functie:

- `AuthController.handleRegister(String username, String email, String password, Model model)`

Werking:
- Controle op bestaande gebruikersnaam via `UserRepository.existsByUsername(...)`
- Hashen van het wachtwoord via `PasswordEncoder.encode(...)`
- Opslaan van de gebruiker via `UserRepository.save(...)`
- Redirect naar loginpagina bij succes

---

### 2.2 Login

Login wordt afgehandeld door Spring Security.

Betrokken onderdelen:
- `SecurityConfig.securityFilterChain(...)`
- Loginpagina via `AuthController.showLoginForm()`

Kenmerken:
- Form-based authentication
- BCrypt hashing
- Redirect na login naar `/dashboard`

---

### 2.3 Security en toegangscontrole

Security wordt centraal geconfigureerd in:
- `SecurityConfig.securityFilterChain(HttpSecurity http)`

Belangrijke regels:
- `/cart/**` → enkel toegankelijk voor ingelogde gebruikers
- `/orders/**` → enkel toegankelijk voor ingelogde gebruikers
- CSRF-bescherming actief voor POST-verzoeken
- H2-console expliciet uitgezonderd van CSRF

---

## 3. Catalogus en filtering

De catalogus wordt weergegeven via:

- `CatalogController.showCatalog(Long categoryId, String search, Model model)`

### 3.1 Filtering op categorie

Als `categoryId` aanwezig is:
- categorie ophalen via `CategoryRepository.findById(...)`
- producten ophalen via `ProductRepository.findByCategory(...)`

De geselecteerde categorie wordt teruggegeven aan de view via:
- `model.addAttribute("selectedCategoryId", categoryId)`

---

### 3.2 Zoeken op productnaam

Als geen categorie geselecteerd is en `search` niet leeg is:
- zoeken via `ProductRepository.findByNameContainingIgnoreCase(...)`

Zoekterm blijft zichtbaar via:
- `model.addAttribute("search", search)`

---

## 4. Winkelmand (Cart)

### 4.1 Persistente winkelmand

De winkelmand is persistent en gekoppeld aan een gebruiker.

De gebruiker wordt opgehaald via:
- `CartService.getCurrentUserOrNull()`

Cart-items worden opgeslagen en opgehaald via:
- `CartItemRepository.findByUser(...)`

---

### 4.2 Product toevoegen aan de winkelmand

Functie:
- `CartService.addToCart(Product product, HttpSession session)`

Werking:
- Bestaand cart-item ophalen via `CartItemRepository.findByUserAndProduct(...)`
- Quantity verhogen of nieuw `CartItem` aanmaken

---

### 4.3 Product verwijderen uit de winkelmand

Functie:
- `CartService.removeFromCart(Long productId, HttpSession session)`

Verwijdert het volledige product uit de cart via:
- `CartItemRepository.deleteByUserAndProduct_Id(...)`

---

### 4.4 Einddatum aanpassen per product

Functie:
- `CartService.updateEndDate(Long productId, LocalDate endDate, HttpSession session)`

Werking:
- Ophalen van cart-items via `CartItemRepository.findByUser(...)`
- Einddatum per item aanpassen en opslaan

---

### 4.5 Totaalprijs berekenen

Functies:
- `CartService.calculateTotal(HttpSession session)`
- `CartService.getDays(LocalDate endDate)`

Berekening:
- `dailyPrice * quantity * aantal dagen`
- Minimum van 1 dag wordt afgedwongen in `getDays(...)`

---

### 4.6 Aantal items in winkelmand

Functie:
- `CartService.getCartCount(HttpSession session)`

Som van alle quantities via:
- stream over `CartItemRepository.findByUser(...)`

---

### 4.7 Winkelmand leegmaken

Functie:
- `CartService.clearCart(HttpSession session)`

Verwijdert alle cart-items van de gebruiker via:
- `CartItemRepository.deleteByUser(...)`

---

## 5. Checkout en reservaties (Orders)

### 5.1 Reservatie aanmaken vanuit winkelmand

Hoofdfunctie:
- `OrderService.createOrderFromCart(AppUser user, HttpSession session)`

Stappen:
1. Cart ophalen via `CartService.getAllItems(...)`
2. Stock controleren per product
3. Stock aanpassen via `ProductRepository.save(...)`
4. `OrderItem` aanmaken en koppelen
5. Prijs berekenen per item
6. `RentalOrder` opslaan via `RentalOrderRepository.save(...)`
7. Cart leegmaken via `CartService.clearCart(...)`

---

### 5.2 Bevestigen van een reservatie

Functie:
- `OrderController.confirmOrder(AppUser user, HttpSession session)`

Werking:
- Roept `OrderService.createOrderFromCart(...)` aan
- Redirect naar detailpagina van de reservatie

---

### 5.3 Overzicht van reservaties

Functie:
- `OrderController.listOrders(AppUser user, Model model)`

Ophalen van reservaties via:
- `OrderService.getOrdersForUser(AppUser user)`

---

### 5.4 Detail van één reservatie

Functie:
- `OrderController.orderDetails(Long orderId, AppUser user, Model model)`

Beveiliging:
- Reservatie ophalen via `OrderService.getOrderForUser(...)`
- Enkel toegankelijk als de reservatie toebehoort aan de gebruiker

---

## 6. Stockbeheer

Stockcontrole gebeurt in:
- `OrderService.createOrderFromCart(...)`

Specifieke stappen:
- Controle via `p.getStock() < qty`
- Stock verminderen via `p.setStock(...)`
- Opslaan via `ProductRepository.save(...)`

---

## 7. Database en persistentie

De applicatie gebruikt een file-based H2 database.

Configuratie:
- JDBC URL: `jdbc:h2:file:./data/rentaldb`

Belangrijke entiteiten:
- `AppUser`
- `Product`
- `Category`
- `CartItem`
- `RentalOrder`
- `OrderItem`

---

## 8. Overzicht kernbestanden

- `SecurityConfig.java` → security, CSRF, toegangsregels
- `AuthController.java` → registratie en login
- `CatalogController.java` → catalogus en filtering
- `CartService.java` → winkelmandlogica en prijsberekening
- `CartController.java` → cart-routes en checkout
- `OrderService.java` → aanmaken van reservaties
- `OrderController.java` → bevestigen en bekijken van reservaties
