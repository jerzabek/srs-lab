mvn compile

#--------------------------------------------------------------------

java -cp target/classes hr.fer.srs.lab02.UserManagement add root

java -cp target/classes hr.fer.srs.lab02.UserManagement passwd root

java -cp target/classes hr.fer.srs.lab02.UserManagement forcepass root

java -cp target/classes hr.fer.srs.lab02.UserManagement del root

#--------------------------------------------------------------------

java -cp target/classes hr.fer.srs.lab02.Login root
