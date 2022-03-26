mvn compile

# Primjer inicijalizacije baze
java -cp target/classes hr.fer.srs.PasswordManager init p4ssw0rd

# Primjer pokusaja ispisa iz baze
java -cp target/classes hr.fer.srs.PasswordManager get p4ssw0rd www.fb.com

# Unos u bazu
java -cp target/classes hr.fer.srs.PasswordManager put p4ssw0rd www.fb.com pass123

java -cp target/classes hr.fer.srs.PasswordManager get p4ssw0rd www.fb.com

# Primjer ispisa greške pri pogrešnoj master lozinki
java -cp target/classes hr.fer.srs.PasswordManager get wrong-password www.fb.com