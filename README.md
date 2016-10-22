# HenkeNet-UserDB

Erstellen Truststore
keytool -import -alias myCACert -file ca-cert.pem -trustcacerts -keystore truststore // fügt ein CA - Cert hinzu (die option -trustcacerts ist wichtig)
keytool -import -alias myServerCert -file server-cert.pem -keystore truststore // fügt ein Server-Cert hinzu
