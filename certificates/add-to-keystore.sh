#!/bin/sh

parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )

cp $parent_path/itemis.de.cer $JAVA_HOME/lib/security/
cd $JAVA_HOME/lib/security/
keytool -import -alias itemis.de -file itemis.de.cer -keystore cacerts -storepass changeit -noprompt
