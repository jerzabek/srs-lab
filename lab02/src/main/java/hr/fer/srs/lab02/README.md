# Sigurnost računalnih sustava - laboratorijska vježba 2

Sustav sigurne pohrane lozinki u obliku binarne datoteke.

Unutar binarne datoteke na početku pohranjujemo 16 okteta soli.
Nakon toga slijede podaci o korisnicima u formatu:

- korisničko ime
  - broj tipa long u 2 okteta, govori nam broj okteta teksta
  - toliko okteta teksta zapisanog u UTF-8 kodnoj stranici
- hashirana lozinka
  - broj tipa long u 2 okteta, govori nam broj okteta lozinke
  - toliko okteta hashirane lozinke pomoću PBKDF2WithHmacSHA256 algoritma
- boolean vrijednost da li korisnik pri prijavi mora resetirati lozinku
  - 1 oktet (vrijednost ili 0 ili 1)

Sigurnosni zahtjevi:

- tajnost
  - lozinke su hashirane
- povjerljivost
  - samo korisnik koji zna zaporku može
- izvornost
  - administrator je mogao promijeniti lozinku
- integritet
  - netko može promijeniti sadržaj binarne datoteke i mi to ne možemo uočiti