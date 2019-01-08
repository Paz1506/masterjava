set LB_HOME=F:\Liquibase
call %LB_HOME%\liquibase.bat --driver=org.postgresql.Driver ^
--classpath=%LB_HOME%\lib ^
--changeLogFile=databaseChangeLogHW06.sql ^
--url="jdbc:postgresql://localhost:5432/masterjava" ^
--username=user ^
--password=password ^
migrate