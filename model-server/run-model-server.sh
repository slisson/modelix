#!/bin/sh

java -Djdbc.url=$jdbc_url -cp "model-server/build/libs/*" org.modelix.model.server.Main
