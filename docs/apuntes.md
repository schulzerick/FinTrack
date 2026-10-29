# Cuaderno de apuntes — FinTrack

Cuaderno de estudio del proyecto final: una app de finanzas personales con Kotlin Multiplatform (Android + Desktop). Se actualiza al cerrar cada bloque. Cada apartado explica **qué** se hizo y, sobre todo, **por qué**.

Documentos relacionados: [planificacion.md](planificacion.md) · [tests/](tests/)

---

## Bloque 0 — Estructura de un proyecto KMP

**Idea central.** Kotlin Multiplatform permite escribir el código una vez y reutilizarlo en varias plataformas. Solo lo que es específico de cada sistema se escribe aparte.

**Los tres módulos del proyecto**

| Módulo | Qué contiene |
|---|---|
| `shared` | Casi todo: lógica, modelos, base de datos y la interfaz con Compose |
| `androidApp` | Un lanzador mínimo de Android (`MainActivity`) |
| `desktopApp` | Un lanzador mínimo de escritorio (`main.kt`) |

**Source sets dentro de `shared`:** `commonMain` (código compartido), `androidMain` (solo Android), `jvmMain` (Desktop; en KMP "Desktop" es el destino `jvm`) y sus equivalentes de test.

**Gradle y el catálogo de versiones.** `gradle/libs.versions.toml` guarda en un único sitio las versiones y librerías. Para añadir una dependencia se hacen dos cosas: declararla en el catálogo y usarla en el `build.gradle.kts` del módulo (`libs.kotlinx.datetime`; los guiones del catálogo pasan a ser puntos).

**Regla de las dependencias.** Una librería que usa el código de `commonMain` se declara en `commonMain.dependencies`. Lo que se declara en `androidMain` solo lo ve el código de Android. Declararla en el sitio equivocado compila al sincronizar, pero falla al usarla desde el código común.

---

## Bloque 1 — Capa de dominio

**Qué es.** Kotlin puro, sin Android, Compose ni base de datos. Contiene las "cosas" del negocio y las reglas para operar con ellas. Es la capa más estable y más fácil de testear.

**Modelos creados:** `MovementType` (enum), `Category`, `Transaction` y `MonthlySummary` (todas `data class`, salvo el enum).

**Decisiones y su porqué**

- **El dinero se guarda como `Long` de céntimos, nunca como `Double`.** La coma flotante arrastra errores (`0.1 + 0.2` no da exactamente `0.3`) y con dinero eso no es aceptable. 12,50 € se guarda como 1250.
- **Los modelos son inmutables (`val`).** Para cambiar algo se crea una copia con `copy()`. Evita muchos fallos.
- **El balance es una propiedad calculada**, no un campo guardado. Un dato que se puede derivar de otros no se almacena: se calcula, así no puede quedar desincronizado.
- **Un enum, no un `String`, para el tipo de movimiento.** El compilador solo admite los valores válidos.

**Casos de uso.** Un caso de uso encapsula una acción del negocio. `CalculateMonthlySummaryUseCase` recibe una lista de transacciones y devuelve un resumen. Usa `operator fun invoke` para poder llamarlo como una función.

**Tests: el patrón AAA.** Arrange (preparar), Act (ejecutar), Assert (comprobar).

- El valor esperado debe ser un número fijo calculado a mano, **nunca** calculado con la misma lógica que se prueba; si no, el test se compara consigo mismo y no prueba nada.
- Un test sin ninguna comprobación (`assert`) siempre pasa y da falsa seguridad.
- En `commonTest` se usa `kotlin.test` (multiplataforma), no JUnit, que solo existe en la JVM.

---

## Bloque 2 — Presentación

**Formatear dinero.** `formatCents` convierte 10470 en `"104,70 €"`. Es una función pura de la capa de presentación: el dominio sigue trabajando en céntimos y solo se formatea al mostrar. Trampa con negativos: `/` y `%` conservan el signo, así que se calcula con el valor absoluto y el signo se pone aparte.

**Compose.** La interfaz se describe con funciones `@Composable`. Se combinan como piezas (`Column`, `Text`, `Button`) y se ajustan con `Modifier`.

**ViewModel y estado.**

- `SummaryUiState` es lo que la pantalla necesita para dibujar, ya formateado.
- `SummaryViewModel` guarda el estado y lo expone como `StateFlow` de solo lectura.
- `App()` lo observa con `collectAsStateWithLifecycle()` y Compose redibuja solo cuando el valor cambia.
- La pantalla solo dibuja; la lógica vive fuera de ella.

---

## Bloque 3 — Persistencia

**Repositorio.** `TransactionRepository` es una **interfaz** del dominio: dice qué operaciones se necesitan (`observeAll`, `add`) sin decir cómo se guardan. Hay dos implementaciones: `InMemoryTransactionRepository` y `SqlDelightTransactionRepository`. El ViewModel solo conoce la interfaz, así que se puede cambiar una por otra sin tocarlo (inyección de dependencias por constructor).

**Flow y suspend.** `Flow` es un flujo de valores a lo largo del tiempo. `suspend` marca una función que puede pausarse sin bloquear la interfaz; se llama desde una corrutina (`viewModelScope.launch`).

**Flujo reactivo de datos.** La pantalla no recibe órdenes de refrescarse: observa un `Flow` (patrón observador). Al guardar, el código generado por SQLDelight avisa (`notifyQueries`) de que la tabla cambió; `selectAll().asFlow()` vuelve a leer y emite; cada capa transforma el valor (`toDomain`, `buildState`) y `collectAsStateWithLifecycle()` lo convierte en estado de Compose, que redibuja. Camino completo: Pantalla → ViewModel → Repositorio → SQLite (ida) y, por `Flow`, de vuelta hasta la pantalla.

**SQLDelight.** Se escribe SQL en un archivo `.sq` y se genera código Kotlin tipado. La ruta del archivo debe coincidir con el `packageName` configurado. Genera `FinTrackDatabase`, la fila `TransactionEntity` y las consultas.

**Driver por plataforma: `expect`/`actual`.** El código común declara `expect class DatabaseDriverFactory`; Android y Desktop dan cada uno su `actual`. Android necesita un `Context`; Desktop abre un archivo en la carpeta del usuario y crea las tablas solo si el archivo es nuevo.

**Mapper.** SQLDelight habla en `TransactionEntity` (textos y números) y el dominio habla en `Transaction` (enum y `LocalDate`). `toDomain()` traduce de uno a otro; al guardar se hace al revés (`.name` y `.toString()`). Es una función de extensión suelta, no una clase.

**Conexión a la app.** `App()` recibe un `DatabaseDriverFactory` como parámetro: cada lanzador (`MainActivity` en Android, `main.kt` en Desktop) crea el suyo, y `App()` solo lo usa sin saber de dónde viene. El repositorio se construye dentro de la lambda de `viewModel { ... }`, que solo se ejecuta al crear el ViewModel y devuelve el valor de su **última línea**. Las líneas anteriores solo preparan cosas.

**Estado actual (22 oct):** Bloque A completado. La app persiste los datos en SQLite (verificado en Desktop: los gastos añadidos siguen ahí al cerrar y reabrir; la base está en `~/.fintrack/fintrack.db`).

---

## Criterios de diseño

**¿Clase o función suelta?** Una clase aporta algo cuando (1) guarda estado entre llamadas, (2) cumple un contrato o debe ser sustituible, o (3) recibe dependencias por constructor que usa en varias funciones. Si ninguna se cumple, una función suelta es suficiente.

**¿Dónde va cada cosa?** `domain` no depende de nadie. `data` y `presentation` dependen de `domain`, nunca al revés.

---

## Bitácora de errores y aprendizajes

| Qué pasó | Qué enseñó |
|---|---|
| `kotlinx-datetime` declarada en `androidMain` | Las dependencias del código común van en `commonMain` |
| Valor esperado de un test calculado con el mismo código que se probaba | El valor esperado se calcula a mano |
| Una suma mal hecha en el test (11130 en vez de 11030) | El test detectó el error de quien lo escribió |
| Tests sin `@Test` o con `assert` fuera de una función | Un test es una función anotada con `@Test` |
| `import org.junit.Test` en `commonTest` | El código común solo usa librerías multiplataforma |
| Funciones de extensión dentro de una `class` (ocurrió tres veces: `formatCents`, `toDomain`, `toItemUi`) | Las extensiones reutilizables van sueltas en el archivo. Truco visual: una función suelta empieza en el margen izquierdo, sin sangría |
| Firma mal escrita: `fun X.f: Tipo(){` | Estructura de una función: `fun` nombre `(` parámetros `)` `:` tipo `{`. Los paréntesis van siempre tras el nombre |
| Crear primero un test sin que existan las clases que usa | Flujo correcto: forma mínima, test en rojo, implementar hasta verde |
| Una función `suspend` llamada sin `launch` | Las funciones `suspend` se llaman desde una corrutina |
| `insert(...)` sin indicar de qué objeto es | Toda función pertenece a un objeto: `database.transactionEntityQueries.insert(...)` |
| Un test que fallaba por el nombre antiguo de otro test | El IDE guarda configuraciones de ejecución; se vuelve a lanzar desde la clase |
| Un repositorio de SQLDelight creado pero no pasado al ViewModel (se descartaba) | Crear un valor no basta: hay que guardarlo en una variable y usarlo. La app compilaba pero no persistía |
| `@Preview` en una función con parámetros | Las funciones de previsualización no pueden tener parámetros |
| No aparecía la opción de ejecutar Desktop | La configuración se crea la primera vez desde el ▶ de `main()` o con la tarea Gradle `:desktopApp:run` |

---

## Glosario

- **Source set:** carpeta de código para un destino (`commonMain`, `androidMain`, `jvmMain`).
- **`expect`/`actual`:** el código común declara lo que espera; cada plataforma lo implementa.
- **Composable:** función `@Composable` que dibuja una parte de la interfaz.
- **ViewModel:** objeto que guarda el estado de una pantalla y sobrevive a los redibujados.
- **StateFlow / Flow:** contenedores de valores que cambian con el tiempo; Compose se suscribe y redibuja.
- **Corrutina:** código que puede pausarse sin bloquear el hilo.
- **Repositorio:** capa que oculta de dónde vienen los datos.
- **Caso de uso:** una acción del negocio encapsulada en una clase pequeña.
