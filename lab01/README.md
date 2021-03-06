# FER Sigurnost računalnih sustava 2021./2022. Laboratorijska vježba 1

Autor: Ivan Jeržabek 0036526653

---

### Opis sustava

Sav sigurnosni protokol odvija se u razredu **SecureStoreIO**. Svi podaci pohranjeni su unutar jedne binarne datoteke *(passwordManager.bin)*.

Prilikom pohrane podataka koristimo sljedeći postupak:

1) pomoću javinog *SecureRandom* sigurnog kriptografskog generatora nasumičnih brojeva stvaramo 16-byteni inicijalizacijski vektor i salt *(ovo se dešava svaki puta kada pohranjujemo datoteku - **čime osiguravamo sigurnosne zahtjeve**)*
2) Pomoću nekoličine pomoćnih razreda generiramo AES 256 bitni ključ algoritmom PBKDF2 sa HMAC SHA-256 na temelju master zaporke te salt-a *(dakle ovdje koristimo autentificiranu šifru)*
3) Nakon toga pomoću AES ključa te inicijalizacijskog vektora enkriptiramo 32 okteta "preambule" te sami tekstualni sadržaj. Preambula je nasumično generirano smeće koje dolazi prije samog tekstualnog sadržaja koje služi tome da nikad ne bismo enkriptirali prazan sadržaj.
4) Konačno pohranimo u prvih 16 okteta datoteke inicijalizacijski vektor, u idućih 16 okteta pohranjujemo salt, te nakon toga slijedi enkriptirani sadržaj.

Prilikom čitanja pohranjenih podataka dešava se esencijalno isti postupak ali u suprotnom redoslijedu.


### Upute za pokretanje

Na računalu je potrebno instalirati prikladnu verziju jave (11) te Maven. Podrazumijeva se da sve
navedene naredbe izvodimo iz korijenskog direktorija projekta.

Kako bi se izvorni kod mogao izvoditi mora se prevesti, to činimo naredbom:

    mvn compile

Nakon toga moguće je pokrenuti program sljedećom naredbom:

    java -cp target/classes hr.fer.srs.PasswordManager [zastavice]

Umjesto `[zastavice]` bloka upisujemo zastavice ovisno o funkcionalnosti koju želimo izvršiti.
Moguće naredbe su **init**, **get** i **put**:

    java -cp target/classes hr.fer.srs.PasswordManager init [master password]

    java -cp target/classes hr.fer.srs.PasswordManager get [master password] [entry name]

    java -cp target/classes hr.fer.srs.PasswordManager put [master password] [new entry name] [password]

Unutar repozitorija se nalazi **demo.sh** shell skripta koja izvodi upravo ove naredbe te
demonstrira jednostavan rad aplikacije.