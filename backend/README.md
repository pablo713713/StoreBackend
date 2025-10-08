# Backend QA: Cobertura con JaCoCo y OpenClover

Este README explica cómo ejecutar las pruebas y generar reportes de cobertura con JaCoCo (estándar) y OpenClover (análisis avanzado) en este proyecto Spring Boot (Maven).

## Requisitos
- Java JDK 21 (o compatible) disponible en PATH o configurado como JAVA_HOME.
- Maven Wrapper incluido en el proyecto (`mvnw.cmd` en Windows).

## Ejecutar pruebas con JaCoCo

JaCoCo ya está configurado vía plugin en `pom.xml`.

1. Desde la carpeta `backend`, ejecutar pruebas:

```powershell
# Windows PowerShell
.\mvnw.cmd -q clean test
```

2. Abrir el reporte HTML de JaCoCo:

```powershell
Start-Process .\target\site\jacoco\index.html
```

Ubicación del reporte: `backend/target/site/jacoco/index.html`.

## Ejecutar cobertura con OpenClover (perfil)

OpenClover está integrado como perfil Maven `clover` para no afectar la build por defecto.

1. Ejecutar instrumentación, tests y generación de reporte:

```powershell
.\mvnw.cmd -q -Pclover clean verify
```

2. Abrir el reporte HTML de Clover:

```powershell
Start-Process .\target\site\clover\index.html
```

Ubicación del reporte: `backend/target/site/clover/index.html`.

## Notas y resolución de problemas
- Si usas versiones múltiples de Java, puedes forzar temporalmente JAVA_HOME en la sesión:

```powershell
$env:JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21'; $env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

- Si el reporte de Clover muestra advertencias de JDK, ignóralas si el HTML se genera correctamente. Podemos ajustar la configuración del plugin si fuese necesario.

- JaCoCo es la fuente de verdad para CI/Quality Gates; usa Clover para análisis puntual (per-test coverage y hotspots) y priorización de tests.

## Comandos útiles adicionales

- Ejecutar solo una clase de test JUnit:
```powershell
.\mvnw.cmd -q -Dtest=OnlineStoreApplicationTests test
```

- Regenerar solo el reporte de pruebas (Surefire report):
```powershell
.\mvnw.cmd -q surefire-report:report
Start-Process .\target\site\surefire-report.html
```

## Mantenimiento y objetivos
- Objetivo de cobertura (sugerido): ≥ 80% líneas con JaCoCo.
- Para asegurar calidad en cambios nuevos, exigir “New Code Coverage” ≥ 90% en PRs.
- Considera añadir Mutation Testing (PIT) para servicios críticos si buscas medir efectividad de tests más allá del % de líneas.
