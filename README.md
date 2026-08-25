# Kalendr

[Dokumentasi Bahasa Inggris](README.en.md)

Kalendr adalah aplikasi kalender Android yang menampilkan kalender Gregorian,
Hijriah civil, dan kalender Jawa dalam satu tampilan bulanan yang ringkas.

## Fitur

- Menampilkan grid kalender Gregorian selama 42 sel, termasuk hari dari bulan
  sebelum dan sesudahnya.
- Menampilkan tanggal Hijriah pada setiap hari.
- Menampilkan tanggal Jawa dan pasaran lima hari: Legi, Pahing, Pon, Wage,
  dan Kliwon.
- Menandai hari ini secara visual.
- Tombol **Hari ini** untuk kembali ke bulan berjalan.
- Tombol **Pilih tanggal** dengan Material Date Picker untuk memilih tanggal,
  bulan, atau tahun secara langsung, termasuk tanggal historis seperti
  Februari 1900.
- Tampilan Material 3 yang compact dan mendukung tema terang/gelap perangkat.

## Teknologi

- Kotlin 2.0.21
- Android Gradle Plugin 8.7.3
- compileSdk 35
- minSdk 26
- AndroidX AppCompat 1.7.0
- Material Components for Android 1.12.0
- Java/Kotlin time API (`LocalDate`, `YearMonth`, dan `Instant`)
- JUnit 4 untuk unit test

## Menjalankan proyek

### Prasyarat

- JDK 17 atau lebih baru.
- Android SDK dengan platform Android 35.
- Gradle yang tersedia di PATH.

Proyek ini belum menyertakan Gradle Wrapper. Dari direktori root proyek,
jalankan:

```bash
gradle assembleDebug
```

APK debug akan tersedia di:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Validasi

Menjalankan unit test:

```bash
gradle test
```

Menjalankan lint:

```bash
gradle lint
```

Menjalankan seluruh pemeriksaan utama dan membuat APK debug:

```bash
gradle test lint assembleDebug
```

## Aturan kalender

Aturan konversi dijelaskan lebih lengkap di
[`docs/calendar-rules.md`](docs/calendar-rules.md).

- Kalender Hijriah menggunakan sistem civil/tabular dengan siklus kabisat 30
  tahun. Sistem ini bukan perhitungan rukyat dan bukan Umm al-Qura.
- Kalender Jawa menggunakan anchor Asapon saat ini dan siklus pasaran lima
  hari.
- Hasil konversi bersifat deterministik: tanggal Gregorian yang sama
  menghasilkan tanggal Hijriah dan Jawa yang sama di setiap perangkat.
- Salah satu vektor validasi utama adalah `2026-08-25 = 11 Mulud 1960,
  Selasa Wage`.

Kalender yang diterbitkan oleh sumber lain dapat berbeda satu hari di sekitar
kurup atau karena perbedaan konvensi lokal.

## Struktur proyek

```text
app/src/main/kotlin/com/kalendr/app/
├── MainActivity.kt                 # UI dan navigasi pemilihan tanggal
└── calendar/
    ├── DateSelection.kt            # Konversi tanggal picker berbasis UTC
    ├── HijriConverter.kt           # Konversi kalender Hijriah
    ├── JavaneseConverter.kt        # Konversi kalender Jawa dan pasaran
    ├── MonthCalendar.kt            # Pembentukan grid 42 sel
    └── CalendarFormatter.kt        # Format ringkasan kalender

app/src/test/                       # Unit test aturan dan grid kalender
```

## Lisensi

Belum ada lisensi proyek yang ditetapkan.
