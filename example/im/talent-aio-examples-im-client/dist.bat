call mvn -f pom-dist.xml clean install


rd ..\..\..\dist\talent-aio-examples-im-client-1.0.1.20170119-SNAPSHOT /s /q

copy target\talent-aio-examples-im-client-1.0.1.20170119-SNAPSHOT.zip ..\..\..\dist\talent-aio-examples-im-client-1.0.1.20170119-SNAPSHOT.zip

pause