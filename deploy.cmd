chcp 65001
pushd %~dp0
call mvn package -Dproject.build.sourceEncoding=UTF-8 -Dproject.reporting.outputEncoding=UTF-8
java -jar ./target/filmorate-0.0.1-SNAPSHOT.jar

