#!/bin/sh
java -Xms64m -Xmx4096m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=~/java_talent-aio-im-server_pid.hprof -jar talent-aio-im-server.jar