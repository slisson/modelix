#!/bin/sh

ls $JAVA_HOME
cp certificates/itemis.de.cer $JAVA_HOME/lib/security/
cd $JAVA_HOME/lib/security/
keytool -import -alias itemis.de -file itemis.de.cer -keystore cacerts -storepass changeit -noprompt
