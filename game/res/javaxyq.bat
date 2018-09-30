@echo off
set myclasspath=lib/*;game-1.5.jar

java -Xmx128m -classpath ./lib/*;./game-1.5.jar  com.javaxyq.core.DesktopApplication

pause