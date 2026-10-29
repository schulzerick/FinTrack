# Test 1 — Fundamentos (bloques 0 a 3)

**Fecha:** 22 de octubre de 2026
**Estado:** cerrado. Las cinco preguntas están corregidas y la pregunta 5 se reforzó con un reintento y una pregunta de seguimiento.

**Instrucciones:** respuestas con las propias palabras del alumno, sin mirar el código. Las respuestas se guardan **literalmente**; las correcciones las añade el profesor debajo. Escala: ✅ correcta · 🟡 correcta pero incompleta o imprecisa · ❌ incorrecta o sin respuesta.

---

## Pregunta 1
¿Por qué guardamos el dinero como `Long` de céntimos y no como `Double`?

**Respuesta del alumno:**
> La coma flotante da problemas.

**Valoración:** 🟡

**Corrección:** La idea es correcta, pero falta explicar *qué* problema es. Un `Double` guarda los números en binario, y muchos decimales que nos parecen exactos (como 0,1) **no se pueden representar exactamente** en binario. Al operar, esos errores minúsculos aparecen: `0.1 + 0.2` da `0.30000000000000004`. En dinero eso rompe las comparaciones (`==`) y los totales pueden descuadrar por céntimos. Un `Long` de céntimos guarda enteros, que son exactos: 12,50 € es 1250, y sumar o restar enteros no pierde nada. El formato con decimales se aplica solo al mostrar (`formatCents`).

---

## Pregunta 2
Pusimos `kotlinx-datetime` en `commonMain.dependencies` y no en `androidMain`. ¿Por qué importa dónde se declara una dependencia?

**Respuesta del alumno:**
> Porque es una dependencia usada para todo el proyecto y no solamente la seccion de android

**Valoración:** ✅ (correcta en lo esencial, con matices)

**Corrección:** La idea es la correcta: la usa código que no es solo de Android. Dos precisiones. Primero, no es "todo el proyecto" sino el código de `commonMain` del módulo `shared` (el modelo `Transaction` usa `LocalDate`). Segundo, y es lo que explica *por qué importa*: las dependencias se **heredan hacia abajo, no hacia arriba**. Lo declarado en `commonMain` lo ven también `androidMain` y `jvmMain`, pero lo declarado en `androidMain` solo lo ve Android. Si hubiera quedado en `androidMain`, el código común no podría importar `LocalDate` y fallaría al compilar, aunque la sincronización de Gradle no se quejara.

---

## Pregunta 3
¿Qué significan `expect` y `actual`, y en qué pieza de tu proyecto los usas?

**Respuesta del alumno:**
> Expect es usado por CommonMain ya que recibe las implementaciones de manera pasiva y Actual es usado en los modulos como la implementacion.

**Valoración:** 🟡

**Corrección:** Vas bien encaminado, y falta la segunda mitad de la pregunta (en qué pieza). Aclaraciones:

- `expect` es una **declaración sin implementación** en `commonMain`: el código común dice "necesito algo con esta forma", sin saber cómo se hace. "Recibir pasivamente" no es lo que hace; *declara lo que espera*.
- `actual` es la implementación real, y vive en el **source set** de cada plataforma (`androidMain`, `jvmMain`). Cuidado con el término: no son "módulos". Los módulos son `shared`, `androidApp` y `desktopApp`; `androidMain` y `jvmMain` son carpetas (source sets) *dentro* del módulo `shared`.
- El compilador comprueba que cada plataforma proporciona un `actual` con la misma firma que el `expect`.
- **Pieza del proyecto:** `DatabaseDriverFactory` (el `expect` en `commonMain`; un `actual` en `androidMain` con `AndroidSqliteDriver` y otro en `jvmMain` con `JdbcSqliteDriver`). La plantilla traía además `getPlatform()`.

---

## Pregunta 4
El `SummaryViewModel` recibe un `TransactionRepository` (la interfaz) y no la clase concreta. ¿Qué ventaja te da eso?

**Respuesta del alumno:**
> Poder recibir cualquier clase que implemente la interfaz.

**Valoración:** ✅

**Corrección:** Correcto. Para completarlo, la consecuencia práctica: el ViewModel no cambia al sustituir `InMemoryTransactionRepository` por `SqlDelightTransactionRepository`; en los tests se le puede dar una versión falsa; y el ViewModel (capa de presentación) no depende de la capa de datos, solo del contrato del dominio, que es la regla de la arquitectura por capas.

---

## Pregunta 5
Cuando pulsas "Añadir gasto" y la pantalla actualiza el resumen, describe el camino que siguen los datos, de la pantalla a la base de datos y de vuelta.

**Respuesta del alumno:**
> No se decirte

**Valoración:** ❌ (no lo sé)

**Corrección:** Es el tema más importante de reforzar, porque junta casi todo lo estudiado. El camino completo:

**Ida (guardar)**
1. El `Button` de `App()` llama a `viewModel.addTestExpense()`.
2. El ViewModel abre una corrutina con `viewModelScope.launch` (porque `add` es `suspend`) y llama a `repository.add(transaction)`.
3. El repositorio traduce el `Transaction` a textos y números (`.name`, `.toString()`) y llama a `insert(...)` dentro de `withContext(Dispatchers.Default)`.
4. SQLite escribe la fila y SQLDelight avisa de que la tabla `TransactionEntity` ha cambiado (`notifyQueries`).

**Vuelta (reactiva; nadie pide "refrescar")**
5. `selectAll().asFlow()` estaba observando esa tabla, así que vuelve a ejecutar la consulta y emite las filas nuevas.
6. `mapToList` las convierte en `List<TransactionEntity>` y `.map { it.toDomain() }` en `List<Transaction>`.
7. En el ViewModel, `.map { buildState(it) }` calcula el resumen (`CalculateMonthlySummaryUseCase`) y lo formatea (`formatCents`) en un `SummaryUiState`; `stateIn` lo mantiene como `StateFlow`.
8. `collectAsStateWithLifecycle()` en `App()` detecta el cambio de estado y Compose redibuja los `Text`.

La idea clave: los datos **fluyen** hacia la pantalla; ninguna capa le ordena a otra que se actualice.

Nota: mientras la app use `InMemoryTransactionRepository` (hasta conectar la base de datos), los pasos 3 a 5 son equivalentes con `MutableStateFlow.update` en lugar de SQLite.

### Reintento de la pregunta 5 (tras la explicación)

**Respuesta del alumno:**
> Pantalla -> ViewModel -> Repository -> SqlDelight y vuelve

**Valoración:** 🟡

**Corrección:** El orden de las capas es correcto (Pantalla → ViewModel → Repositorio → base de datos). Dos matices. Primero, SQLDelight es la librería que genera el código; la base de datos es SQLite. Segundo, la respuesta solo nombra las piezas: no dice qué hace cada una, y "vuelve" no explica el mecanismo, que es lo esencial. La pantalla no recibe ninguna orden de refrescarse; se entera porque observa un `Flow`. Queda pendiente una pregunta de seguimiento sobre ese mecanismo.

---

### Pregunta de seguimiento (pregunta 5)
Nadie le dice a la pantalla "refréscate". ¿Qué mecanismo hace que se entere de que hay datos nuevos? ¿Qué papel juega la base de datos en ese aviso?

**Respuesta del alumno:**
> supongo que funcionara mediante algun listener y la base de datos al guardar dara la señal que hace que la pantalla se actualize

**Valoración:** ✅ (concepto correcto; falta precisar el vocabulario)

**Corrección:** Correcto en lo esencial, y llegaste a la idea razonando por analogía. El patrón que describes se llama **observador**: alguien se suscribe y recibe un aviso cuando algo cambia. En Kotlin ese "listener" no se escribe como un callback, sino que se expresa con un **`Flow`**: el repositorio devuelve un `Flow` y quien lo observa recibe cada valor nuevo. Precisión sobre quién da la señal: no la emite SQLite, sino el código que genera SQLDelight. Tras cada `insert` llama a `notifyQueries`, y las consultas que observan esa tabla (`selectAll().asFlow()`) vuelven a ejecutarse. Después cada capa transforma el valor y lo pasa hacia arriba, y en la pantalla `collectAsStateWithLifecycle()` es la suscripción que convierte cada emisión en un cambio de estado de Compose, que provoca el redibujado.

---

## Resultado

| Pregunta | Valoración |
|---|---|
| 1 | 🟡 |
| 2 | ✅ |
| 3 | 🟡 |
| 4 | ✅ |
| 5 | ❌ al principio; 🟡 en el reintento; ✅ en el seguimiento (concepto) |

**Temas a reforzar:**
1. **Explicar el porqué, no solo la idea.** Las respuestas 1, 2 y 4 apuntan bien pero se quedan en una frase. En una defensa hay que decir también *qué ocurre* y *qué consecuencia tiene*.
2. **Terminología:** *source set* (carpeta dentro de un módulo) frente a *módulo* (`shared`, `androidApp`, `desktopApp`).
3. **El flujo reactivo de datos** de la pantalla a la base de datos y de vuelta.
