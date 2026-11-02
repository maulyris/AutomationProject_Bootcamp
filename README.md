# Automation Test Framework – Web UI & API

Framework automation testing berbasis **Java + Selenium + Cucumber (Gherkin)** dengan **Gradle**. Web UI test dan API test berada dalam satu repository.

| Jenis | Target | Tag |
|---|---|---|
| Web UI | https://www.demoblaze.com/ | `@web` |
| API | https://dummyapi.io/data/v1 (User & Tag) | `@api` |

## Tools & Library

| Tool | Fungsi |
|---|---|
| Java 17 | Bahasa pemrograman |
| Gradle | Build tool & task runner |
| Cucumber 7 (`cucumber-java`) | BDD / Gherkin |
| `cucumber-picocontainer` | Dependency injection antar step (state per scenario) |
| Selenium 4 | Web UI automation (driver diurus otomatis oleh Selenium Manager) |
| REST Assured 5 + `json-schema-validator` | API testing & validasi response |
| AssertJ | Assertion |
| GitHub Actions | CI |

## Struktur Project

```
.
├── build.gradle                     # dependency + task apiTest & webTest
├── .github/workflows/test.yml       # CI: manual trigger & pull request
└── src/test
    ├── java/com/automation
    │   ├── web
    │   │   ├── pages/               # Page Object Model (BasePage, HomePage, LoginModal, SignUpModal, ProductPage, CartPage)
    │   │   ├── steps/               # Step definitions Web
    │   │   ├── hooks/               # Hooks (start/quit browser, screenshot saat gagal)
    │   │   └── utils/               # WebConfig, DriverFactory
    │   └── api
    │       ├── client/              # DummyApiClient (kumpulan request endpoint)
    │       ├── steps/               # Step definitions API
    │       └── utils/               # ApiConfig, UserDataFactory
    └── resources
        ├── features/web/            # Feature Gherkin Web (@web)
        ├── features/api/            # Feature Gherkin API (@api)
        └── schemas/                 # JSON Schema untuk validasi response API
```

## Cakupan Skenario

**Web (Demoblaze)** – sign up, login, logout, login gagal (password salah, user tidak ada, field kosong), sign up user duplikat, filter kategori, add to cart, multiple product + validasi total, hapus item cart, checkout sukses & gagal.

**API (DummyAPI)** – Get User by ID, Create User, Update User, Delete User, Get List of Tags; masing-masing positif + negatif (404, 400, 403 tanpa `app-id`).

### Validasi response API
1. **Status code**
2. **JSON Schema** (`src/test/resources/schemas`)
3. **Isi field** (id, nama, email sama dengan request; `updatedDate >= registerDate`)
4. **Content-Type**, field error (`RESOURCE_NOT_FOUND`, `PARAMS_NOT_VALID`, `BODY_NOT_VALID`, `APP_ID_MISSING`)
5. **Verifikasi efek** (user yang sudah di-delete → GET mengembalikan 404)

## Cara Menjalankan

Prasyarat: **JDK 17+**, **Gradle 8.x** (atau jalankan `gradle wrapper` sekali untuk membuat `gradlew`), dan Google Chrome (untuk Web test).

```bash
gradle apiTest     # semua scenario @api
gradle webTest     # semua scenario @web
```

Opsi Web:

```bash
gradle webTest -Pheadless=true            # tanpa tampilan browser
gradle webTest -Pbrowser=firefox          # chrome (default) | firefox | edge
```

API `app-id` default memakai nilai dari soal; untuk mengganti: set env `APP_ID`.

```powershell
$env:APP_ID="your-app-id"; gradle apiTest
```

## Report

Dihasilkan otomatis setiap task dijalankan:

| Task | HTML | JSON |
|---|---|---|
| `apiTest` | `build/reports/cucumber/api/cucumber.html` | `build/reports/cucumber/api/cucumber.json` |
| `webTest` | `build/reports/cucumber/web/cucumber.html` | `build/reports/cucumber/web/cucumber.json` |

Screenshot otomatis dilampirkan di report saat scenario Web gagal.

## GitHub Actions

Workflow: [.github/workflows/test.yml](.github/workflows/test.yml)

- **Trigger**: `workflow_dispatch` (manual, bisa memilih `all`/`api`/`web`) dan `pull_request`.
- Job `api-test` menjalankan `gradle apiTest`, job `web-test` menjalankan `gradle webTest` (headless Chrome).
- Report HTML + JSON di-upload sebagai artifact.
- Opsional: simpan app-id di repository secret `DUMMYAPI_APP_ID`; jika tidak ada, default dipakai.

## Catatan
- DummyAPI & Demoblaze adalah layanan publik, sehingga bisa sesekali lambat/tidak stabil. Test data (username, email) dibuat unik secara dinamis.
- Jika Demoblaze tidak bisa diakses, ganti `-PbaseUrl` dan page object ke https://www.saucedemo.com/.
