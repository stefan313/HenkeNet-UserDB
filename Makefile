jc=javac
cp= cp -fv
mysqlpath=mysql.jar
.PHONY: all clean-java clean-all deploy clean-truststore

all: deploy clean-java

HenkeNet-UserTool.jar: ${mysqlpath} #dependencies adden TODO!
	${jc} -cp ${mysqlpath} -d ./ -verbose ./*.java > java_compile.log
	jar cvemf MainControl MANIFEST.MF HenkeNet-UserTool.jar ./*.class HenkeNet-128x128.png > java_build.log

truststore: ca-cert.pem server-cert.pem
	keytool -import -alias myCACert -file ca-cert.pem -trustcacerts -keystore truststore
	keytool -import -alias myServerCert -file server-cert.pem -keystore truststore
	echo "update configuration.txt!"

deploy: HenkeNet-UserTool.jar truststore configuration.txt
	mkdir -p deploy
	${cp} truststore deploy/
	${cp} HenkeNet-UserTool.jar deploy/
	${cp} configuration.txt deploy/
	${cp} ${mysqlpath} deploy/mysql-deploy.jar

clean-all: clean-java clean-deploy clean-truststore

clean-java:
	rm -fv *.class
	rm -fv HenkeNet-UserTool.jar
	rm -fv java_compile.log
	rm -fv java_build.log

clean-deploy:
	rm -rfv deploy

clean-truststore:
	rm -fv truststore
