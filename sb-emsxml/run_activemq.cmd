set JAVA_HOME=%STREAMBASE_HOME%\jdk
set ACTIVEMQ_HOME=C:\apache-activemq-5.10.0
PATH=%JAVA_HOME%\bin;%ACTIVEMQ_HOME%\bin;%PATH%
cd %ACTIVEMQ_HOME%
activemq start
