# Test 1b — Repaso de `expect` / `actual` (cierre del Bloque A)

**Fecha:** 22 de octubre de 2026
**Estado:** corregido

Repaso breve tras conectar la base de datos, para reforzar el punto flojo del Test 1 (pregunta 3). Escala: ✅ correcta · 🟡 correcta pero incompleta o imprecisa · ❌ incorrecta o sin respuesta. Las respuestas del alumno se guardan literalmente.

---

## Pregunta 1
Si borraras el archivo `DatabaseDriverFactory.jvm.kt`, ¿qué pasaría con `androidApp` y qué pasaría con `desktopApp`? ¿Por qué?

**Respuesta del alumno:**
> No deberia afectar el androidApp, pero si afectaria el funcionamiento del desktop ademas de errores de codigo.

**Valoración:** 🟡

**Corrección:** La conclusión es correcta: Android seguiría compilando y Desktop no. Falta el porqué, que es lo que se pregunta. El compilador exige que **cada destino** del módulo `shared` tenga su `actual` para cada `expect`. Sin el archivo de `jvmMain`, el destino JVM no tiene `actual` y falla con un error del tipo "Expected class has no actual declaration". Como `desktopApp` usa el destino JVM de `shared`, no se puede ni compilar. `androidApp` usa el destino Android, que conserva su propio `actual`, y no se ve afectado.

Un matiz importante de vocabulario: no es un fallo "de funcionamiento" sino un **error de compilación**. La app de Desktop no llegaría a ejecutarse. Esa es justo la ventaja de `expect`/`actual`: el olvido se detecta al compilar y no cuando el usuario ya está usando la app.

---

## Pregunta 2
¿Por qué `MainActivity` crea `DatabaseDriverFactory(applicationContext)` y `main.kt` crea `DatabaseDriverFactory()` sin nada?

**Respuesta del alumno:**
> Porque la aplicacion de android necesita el contexto para mantener la persistencia de la aplicacion

**Valoración:** 🟡

**Corrección:** La idea es buena, con una imprecisión. El `Context` no "mantiene la persistencia": es el objeto de Android que da acceso al entorno de la aplicación, y en concreto le dice a Android **dónde está el almacenamiento privado de esa app**, para saber dónde crear el archivo de la base de datos. En Desktop no existe el concepto de `Context`; ahí el propio código decide la ruta (`~/.fintrack/fintrack.db`).

Por qué los constructores pueden ser distintos: el `expect` solo declara `createDriver()`, no el constructor. Cada `actual` puede pedir lo que necesite para construirse. Y como el código común (`App()`) no puede crear la fábrica (no sabe qué constructor tiene cada plataforma), cada lanzador la crea y se la pasa.

---

## Resultado

| Pregunta | Valoración |
|---|---|
| 1 | 🟡 |
| 2 | 🟡 |

**Para reforzar:** responder siempre con la causa y la consecuencia. Aquí lo importante son dos ideas: el compilador verifica que cada plataforma tiene su `actual` (error en compilación, no en ejecución), y cada plataforma resuelve a su manera lo que no se puede compartir (el `Context` en Android, una ruta de archivo en Desktop).
