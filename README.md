# Calculadora Matemática (Java + HTML) — Control de Versiones

Aplicación web desarrollada en **Java** (`Server.java`) y **HTML** (`index.html`) que resuelve exclusivamente operaciones matemáticas de **suma**, **resta**, **multiplicación** y **potencia** utilizando únicamente números enteros de hasta 5 dígitos dentro del rango `[-99999, 99999]`.

---

## Estructura de Ramas del Repositorio

El flujo de trabajo con Git se divide en tres ramas con responsabilidades estrictas:
- **`main`**: Rama principal. Contiene exclusivamente actualizaciones de documentación (`README.md`) y los `merge` provenientes de las ramas de desarrollo.
- **`frontend`**: Contiene exclusivamente commits relacionados con archivos `.html` (`index.html`).
- **`backend`**: Contiene exclusivamente commits relacionados con archivos `.java` (`Server.java`).

---

## Guía detallada: Comandos de Git y cómo publicar un repositorio en GitHub

### 1. Explicación de los comandos principales de Git
- **`git init`**: Inicializa un nuevo repositorio local de Git creando la carpeta oculta `.git` donde se almacena todo el historial de versiones.
- **`git clone <url>`**: Descarga una copia completa de un repositorio remoto desde GitHub a nuestro equipo local, configurando automáticamente el remoto llamado `origin` y descargando sus ramas.
- **`git checkout -b <nombre-rama>`**: Crea una nueva rama a partir de la rama actual y cambia inmediatamente el espacio de trabajo a esa nueva rama.
- **`git checkout <nombre-rama>`**: Cambia el espacio de trabajo a una rama ya existente.
- **`git add <archivo>`**: Añade los cambios de un archivo específico al área de preparación (*staging area*) para incluirlos en el siguiente commit.
- **`git commit -m "<mensaje>"`**: Guarda de forma permanente en el historial local una instantánea de los archivos preparados en el *staging area*, acompañada de un mensaje descriptivo.
- **`git remote add origin <url>`**: Vincula un repositorio local creado desde cero con un repositorio remoto vacío alojado en GitHub bajo el alias `origin`.
- **`git push -u origin <nombre-rama>`**: Sube los commits locales de la rama indicada al repositorio remoto (`origin`). La opción `-u` (*upstream*) deja vinculada la rama local con la remota para futuros `git push`.
- **`git merge <nombre-rama>`**: Fusiona el historial y los cambios de la rama indicada dentro de la rama en la que nos encontramos situados actualmente.

---

### 2. Cómo publicar un repositorio en GitHub

#### Opción A: Empezando desde GitHub con un `README.md` existente (Método recomendado)
1. **Crear el repositorio en GitHub**:
   - Iniciar sesión en GitHub y pulsar en **New repository**.
   - Escribir el nombre del repositorio (`calculadora-edd`), seleccionar visibilidad **Public** y marcar la casilla **Add a README file** para inicializar la rama `main` con un commit inicial.
   - Pulsar **Create repository** y copiar la URL HTTPS (`https://github.com/MIQU3LCRR/calculadora-edd.git`).
2. **Clonar en el equipo local**:
   - Abrir la terminal en la carpeta deseada y ejecutar:
```bash
git clone [https://github.com/MIQU3LCRR/calculadora-edd.git](https://github.com/MIQU3LCRR/calculadora-edd.git)
cd calculadora-edd