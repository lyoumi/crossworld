export JAVA_HOME=`/usr/libexec/java_home -v 11`; java -version
cd auth-service/
gradle build
cd ../core/
gradle build
cd ../scheduler/
gradle build
cd ../rest/
gradle build
