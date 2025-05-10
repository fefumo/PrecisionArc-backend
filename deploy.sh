#!/bin/bash

WILDFLY_HOME=~/wildfly/wildfly-preview-26.1.3.Final

echo "Building project..."
if gradle clean assemble; then
    echo "Build successful. Deploying to WildFly..."
    cp ./build/libs/PrecisionArc.war $WILDFLY_HOME/standalone/deployments/
else
    echo "Build failed. Check errors above."
    exit 1
fi

echo "Starting WildFly..."
$WILDFLY_HOME/bin/standalone.sh &
echo "Deployment script completed."
