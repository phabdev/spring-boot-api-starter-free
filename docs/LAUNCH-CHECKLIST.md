# Launch checklist

Concrete steps to publish `spring-boot-api-starter-free` and prepare the Premium Edition sale. Tick items as you go.

## 1. Technical checks

- [ ] `mvn clean test` passes on a clean clone (delete `~/.m2` cache is not required, a fresh `git clone` is)
- [ ] `mvn spring-boot:run` starts without warnings that matter
- [ ] `GET http://localhost:8080/api/tasks` returns `[]`
- [ ] Swagger UI loads at `http://localhost:8080/swagger-ui/index.html`
- [ ] H2 console loads and connects with `jdbc:h2:mem:apistarter`
- [ ] All curl examples in the README work as written
- [ ] No secrets, tokens or personal data in the repository (`git grep -i password`, `git grep -i secret`)
- [ ] `.gitignore` excludes `target/` and IDE files
- [ ] Dependency versions are current (Spring Boot 3.x, springdoc)
- [ ] Java 21 is the only JDK requirement stated and enforced

## 2. README checks

- [ ] Title and one-line description match the OpenAPI metadata
- [ ] "What's included" and "What's not included" are accurate for the code that is actually there
- [ ] Quick start works copy-paste
- [ ] Endpoint table matches the controller
- [ ] Error format example matches a real response
- [ ] Premium section is present with the placeholder link
- [ ] Clone URL points to the real GitHub repository
- [ ] English, no typos, no marketing tone

## 3. License checks

- [ ] `LICENSE` is MIT with the correct year and name
- [ ] `pom.xml` declares the MIT license
- [ ] README says MIT for the Free Edition and proprietary for Premium
- [ ] `docs/PREMIUM-ROADMAP.md` contains the draft premium license text

## 4. GitHub repository

- [ ] Create public repository `spring-boot-api-starter-free`
- [ ] Description: "A clean and minimal Spring Boot 3 REST API starter template (Java 21, Maven, H2, Swagger)"
- [ ] Topics: `spring-boot`, `java`, `rest-api`, `starter-template`, `openapi`, `swagger`, `h2`, `maven`, `java-21`
- [ ] Push `master` and `develop` branches
- [ ] Enable Issues, disable Wiki and Projects
- [ ] Add a minimal GitHub Actions workflow running `mvn -B clean test` on push (optional, keep it tiny)
- [ ] Create release `v1.0.0` from the tag with a short changelog
- [ ] Pin the repository on the GitHub profile

## 5. Content

### LinkedIn post

- [ ] Short post: what it is, who it is for, what it deliberately leaves out, link to the repo
- [ ] One screenshot of Swagger UI or of the project structure
- [ ] Mention that a premium edition is coming, without a hard sell

### Blog article (phabdev.com)

- [ ] Title idea: "A minimal Spring Boot REST API starter, and what I left out on purpose"
- [ ] Explain the structure and the error handling choices
- [ ] Link to the repository and to the premium roadmap

### Short demo video

- [ ] 3-5 minutes: clone, `mvn spring-boot:run`, Swagger UI, one POST, one validation error
- [ ] No editing beyond trimming, screen only
- [ ] Publish on YouTube (unlisted is fine at first) and link it from the README

## 6. Premium product preparation

- [ ] Create private repository `spring-boot-api-starter-premium`
- [ ] Build the premium features listed in `docs/PREMIUM-ROADMAP.md`
- [ ] Write the premium README, adaptation guide and deploy checklist
- [ ] Finalize the proprietary license text
- [ ] Prepare a zip export process (script or GitHub release on the private repo)

## 7. Sales platform

- [ ] Choose one platform: Gumroad, Lemon Squeezy or Paddle (check VAT handling for EU sales)
- [ ] Create the product using `docs/PRODUCT-PAGE-DRAFT.md`
- [ ] Set price to 29 EUR
- [ ] Set delivery: zip download or private repo invitation instructions
- [ ] Test a purchase end to end with a test account
- [ ] Replace the placeholder `Premium Edition link coming soon` in the README with the real link
- [ ] Tag a new free-edition release after updating the link

## 8. Feedback loop

- [ ] Add a "Feedback" section or issue template to the free repository
- [ ] Ask 3-5 developers to try the free edition and report friction
- [ ] Track: stars, clones, issues, page views on the product page, conversions
- [ ] Review after 30 days: adjust README, price, or premium scope based on real feedback
- [ ] Decide whether to start `angular-admin-starter-free` or improve premium first
