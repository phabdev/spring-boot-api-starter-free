# Stato di sviluppo — spring-boot-api-starter-free

> Documento vivo: aggiornarlo ad ogni release/hotfix, non ad ogni
> commit. Scopo: dare a qualunque sessione futura (Claude o altro) il
> contesto minimo per continuare senza dover ripercorrere tutta la
> cronologia. È in italiano perché serve a Fabrizio e all'assistente;
> tutto il resto del repo è in inglese.

## Versione corrente

- Ultima release: **v1.0.0** (9 settembre 2026, prima release
  pubblica: CRUD `Task`, DTO record, validazione, error handler, H2,
  Swagger, test, README e docs di prodotto). Release GitHub
  pubblicata con note di rilascio.
- Release precedente: nessuna
- Ultimo hotfix: nessuno
- Semver: `x.y.z` — `y` per nuove feature (resetta `z`), `z` per
  bugfix, `x` per breaking change

## Architettura

Stack (versioni verificate su Maven Central il 9 settembre 2026):

- Java 21 (Temurin 21.0.12.1 usato per la verifica), Maven 3.9.9 via
  wrapper
- Spring Boot **3.5.16** (ultima della linea 3.x; esiste già la 4.x,
  non adottata perché il brief di prodotto chiede la 3.x)
- springdoc-openapi-starter-webmvc-ui **2.8.17**
- H2 in-memory (`jdbc:h2:mem:apistarter`), Hibernate `ddl-auto:
  create-drop`
- Test: JUnit 5, Mockito (caricato come Java agent via surefire per
  evitare i warning JDK 21), AssertJ, MockMvc

Struttura: un package per risorsa (`task`) con entity, enum,
repository, service, controller, `dto/` e `mapper/`; `common/error`
per l'error handling trasversale; `common/web/ApiPaths` per le route;
`config/OpenApiConfig` per i metadata Swagger. Il service restituisce
DTO, il mapper è scritto a mano (niente MapStruct).

Scelte di prodotto: la free edition è **volutamente incompleta** (vedi
il confine free/premium in `CLAUDE.md` e `docs/PREMIUM-ROADMAP.md`).

## Infrastruttura e servizi

> Solo nomi/URL/convenzioni — nessun secret qui.

- **GitHub**: repo pubblico `phabdev/spring-boot-api-starter-free`,
  licenza MIT. Branch principali: `master` (produzione, taggato ad
  ogni release/hotfix), `develop` (integrazione). Convenzione branch:
  `feature/*`, `release/x.y.z`, `hotfix/*`, `docs/*` (Git Flow
  classico, vedi `CLAUDE.md`). Issues attive, Wiki e Projects
  disattivati. Topics: spring-boot, java, java-21, rest-api,
  starter-template, openapi, swagger, h2, maven.
- Nessun deploy: è un template da clonare, non un servizio.
- Nessuna CI: volutamente fuori dalla free edition (al massimo un
  workflow minimale `mvn -B clean test`, vedi backlog).

## Backlog e priorità

1. **Prodotto**: avviare la repo privata `spring-boot-api-starter-premium`
   (JWT + refresh token, ruoli, PostgreSQL + Flyway, Docker Compose,
   profili, Testcontainers, Postman, docs premium). Roadmap completa in
   `docs/PREMIUM-ROADMAP.md`; checklist di lancio in
   `docs/LAUNCH-CHECKLIST.md`.
2. **Lancio free**: pin del repo sul profilo GitHub, post LinkedIn,
   articolo su phabdev.com, breve video demo.
3. **Vendita**: scegliere la piattaforma (Gumroad / Lemon Squeezy /
   Paddle), sostituire il placeholder "Premium Edition link coming
   soon" nel README con il link reale (modifica `.md` → processo
   leggero docs).
4. **Tecnico, minore**: la versione è duplicata in `pom.xml` e in
   `OpenApiConfig` (`.version("1.0.0")`); valutare di leggerla da
   `build-info` (`spring-boot-maven-plugin` goal `build-info` +
   `BuildProperties`) alla prossima release minor. Valutare un
   workflow GitHub Actions minimale.

## Incidenti noti e fix

Nessun incidente. Nota operativa: la prima verifica del 9 settembre
2026 è stata fatta con JDK e Maven portatili perché sul PC di sviluppo
non c'era Java nel PATH; il Maven Wrapper è stato aggiunto proprio per
ridurre i prerequisiti a solo JDK 21.
