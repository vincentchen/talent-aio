call mvn -f pom-dist.xml clean install

rd ..\..\..\dist\talent-aio-examples-im-server-1.0.1.v20170214-RELEASE /s /q

copy target\talent-aio-examples-im-server-1.0.1.v20170214-RELEASE.zip ..\..\..\dist\talent-aio-examples-im-server-1.0.1.v20170214-RELEASE.zip
