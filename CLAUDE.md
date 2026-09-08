# spring-boot-api-starter-free

Nome del prodotto: **Spring Boot API Starter - Free Edition**. È la
repo pubblica gratuita del modello open-core: vetrina tecnica e canale
verso la **Premium Edition** (repo privata `spring-boot-api-starter-premium`).
Il repository mantiene il nome `spring-boot-api-starter-free` — non va
rinominato senza richiesta esplicita, per non rompere URL e link già
pubblicati.

## Confine free / premium (regola di prodotto)

La free edition **non deve** contenere: autenticazione, JWT, refresh
token, ruoli/permessi, PostgreSQL, Docker, Flyway/Liquibase, profili
`dev`/`test`/`prod`, CI/CD completa, logging strutturato, rate
limiting, email, upload, pagamenti, frontend. Queste cose vivono nella
premium (vedi `docs/PREMIUM-ROADMAP.md`). Se una richiesta le
introduce qui, fermarsi e chiedere: il confine è una scelta di
business, non tecnica.

Stack fisso: Java 21, Spring Boot 3.x, Maven, H2 in-memory,
springdoc-openapi. Niente dipendenze aggiunte "per comodità".

## Lingua

Repo pubblica internazionale: codice, commenti, README, `docs/*`
rivolti agli utenti, `CONTRIBUTING.md`, `CHANGELOG.md`, messaggi di
commit e PR **in inglese**. Questo file e `docs/STATO_SVILUPPO.md`
sono in italiano perché servono a Fabrizio e all'assistente.

## Git Flow

Questo progetto segue il Git Flow classico (Vincent Driessen):

> ⚠️ Eccezione: le modifiche **100% documentazione** (soli file `.md`)
> non richiedono release — vedi "Lightweight process for
> documentation-only changes" in `CONTRIBUTING.md`: branch `docs/*`
> da `develop`, PR dirette sia verso `master` sia verso `develop`,
> nessun bump di versione né voce di changelog.

- **feature/***: branch da `develop`, PR/merge di ritorno in `develop`.
  I nomi dei branch seguono **sempre** queste convenzioni (`feature/*`,
  `release/x.y.z`, `hotfix/*`, `docs/*`), anche quando l'ambiente di
  lavoro (es. sessione remota Claude) propone un nome di branch
  proprio tipo `claude/*`: quel nome non va usato per il lavoro vero.
- **release/***: branch da `develop` per preparare una nuova versione
  (solo bugfix minori, bump versione, changelog — niente nuove
  feature); al completamento, merge sia in `master` (con tag di
  versione) sia in `develop`
- **hotfix/***: branch da `master` per bug critici in produzione;
  merge esplicito sia in `master` (nuovo tag patch) sia in `develop`
- `master` rappresenta sempre lo stato in produzione, taggato ad ogni
  release/hotfix. Ogni tag ha anche una **release GitHub** con le note
  di rilascio (la v1.0.0 esiste già).

### Comandi di fine release

Se l'ambiente di lavoro dell'assistente non può eseguire il push dei
tag o la cancellazione dei ref remoti (tipico delle sessioni remote
con proxy git: verificarlo alla prima occasione, non assumerlo), quei
comandi vanno consegnati all'utente da lanciare in locale.

I blocchi di comandi destinati al copia-incolla vanno consegnati
**puliti, senza commenti dentro**: le spiegazioni (quale SHA è stato
scelto e perché, cosa fa ogni passo) stanno nel testo prima o dopo il
blocco, mai tra i comandi. Il blocco di fine release è:

```bash
git fetch origin master
git tag -a vX.Y.Z <sha> -m "Release vX.Y.Z"
git push origin vX.Y.Z
git ls-remote --tags origin | grep vX.Y.Z
git push origin --delete <branch-di-lavoro> release/X.Y.Z
git fetch --prune origin
git checkout develop && git pull && git checkout master && git pull
```

dove `<sha>` è il commit di merge della PR release/X.Y.Z su `master`
(indicato esplicitamente nel testo che accompagna il blocco); il
`ls-remote` in mezzo serve come conferma visiva del tag appena creato.
Dopo il tag: `gh release create vX.Y.Z --verify-tag --title
"vX.Y.Z - Free Edition" --notes-file <file>` con le note prese dal
`CHANGELOG.md`.

Regole per l'assistente quando consegna questo blocco:

- **Sempre lo SHA esplicito**, mai un segnaposto: va indicato il
  commit esatto da taggare, dopo averlo verificato su `origin/master`.
- **Controllare prima lo stato remoto** (`git ls-remote --tags origin`,
  `git ls-remote --heads origin`): se il tag esiste già o i branch
  sono già stati cancellati, i relativi comandi vanno omessi, così
  l'utente non riceve comandi destinati a fallire.
- **Il blocco deve riportare dove si era**: mai lasciare l'utente su
  un branch che non ha scelto. Il `git pull` finale va **senza remoto
  esplicito**: ogni branch ha già il proprio upstream configurato.
- Il blocco va riproposto a **ogni** release, non solo la prima volta.

## Stato remoto: non fidarsi di quello locale

In sessione remota il clone può essere **shallow** e i ref locali
fermi a prima. Ogni affermazione sullo stato del repository va
verificata sul remoto, mai dedotta da comandi che leggono solo il
locale:

- `git ls-remote` per branch e tag, che interroga il remoto;
- `git fetch --unshallow` prima di qualunque ragionamento sulla
  storia: `git merge-base --is-ancestor` fallisce silenziosamente se
  il commit non è nel clone, e il risultato sembra un "no" legittimo;
- per dire che un branch è cancellabile: `git rev-list --count
  origin/master..origin/<branch>` uguale a zero, non l'impressione che
  sia stato mergiato.

## Prima di scegliere un numero di versione

Fra l'inizio di un lavoro e il suo rilascio possono essere uscite
altre versioni (altri contributori, altre sessioni). Prima di
preparare una release vanno controllati sul remoto:

- l'ultima versione realmente rilasciata (in cima al `CHANGELOG.md`
  di `origin/master` e in `git ls-remote --tags origin`, non quello
  che si ricorda);
- l'esistenza di un `release/X.Y.Z` già aperto da altri. Se c'è,
  **non ci si scrive sopra**: si prende il numero successivo.

Che un ref non sia stato creato dalla sessione corrente è la norma,
non un'anomalia: quel che conta è la coerenza dello stato — ogni tag
su un commit di `master`, `master` e `develop` non divergenti, nessun
branch orfano — non la paternità.

## Dove vive la versione

La versione vive in **tre punti** che il branch di release deve
allineare nello stesso commit:

1. `pom.xml` → `<version>X.Y.Z</version>` del progetto (fonte di
   verità per Maven);
2. `CHANGELOG.md` → voce `## [vX.Y.Z] - YYYY-MM-DD` in cima;
3. `src/main/java/com/phabdev/apistarter/config/OpenApiConfig.java` →
   `.version("X.Y.Z")` mostrata in Swagger UI.

Il tag git `vX.Y.Z` e la release GitHub seguono il merge su `master`.
Niente `-SNAPSHOT` su `master`; su `develop` la versione resta quella
dell'ultima release finché non si apre il branch di release.

## Verifiche prima di dichiarare pronto

Toolchain: JDK 21 e Maven 3.9+ (o `./mvnw`, il wrapper è nel repo).
Se sulla macchina non c'è un JDK nel PATH, cercarne uno prima di
installare qualcosa; su questo PC ne è stato usato uno portatile in
`%TEMP%\sbtools` (può non esistere più).

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Con l'app avviata devono rispondere `http://localhost:8080/api/tasks`,
`/swagger-ui/index.html`, `/v3/api-docs`, `/h2-console`, e devono
funzionare gli esempi curl del README (201 con `Location`, 400 con
`fieldErrors`, 404, 204). **Fermare il processo Java alla fine**: un
server lasciato vivo sulla 8080 falsa le verifiche successive.

## Commit messages

Usare i [Conventional Commits](https://www.conventionalcommits.org/):
`<tipo>(<scope opzionale>): <descrizione>`, **in inglese** (eccezione
consapevole rispetto agli altri repo PHABDEV: repo pubblica
internazionale). Tipi principali: `feat`, `fix`, `docs`, `chore`,
`refactor`, `test`, `style`, `ci`.
