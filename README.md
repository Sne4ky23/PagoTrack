# PagoTrack - Control de Finanzas Personales

Aplicación Android nativa para automatizar el control de gastos personales mediante la interceptación de notificaciones bancarias.

## Características

✅ **Interceptación de Notificaciones**: Escucha notificaciones de apps bancarias y de pago en tiempo real
✅ **Análisis Inteligente**: Motor Regex robusto para detectar importe, moneda y comercio
✅ **Base de Datos Local**: Persistencia con Room Database
✅ **Confirmación Rápida**: Notificaciones con botones de confirmación/descarte
✅ **Interfaz Moderna**: UI construida con Jetpack Compose y Material 3
✅ **Navegación Fluida**: BottomBar con pantalla de Inicio y Ajustes

## Stack Tecnológico

- **Lenguaje**: Kotlin 1.9.22
- **Android Gradle Plugin**: 8.2.2
- **SDK**: compileSdk = 34, targetSdk = 34, minSdk = 26
- **UI Framework**: Jetpack Compose (BOM 2024.02.00)
- **Base de Datos**: Room 2.6.1
- **Arquitectura**: MVVM + Repository + Coroutines Flow

## Estructura del Proyecto

```
app/src/main/
├── java/com/pagotrack/app/
│   ├── MainActivity.kt
│   ├── data/
│   │   ├── Expense.kt (@Entity)
│   │   ├── ExpenseDao.kt (@Dao)
│   │   ├── AppDatabase.kt (Singleton)
│   │   └── ExpenseRepository.kt
│   ├── parser/
│   │   └── PaymentParser.kt (Motor Regex)
│   ├── service/
│   │   ├── PaymentNotificationListener.kt (NotificationListenerService)
│   │   └── NotificationActionReceiver.kt (BroadcastReceiver)
│   └── ui/
│       ├── ExpenseViewModel.kt
│       ├── Navigation.kt
│       └── screens/
│           ├── HomeScreen.kt
│           └── SettingsScreen.kt
├── AndroidManifest.xml
└── res/
    └── values/
        ├── strings.xml
        └── themes.xml
```

## Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/Sne4ky23/PagoTrack.git
   cd PagoTrack
   ```

2. **Compilar la app**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Instalar en dispositivo/emulador**
   ```bash
   ./gradlew installDebug
   ```

## Uso

1. **Otorgar permisos**:
   - Acceder a Ajustes > Apps > PagoTrack > Notificaciones > Permitir
   - Acceder a Ajustes > Notificaciones > Aplicaciones avanzadas > Acceso a notificaciones > PagoTrack

2. **Agregar apps bancarias**:
   - La app detecta automáticamente notificaciones de Santander, BBVA, CaixaBank, Revolut, N26, Bizum, Google Wallet y más

3. **Confirmar gastos**:
   - Recibe notificaciones con dos botones: Guardar (✅) y Descartar (❌)
   - Los gastos confirmados se registran en la base de datos

4. **Ver resumen**:
   - Pantalla Inicio muestra el total gastado hoy
   - Lista de transacciones con estado (Guardado/Pendiente)
   - Pantalla Ajustes para limpiar la base de datos

## Permisos Necesarios

- `BIND_NOTIFICATION_LISTENER_SERVICE`: Acceso a notificaciones del sistema
- `POST_NOTIFICATIONS`: Mostrar notificaciones propias (Android 13+)

## CI/CD

Workflow automático en GitHub Actions:
- ✅ Compila la app en Ubuntu con Java 17
- ✅ Ejecuta pruebas unitarias
- ✅ Genera APK en modo debug
- ✅ Sube artefactos automáticamente

## Roadmap

- [ ] Exportar reportes (CSV, PDF)
- [ ] Categorización automática de gastos
- [ ] Gráficos y estadísticas
- [ ] Sincronización en la nube
- [ ] Notificaciones personalizadas

## Licencia

Este proyecto es de código abierto bajo licencia MIT.

## Contacto

📧 [Sne4ky23@github.com]
🐙 GitHub: https://github.com/Sne4ky23/PagoTrack
