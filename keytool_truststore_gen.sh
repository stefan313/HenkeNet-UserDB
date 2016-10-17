#!/bin/bash
# TODO: Testen!!!!!
if[${1} == ""]
then
	echo "meh, kein cert (${1})"
	exit 1
elif [ -f ${1}]
then
	echo "starten"
else
	echo "meh, pfad doof"
	exit 1
fi

echo "generating truststore for UserTool with ${1}!"
read -p "PW for truststore:" truststorepw
# https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-using-ssl.html
keytool -v -import -alias mysqlServerCACert -file ${1} -keystore truststore

echo "keytool has created truststore"
echo "creating keystore for Usertool, geneating new key"
read -p "PW for keystore:" keystorepw
keytool -v -genkey -keyalg rsa -alias mysqlClientCert -keystore keystore

echo "adding files to your config, CAUTION!: absolute Paths!"

# Absoluter Pfad für keystores!
$pwd = pwd
echo "keyStorePath=${pwd}/keystore" >> configuration.txt
echo "keyStorePassword=${keystorepw}" >> configuration.txt
echo "trustStorePath=${pwd}/truststore" >> configuration.txt
echo "trustStorePassword=${truststorepw}" >> configuration.txt
