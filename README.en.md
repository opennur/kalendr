# Kalendr

[Indonesian documentation](README.md)

Kalendr is an Android calendar application that displays the Gregorian,
civil Hijri, and Javanese calendars together in a compact monthly view.

## Features

- Displays a 42-cell Gregorian calendar grid, including days from adjacent
  months.
- Shows the Hijri date for each day.
- Shows the Javanese date and the five-day pasaran cycle: Legi, Pahing, Pon,
  Wage, and Kliwon.
- Visually highlights the current day.
- Provides a **Hari ini** button to return to the current month.
- Provides a **Pilih tanggal** Material Date Picker for direct date, month, or
  year selection, including historical dates such as February 1900.
- Uses a compact Material 3 interface and supports the device light/dark
  theme.

## Technology

- Kotlin 2.0.21
- Android Gradle Plugin 8.7.3
- compileSdk 35
- minSdk 26
- AndroidX AppCompat 1.7.0
- Material Components for Android 1.12.0
- Java/Kotlin time API (`LocalDate`, `YearMonth`, and `Instant`)
- JUnit 4 for unit tests

## Running the project

### Requirements

- JDK 17 or newer.
- Android SDK with the Android 35 platform.
- Gradle available on PATH.

This project does not currently include a Gradle Wrapper. From the project
root, run:

```bash
gradle assembleDebug
```

The debug APK will be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Validation

Run unit tests:

```bash
gradle test
```

Run lint:

```bash
gradle lint
```

Run the main checks and build the debug APK:

```bash
gradle test lint assembleDebug
```

## Calendar rules

The conversion rules are documented in detail in
[`docs/calendar-rules.md`](docs/calendar-rules.md).

- The Hijri calendar uses a civil/tabular system with a standard 30-year leap
  cycle. It is not moon-sighting-based and is not Umm al-Qura.
- The Javanese calendar uses the current Asapon anchor and a five-day pasaran
  cycle.
- Conversion results are deterministic: the same Gregorian date produces the
  same Hijri and Javanese dates on every device.
- One of the primary validation vectors is `2026-08-25 = 11 Mulud 1960,
  Selasa Wage`.

Published calendars may differ by one day around kurup or because of local
calendar conventions.

## Project structure

```text
app/src/main/kotlin/com/kalendr/app/
├── MainActivity.kt                 # UI and date-selection navigation
└── calendar/
    ├── DateSelection.kt            # UTC-based date-picker conversion
    ├── HijriConverter.kt           # Hijri calendar conversion
    ├── JavaneseConverter.kt        # Javanese calendar and pasaran conversion
    ├── MonthCalendar.kt            # 42-cell grid generation
    └── CalendarFormatter.kt        # Calendar summary formatting

app/src/test/                       # Calendar rules and grid unit tests
```

## License

No project license has been established yet.
