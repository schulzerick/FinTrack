# Planificación del proyecto FinTrack

**Fecha límite de entrega:** jueves 31 de diciembre de 2026.
**Dedicación máxima:** 4 horas por semana. La semana del 19 de octubre no hay otras entregas, así que se puede dedicar más (estimado: unas 6 h).

> Las horas son **estimaciones** basadas en el ritmo real de las primeras sesiones (cada tarea pequeña ronda los 30-60 minutos). Se revisan al cierre de cada bloque.

## Bloques de trabajo

| Bloque | Contenido | Qué se aprende | Horas estimadas |
|---|---|---|---|
| A | Conectar la base de datos a la app y repasar `expect`/`actual` | Inyección de la plataforma, persistencia real | 3 |
| B | Compose de verdad: lista de movimientos y formulario de alta | Layouts, `LazyColumn`, estado de pantalla, formularios | 8 |
| D | Presupuestos y objetivos de ahorro (dominio + pantallas) | Casos de uso con tests, lógica de negocio propia | 10 |
| C | Navegación entre pantallas (Android y Desktop) | Navegación multiplataforma | 3 |
| F | Tema claro/oscuro, adaptación a Desktop y pulido | Material 3 theming, layouts adaptativos | 4 |
| E | Inyección de dependencias con Koin (**opcional**) | DI con un framework | 3 |
| M | Memoria, revisión de apuntes y preparación de la entrega | Documentar y defender el proyecto | 4 |

El orden pone **antes lo evaluable** (movimientos, presupuestos, objetivos de ahorro) y **después lo que pule** (navegación, tema, Koin). Si hay retraso, se recorta por el final.

## Calendario

| Semana | Foco | Entregable de la semana |
|---|---|---|
| 19-25 oct (esta) | Bloque A completo + test 1 + arranque de B | BD conectada y persistiendo en Desktop y Android |
| 26 oct | Bloque B: lista de movimientos | Pantalla de movimientos con datos reales |
| 2 nov | Bloque B: formulario de alta + **test 2** | Alta de movimientos desde la UI |
| 9 nov | Bloque D: presupuestos (dominio con tests) | Motor de presupuestos probado |
| 16 nov | Bloque D: objetivos de ahorro (dominio con tests) | Motor de objetivos probado |
| 23 nov | Bloque D: pantallas de presupuestos y objetivos + **test 3** | Las dos pantallas funcionando |
| 30 nov | Bloque C: navegación | App navegable entera |
| 7 dic | Bloque F: tema y Desktop | App pulida |
| 14 dic | Colchón de desarrollo / Bloque E (si hay margen) + **test final** | Código congelado |
| 21 dic | Bloque M: memoria y entrega | Documentación completa |
| 28-31 dic | Margen de seguridad | Entrega **antes** del 31 |

## Si hay retraso: qué se recorta, en este orden

1. Koin (Bloque E): se queda con el cableado manual.
2. Pulido del tema claro/oscuro (Bloque F).
3. Adaptación específica a Desktop (Bloque F).

**No se recorta:** movimientos, presupuestos y objetivos de ahorro. Son el núcleo del proyecto.

## Seguimiento

| Fecha | Horas usadas | Nota |
|---|---|---|
| 22 oct | — | Plan creado |
