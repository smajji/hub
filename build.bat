@echo off
echo Setting JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_111
set PATH=%JAVA_HOME%\bin;%PATH%
set M2_HOME=C:\apache-maven-3.3.9
set PATH=%M2_HOME%\bin;%PATH%
mvn clean package