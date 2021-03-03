javac -cp "lib/*;." bank/*.java
javac -cp "lib/*;." client/*.java
javac -cp "lib/*;." server/*.java
jar cvf bank.jar bank/*.class
