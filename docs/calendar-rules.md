# Calendar Rules

Kalendr uses deterministic arithmetic so the same Gregorian date produces the
same result on every device.

- Hijri: civil/tabular Islamic calendar, epoch 1 Muharram 1 AH = 622-07-16
  (Julian), with alternating 30/29-day months and leap years in the standard
  30-year cycle. This is not moon-sighting or Umm al-Qura.
- Javanese: lunar calendar anchored historically at 1 Sura 1555 = 1633-07-08
  Gregorian. The current Asapon cycle is anchored at 1 Sura 1960 = 2026-06-17
  Gregorian. Month lengths alternate 30/29 days; Besar is 30 in the windu
  leap years Ehe, Dal, and Jimakir (years 2, 5, and 8). The five-day pasaran is
  anchored to 1 Sura 1960 = Kliwon.
- The displayed Javanese year is the local year count starting at 1555.
- Indonesian holidays are fetched per rendered month from the Tanggal Merah
  API. The API's `holiday` type is represented as a national holiday and its
  `leave` type as joint leave. If published Indonesian data is unavailable,
  Nager.Date is used for predictable national holidays in the requested year.
  The 2026 data follows SKB Menteri Agama,
  Menteri Ketenagakerjaan, dan Menteri PANRB No. 1497/2025, No. 2/2025,
  No. 5/2025. Joint leave is represented separately from national holidays;
  it is not automatically a universal day off for every employer.

Validated vector: 2026-08-25 = 11 Mulud 1960, Selasa Wage. Published calendars
can differ by a day around kurup or local convention; this app follows the
current Asapon/Kemenag-aligned anchor above.

Validation references:

- https://ki-demang.com/almanak_jawa/konversi/masehi/2026/agustus/25
- https://nusamasa.com/kalender/masehi/2026
- https://mcp.anu.edu.au/proudfoot/calendars.html
- https://www.kemenkopmk.go.id/index.php/pemerintah-tetapkan-17-hari-libur-nasional-dan-8-hari-cuti-bersama-tahun-2026
- https://jdih.kemenkoinfra.go.id/cfind/source/files/skb-libur-nasional-dan-cuti-bersama-tahun-2026.pdf
