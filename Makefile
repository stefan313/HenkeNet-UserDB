jc=javac
mysqlpath=mysql.jar
$PHONY=clean_all

HenkeNet-UserTool.jar: ${mysqlpath} #dependencies adden TODO!
	mkdir build
	${jc} -cp ${mysqlpath} -d ./build/ -verbose ./*.java > java_compile.log
	jar cvef MainControl  HenkeNet-UserTool.jar ./build/*.class HenkeNet-128x128.png > java_build.log

truststore: ca-cert.pem server-cert.pem
	keytool -import -alias myCACert -file ca-cert.pem -trustcacerts -keystore truststore
	keytool -import -alias myServerCert -file server-cert.pem -keystore truststore
	echo "update configuration.txt!"

clean_java:
	rm -rfv build/
	rm -fv HenkeNet-UserTool.jar
	rm java_compile.log
	rm -fv java_build.log
