cd ..
javac -d classes -classpath ".;src" src/fshare/commun/*.java
javac -d classes -classpath ".;src" src/fshare/gui/*.java
javac -d classes -classpath ".;src" src/fshare/serveur/*.java
javac -d classes -classpath ".;src" src/fshare/client/*.java
javac -d classes -classpath ".;src" src/fshare/remote/*.java

rmic -d classes -classpath "src" fshare.client.ClientImpl
rmic -d classes -classpath "src" fshare.serveur.RemoteServeurImpl

pause
