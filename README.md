# Gestión y Localización de Máquinas (Android)

Aplicación Android desarrollada en **Android Studio** con **Kotlin**, orientada a la gestión y visualización de máquinas mediante un listado detallado y un mapa interactivo con geolocalización.

El proyecto fue realizado con fines **académicos** y se encuentra **incompleto**, ya que su desarrollo se detuvo antes de finalizar todas las funcionalidades previstas.

---

## 📱 Descripción general

La aplicación permite consultar información detallada de distintas máquinas registradas y visualizar su ubicación geográfica, ofreciendo dos modos de visualización complementarios:

- Vista en **listado**
- Vista en **mapa**

---

## 📋 Funcionalidades

### 🔹 Listado de máquinas
Pantalla que muestra todas las máquinas registradas junto con su información principal:

- Imagen del tipo de máquina
- Nombre
- Ubicación
- Estado (aparcada o no)
- Tiempo de uso diario
- Nivel de batería restante
- Estado de las llaves (puestas o no)

Incluye un **buscador** que permite filtrar las máquinas por cualquiera de sus características.

---

### 🗺️ Mapa de máquinas
Pantalla de mapa interactivo que permite:

- Visualizar la **ubicación actual del usuario**
- Mostrar las máquinas en su posición mediante iconos
- Consultar la información completa de una máquina al pulsar sobre ella

Funciones adicionales:
- Buscador de máquinas por características
- Botón para centrar el mapa en la ubicación del usuario
- Botón para alternar entre **vista mapa** y **vista satélite**

---

## 📍 Permisos y privacidad

Al iniciar la aplicación, se solicita al usuario permiso para acceder a su **ubicación**, necesario para:

- Mostrar la posición actual en el mapa
- Facilitar la localización de las máquinas

La aplicación no tiene fines comerciales ni gestiona datos en producción.

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin  
- **IDE:** Android Studio  

### Librerías y dependencias principales

```kotlin
// Mapas
api("org.osmdroid:osmdroid-android:6.1.20")

// Android Core
implementation("androidx.core:core-ktx:1.10.1")

// Localización
implementation("com.google.android.gms:play-services-location:21.0.1")
implementation("org.osmdroid:osmdroid-wms:6.1.10")
implementation("org.osmdroid:osmdroid-mapsforge:6.1.10")

// Utilidades
implementation("org.apache.commons:commons-math3:3.6.1")
