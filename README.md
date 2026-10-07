# Hod kostkou: XML a Jetpack Compose

Repozitář obsahuje dvě varianty jednoduché aplikace pro hod kostkou. Obě po stisknutí tlačítka krátce zobrazují průběh hodu, potom ukážou výsledek a zvýší počet hodů.

## Varianta XML

Rozhraní je definováno v souboru XML. V Kotlinu se jednotlivé prvky najdou pomocí `findViewById`. Během hodu aplikace přímo mění text `TextView` s kostkou; po dokončení zobrazí výsledek a počet hodů.

## Varianta Jetpack Compose

Rozhraní je sestaveno pomocí composable funkcí. Hodnota kostky a stav hodu jsou uložené jako Compose state. Když se `diceValue` změní, Compose automaticky znovu vykreslí části rozhraní, které tuto hodnotu používají.

## Krátké porovnání aktualizace kostky

- **XML:** Kotlin při každé změně nastaví nový text přímo na `tvDice` (`TextView`).
- **Jetpack Compose:** Kotlin změní stav `diceValue`; Compose změnu zaznamená a automaticky překreslí příslušný `Text`.

V obou variantách se během hodu desetkrát náhodně změní zobrazená kostka s prodlevou 250 ms a poté se zobrazí konečný výsledek.
