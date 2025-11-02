# Rubrica (Spring Boot)

Applicazione **Spring Boot** per la gestione di una rubrica contatti con **REST API**, database **H2 in-memory**, **cache** (gestita con **Caffeine**) e un piccolo **front-end server-side** con template HTML.

## Requisiti
- **Java 21**
- **Maven** (oppure gli script wrapper `mvnw`/`mvnw.cmd`)
- IDE consigliato: IntelliJ IDEA / VS Code (ricorda di **abilitare Lombok**)

## Avvio rapido
```bash
git clone <https://github.com/Flamsdf/Rubrica.git>
cd rubrica
./mvnw spring-boot:run     # http://localhost:8080
```

## Database (H2)
- URL JDBC: `jdbc:h2:mem:rubricadb`
- Console H2 (se abilitata): `http://localhost:8080/h2-console`  
  - Driver: `org.h2.Driver` – Username: `sa` – Password: *(vuota)*
- Dati di esempio in `src/main/resources/data.sql`.

## Cache
La cache è gestita con **Caffeine**.

## Documentazione API (Swagger/OpenAPI)
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`  
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`  
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

## API REST
| Metodo | Path | Descrizione | Parametri |
|---|---|---|---|
| GET | `/api/contatti` | Lista contatti | – |
| POST | `/api/contatti` | Crea contatto | Body `ContattoRequest` |
| GET | `/api/contatti/{id}` | Dettaglio contatto | `id` |
| PUT | `/api/contatti/{id}` | Aggiorna contatto | `id`, Body `ContattoRequest` |
| DELETE | `/api/contatti/{id}` | Elimina contatto | `id` |
| GET | `/api/contatti/search` | Ricerca esatta | `nome` (opt), `cognome` (opt) |
| GET | `/api/contatti/searchlike` | Ricerca parziale per nome | `nome` (req) |
| GET | `/api/contatti/searchsort` | Ricerca/paginazione/ordinamento | `nome`/`cognome` (opt), `page`, `size`, `sort` (default: `size=10`, `sort=cognome,ASC`) |

## Front-end (pagine server-side)
Gestito da `PageController` con template in `src/main/resources/templates`:
| Metodo | Path | Vista | Note |
|---|---|---|---|
| GET | `/` | `index.html` | Lista contatti (`model: contatti`) |
| GET | `/contatti/{id}` | `contatto.html` \| `not-found.html` | Dettaglio (`model: contatto` o `errorMessage`) |
| GET | `/contatti/{id}/edit` | `edit.html` \| `not-found.html` | Form modifica |
| POST | `/contatti` | redirect `/` | Crea contatto |
| PUT | `/contatti/{id}` | redirect `/` | Aggiorna contatto |
| DELETE | `/contatti/{id}` | redirect `/` | Elimina contatto |

> Le viste usano gli attributi `contatti`, `contatto` ed eventuale `errorMessage`.  
> Template presenti: `index.html`, `contatto.html`, `edit.html`, `not-found.html` (ed eventuale `error.html`).

## Struttura del progetto
```
src/
├─ main/
│  ├─ java/com/example/rubrica/
│  │  ├─ controller/
│  │  │  ├─ web/api/ContattoController.java   # REST API
│  │  │  └─ PageController.java               # pagine server-side
│  │  ├─ dto/ (ContattoRequest.java, ContattoResponse.java)
│  │  ├─ mapper/ (ContattoMapper.java)
│  │  ├─ model/ (Contatto.java)
│  │  ├─ repository/ (ContattoRepository.java)
│  │  ├─ service/ (ContattoService.java)
│  │  └─ RubricaApplication.java
│  └─ resources/
│     ├─ templates/ (index.html, contatto.html, edit.html, not-found.html)
│     ├─ static/
│     ├─ application.properties
│     └─ data.sql
└─ test/
   ├─ java/com/example/rubrica/
   │  ├─ repository/ContattoRepositoryTest.java
   │  └─ RubricaApplicationTests.java
   └─ resources/application-test.properties
```

## Tecnologie
- Spring Boot (Web, Data JPA)
- **Caffeine** (cache)
- H2 Database (in-memory)
- Lombok
- Thymeleaf (template HTML)
- Swagger/OpenAPI (documentazione)
- JUnit

## Note
- Per usare un database esterno (MySQL/PostgreSQL), modifica `application.properties`.
- Se l’IDE non riconosce **Lombok**, installa il plugin e abilita l’annotation processing.
