# Trvalé ukládání objednávek

## Současný stav aplikace

`MainActivity` předává vybrané kolo, příplatkovou výbavu a cenu do `SummaryActivity` pomocí `Intent`. Tím se údaje pouze přenesou mezi obrazovkami; po zavření aplikace se z nich nevytvoří uložená objednávka a aplikace nemá historii objednávek.

## Možnosti ukládání

| Řešení | Kdy se hodí | Omezení |
| --- | --- | --- |
| **Room (SQLite)** | Strukturovaná data a historie objednávek uložená přímo v telefonu. Podporuje dotazy a změny schématu. | Samo o sobě nesynchronizuje objednávky mezi telefony ani se serverem. Data v úložišti aplikace se při odinstalaci odstraní. |
| **DataStore** | Malé nastavení, například zvolená měna nebo poslední výběr. | Není vhodné pro rostoucí seznam objednávek, jejich filtrování a vztahy; pro taková data Android doporučuje Room. |
| **Soubor JSON** | Jednoduchý export, import nebo prototyp. | Je nutné ručně řešit souběžné úpravy, vyhledávání, poškozený soubor a změny formátu. |
| **Cloud Firestore nebo vlastní server s databází** | Záloha mimo telefon, přístup z více zařízení nebo sdílení objednávek. | Je potřeba síť, bezpečné přihlášení a správná pravidla přístupu. Cloudová služba může mít limity nebo cenu podle zvoleného plánu. |

Android doporučuje pro strukturovaná lokální data používat Room místo přímého přístupu k SQLite. DataStore je určeno spíše pro malé datové sady a nastavení. [Room – lokální databáze](https://developer.android.com/training/data-storage/room) · [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

## Doporučení pro tuto aplikaci

Pro první verzi je nejjednodušší a přiměřené řešení **Room**. Objednávka by mohla obsahovat:

- jedinečné ID a datum vytvoření,
- název kola,
- příznaky vybrané výbavy,
- cenu uloženou jako celé číslo v Kč,
- případně stav objednávky.

Při potvrzení objednávky aplikace vytvoří neměnný záznam v databázi. Obrazovka se souhrnem pak může zobrazit uložený záznam a další obrazovka historii z Room znovu načte i po restartu aplikace. Pro tento záměr se hodí vrstvy **Entity → DAO → Room Database → Repository**. Cenu je vhodné ukládat jako číselnou hodnotu, nikoli jen jako formátovaný text typu „Celkem: 135000 Kč“.

Room ukládá data do zařízení. Pokud mají objednávky zůstat dostupné i po výměně či ztrátě telefonu, lze později přidat cloudovou synchronizaci nebo zálohování. Cloud Firestore podporuje lokální cache a synchronizaci změn po návratu připojení; mobilní aplikace ale musí mít správně nastavená autentizační a bezpečnostní pravidla. [Firestore – práce offline](https://firebase.google.com/docs/firestore/manage-data/enable-offline) · [Zabezpečení Firestore](https://firebase.google.com/docs/firestore/security/get-started)

Android Auto Backup může pomoci obnovit některá aplikační data, ale není náhradou databáze ani vlastní historie na serveru. Závisí na nastavení zařízení a zálohování. [Android Auto Backup](https://developer.android.com/identity/data/autobackup)

## Jak může pomoci AI

AI nástroj může navrhnout datový model Room, připravit kostru `Entity`, `DAO` a databáze, vysvětlit migrace nebo navrhnout testovací případy. Vygenerovaný kód je potřeba zkontrolovat, spustit a ověřit podle aktuální dokumentace Androidu. AI sama objednávky neukládá a neměla by dostávat skutečné osobní údaje zákazníků.

## Závěr

Začněte s **Room**, protože aplikace potřebuje ukládat strukturované objednávky a jejich historii v jednom zařízení. Pokud zadání později vyžaduje přístup z více zařízení nebo spolehlivou vzdálenou zálohu, doplňte cloudovou službu s přihlášením a zabezpečenými pravidly.
