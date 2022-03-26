# FER Sigurnost računalnih sustava 2021./2022. Laboratorijska vježba 1

Autor: Ivan Jeržabek 0036526653

---

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