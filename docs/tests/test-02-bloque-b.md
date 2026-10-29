# Test 2 — Bloque B: lista de movimientos (en curso)

**Fecha de inicio:** 29 de octubre de 2026
**Estado:** abierto. Se irán añadiendo preguntas durante el bloque y se cierra al terminar el Bloque B.

Escala: ✅ correcta · 🟡 correcta pero incompleta o imprecisa · ❌ incorrecta o sin respuesta. Las respuestas del alumno se guardan literalmente.

---

## Pregunta 1
En `TransactionViewModel` hay dos `map` anidados. ¿Cuál es la diferencia entre el `map` de fuera y el `map` de dentro? (qué recibe cada uno y qué devuelve)

**Respuesta del alumno:**
> map de fuera: recibe una lista de transacciones, y devuelve un TransactionUiState
> map de dentro: convierte la lista de transacciones a los items de ui

**Valoración:** 🟡 (el de fuera ✅; el de dentro, correcto en el resultado pero impreciso)

**Corrección:**

- **`map` de fuera: correcto.** Es `Flow.map`. Se ejecuta **cada vez que el repositorio emite** una lista nueva (cada vez que cambia la tabla), recibe esa `List<Transaction>` y devuelve un `TransactionUiState`.
- **`map` de dentro: acertaste el efecto, pero no lo que recibe.** Es `List.map`. Su lambda **no recibe la lista**: recibe **cada transacción por separado** (el `it`) y devuelve el `TransactionItemUi` correspondiente. El `map` aplica esa conversión a todos los elementos y el resultado, una `List<TransactionItemUi>`, es lo que se mete en `items`.

La diferencia clave que no mencionaste: el `map` de fuera actúa **a lo largo del tiempo** (una vez por cada emisión del flujo) y el de dentro actúa **a lo ancho de una lista** (una vez por cada elemento). Los dos se llaman `map` porque hacen lo mismo con contenedores distintos: transformar el contenido sin cambiar la forma del contenedor.

---

## Pregunta 2
`toItemUi()` es una función suelta, fuera de cualquier clase. Te ha pasado tres veces colocar una función de este tipo dentro de una clase. ¿Qué criterio usas para decidir si algo debe ser una clase o una función suelta?

**Respuesta del alumno:**
> Que la funcion este dentro de una declaración de clase

**Valoración:** ❌ (no responde al criterio; ver reintento)

**Corrección:** Lo que describes es una *consecuencia*, no un criterio: una función declarada dentro de una clase pasa a ser un método de esa clase. La pregunta era **cuándo conviene** que lo sea. El criterio: una clase aporta algo cuando (1) **guarda estado** entre llamadas, (2) **cumple un contrato** (una interfaz) o debe poder sustituirse por otra, o (3) **recibe dependencias por constructor** que usa en varias funciones. Si no se cumple ninguna, es una función suelta. `toItemUi()` y `formatCents` no guardan nada, no cumplen ningún contrato ni reciben dependencias, así que son funciones sueltas. `TransactionViewModel` guarda estado y recibe el repositorio, así que es una clase.

**Reintento (caso práctico):** decide, con el criterio anterior, si cada una debe ser clase o función suelta y por qué:
- (a) `isWeekend(date: LocalDate): Boolean`, que dice si una fecha cae en fin de semana.
- (b) Un `RecentTransactionsCache` que recuerda las últimas 10 transacciones consultadas.

**Respuesta del alumno al reintento:**
> a) funcion independiente ya que no guarda estados, no usa una interfaz ni recibe dependencias
> b) clase ya que guarda  estados

**Valoración del reintento:** ✅

**Corrección del reintento:** Las dos son correctas y, sobre todo, **bien razonadas**: aplicas los tres criterios con su causa. En (a), `isWeekend` es una función pura: recibe una fecha y devuelve un resultado, sin memoria, sin contrato y sin dependencias, así que una clase solo añadiría ruido. En (b), el caché tiene que recordar las últimas 10 transacciones entre una llamada y la siguiente, y eso es **estado**, que necesita vivir en un objeto. Un matiz útil para la defensa: basta **un solo** criterio afirmativo para justificar una clase; no hace falta que se cumplan los tres.

---

## Pregunta 3
¿Por qué creamos un `TransactionViewModel` nuevo en lugar de añadir la lista al `SummaryViewModel`?

**Respuesta del alumno:**
> Porque tienen necesidad de ser independientes, sea por funcionalidad o por la informacion que reciben y exponen

**Valoración:** ✅

**Corrección:** Correcta en lo esencial: es el principio de **responsabilidad única**, cada clase se ocupa de una sola cosa. Para completarla con las consecuencias concretas: cada pantalla tiene su propio estado (`SummaryUiState` y `TransactionUiState`) que cambia por motivos distintos; un cambio en la lista no obliga a tocar ni a volver a probar el resumen; cada ViewModel es pequeño y fácil de leer; y si se junta todo en uno, acaba creciendo hasta ser una "clase Dios" difícil de mantener. En una defensa, conviene acompañar la idea con ese ejemplo.

---

## Resultado parcial

| Pregunta | Valoración |
|---|---|
| 1 | 🟡 |
| 2 | ❌ al principio; ✅ en el reintento con caso práctico |
| 3 | ✅ |
