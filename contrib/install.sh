#!/bin/bash

if [ -d /build/artifacts ]; then
	cp /build/artifacts/*.jar $NUXEO_HOME/nxserver/bundles
fi


if [ -f /build/nuxeo.conf ]; then
	cp /build/nuxeo.conf /docker-entrypoint-init.d/
fi

if [ -d /build/marketplace ]; then
  echo "---> Installing Nuxeo Package for project from $LOCAL_SOURCE_DIR/$NUXEO_PACKAGE"  
  PACKAGE=$(/build/marketplace)
  echo "---> Found package $PACKAGE"
  /docker-entrypoint.sh nuxeoctl mp-init
  /docker-entrypoint.sh $NUXEO_HOME/bin/nuxeoctl mp-install $PACKAGE
fi