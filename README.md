# BlackJack

## Dinamika igre

1. **Začetek igre**: Igralec prejme dve kart, ki sta vidni. Dealer prejme samo eno karto, ki je vidna.
2. **Odločitve igralca**:
    - **"Hit"**: Igralec vzame dodatno karto.
    - **"Stand"**: Igralec ostane pri trenutnem seštevku kart.
    - **"Split"**: Če ima igralec dve enaki karti, lahko razdeli roko na dve ločeni roki.
    - **"Double Down"**: Igralec podvoji stavo in vzame samo eno dodatno karto.
3. **Pravila dealer-ja**: Delaer mora vleči karte, dokler ne doseže ali preseže skupnega seštevka 17.
4. **Konec igre**:
    - Igralec zmaga, če njegov seštevek kart preseže dealerjevega (a ostane ≤ 21) ali če dealer preseže 21.
    - Igralec izgubi, če preseže 21 ali če ima dealer višji seštevek.
    - Rezultat je neodločen, če imata oba enak seštevek.
---

## Mehanika igre

- **Karte**: V igri je uporabljen standardni komplet 52 kart. Vrednosti kart so naslednje:
    - Številske karte (2–10): njihova nominalna vrednost.
    - Figure (J, Q, K): vrednost 10.
    - As (A): vrednost 1 ali 11, odvisno od tega, katera je ugodnejša za igralca ali dealerja.
- **Stave**: Igralec lahko stavi virtualne žetone, ki se dodajo ali odvzamejo glede na izid igre.
- **Štetje točk**: Program avtomatsko izračuna seštevke in preveri stanje igre.
---

## Elementi igre

- **Uporabniški vmesnik**: Preprost in intuitiven prikaz kart, seštevkov in možnih potez (Hit, Stand, Split, Double Down).
- **Dealer**: Simuliran z računalniškim algoritmom, ki sledi strogim pravilom vlečenja kart.
- **Igralec**: Možnost prilagajanja strategije z različnimi potezami.

## Tedenski napredek (Gameplay)

- **Teden 1 (10.12)**:
    - Začetki implementacije game screen-a.
- **Teden 2 (17.12)**:
    - Deljenje kart igralcu in dealerju.
    - Imlementacija štetja vrednosti kart.
    - Implementacija možnosti igralca (Hit, Stand, Double, Split).
- **Teden 3 (7.1)**:
    - Implementacija stave.
    - Implementacija pravil dealerja.
    - Implementacija rezultata igre

