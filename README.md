# AgenciaDeViajes - Segundo Desafío Práctico DSMCII 2026

Aplicación móvil Android desarrollada en **Kotlin + Jetpack Compose** para la gestión de un catálogo de destinos turísticos, con autenticación de usuarios y CRUD completo conectado a **Firebase**.

---

## Información del Alumno

- **Nombre:** Kevin Alexander Argueta Alas
- **Carné:** AA250104
- **Materia:** Desarrollo de Software para Móviles DSM441 G04L
---

##Descripción de la Aplicación

**AgenciaDeViajes** es una app Android que permite a los agentes de viajes:

- 🔐 Autenticarse con correo y contraseña mediante Firebase Auth
- 🗂️ Gestionar un catálogo de destinos turísticos (Crear, Leer, Actualizar, Eliminar)
- 🖼️ Asociar una imagen a cada destino desde la galería del dispositivo
- ✅ Validar campos obligatorios antes de guardar
- 🔥 Almacenar la información en Firebase Firestore
- 💾 Guardar las imágenes localmente en el almacenamiento interno de la app

---

## 🚀 Tecnologías Utilizadas

| Tecnología | Uso |
|------------|-----|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** | Interfaz de usuario declarativa |
| **Material Design 3** | Sistema de diseño y paleta de colores |
| **Firebase Auth** | Autenticación de usuarios |
| **Firebase Firestore** | Base de datos en la nube |
| **Coil** | Carga de imágenes locales |
| **Navigation Compose** | Navegación entre pantallas |
| **ViewModel / State** | Gestión de estado |
| **Coroutines** | Programación asíncrona |

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura por capas basada en **MVVM** (Model-View-ViewModel):
app/src/main/java/agencia/viajes/
│
├── MainActivity.kt # Punto de entrada
├── AppNavigation.kt # NavHost y rutas
│
├── data/ # Capa de datos
│ ├── Destino.kt # Modelo de datos
│ ├── AuthRepository.kt # Firebase Auth
│ ├── DestinoRepository.kt # Firestore CRUD
│ └── LocalStorageRepository.kt # Imágenes locales (filesDir)
│
├── ui/ # Capa de presentación
│ ├── auth/AuthScreen.kt # Login / Registro
│ ├── catalog/
│ │ ├── CatalogScreen.kt # Lista de destinos
│ │ └── DestinoCard.kt # Card individual
│ ├── destination/
│ │ └── DestinationFormScreen.kt # Crear / Editar destino
│ └── theme/ # Paleta y tema
│ ├── Color.kt
│ ├── Theme.kt
│ └── Type.kt
│
└── viewmodel/ # Capa de lógica de presentación
├── AuthViewModel.kt
└── DestinoViewModel.kt


---

## ⚙️ Funcionalidades Principales

### 🔐 Autenticación (Firebase Auth)
- Registro de nuevos usuarios con email y contraseña
- Inicio de sesión de usuarios existentes
- Cierre de sesión
- Validaciones: campos no vacíos, contraseña mínima de 6 caracteres

### 📋 CRUD de Destinos

| Operación | Descripción |
|-----------|-------------|
| **Create** | Formulario para agregar un destino con nombre, país, precio, descripción e imagen |
| **Read** | Catálogo con `LazyColumn` + `CardView` mostrando todos los destinos |
| **Update** | Edición de cualquier campo del destino, incluyendo la imagen |
| **Delete** | Eliminación con `AlertDialog` de confirmación previa |

### ✅ Validaciones Obligatorias Implementadas

- ❌ No se permiten campos nulos o vacíos
- 💰 El precio debe ser obligatoriamente mayor a 0
- 🖼️ Cada destino debe tener una imagen asociada para ser guardado
- 📝 La descripción debe tener mínimo 20 caracteres
- ⚠️ Los errores se muestran en pantalla con texto en rojo

### 🖼️ Gestión de Imágenes

Las imágenes se gestionan mediante **almacenamiento local interno** (`context.filesDir`):

1. El usuario selecciona una imagen desde la galería
2. La app copia el archivo al directorio privado `filesDir/destinos/`
3. Se guarda la ruta absoluta en Firestore junto con los demás datos
4. Coil carga la imagen desde la ruta local en el catálogo
5. Al eliminar un destino, también se elimina su imagen asociada

> **Nota:** Se optó por almacenamiento local en lugar de Firebase Storage porque este último ahora requiere activación del plan Blaze (con tarjeta de crédito), lo cual no es viable para un proyecto académico. La indicación del desafío permite explícitamente *"Storage Local u otras alternativas"*.

---

## 🎨 Paleta de Colores

La app utiliza una paleta basada en [Material Palette](https://www.materialpalette.com/):

| Color | Hex | Uso |
|-------|-----|-----|
| Primary | `#00695C` | Color principal (Teal) |
| Primary Dark | `#004D40` | Variante oscura |
| Secondary | `#FFA000` | Color de acento (Amber) |
| Background | `#F5F5F5` | Fondo de pantallas |
| Surface | `#FFFFFF` | Superficie de cards |
| Error | `#B00020` | Mensajes de error |


