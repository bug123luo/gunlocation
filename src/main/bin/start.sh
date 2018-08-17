#!/bin/bash
  
if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi
 
bin=`dirname "$1"`
 
export MYJETTY_HOME=`cd $bin/../; pwd`
 
MYJETTY_CONF_DIR=$MYJETTY_HOME/conf
CLASSPATH="${MYJETTY_CONF_DIR}"
 
for f in $MYJETTY_HOME/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
 
LOG_DIR=${MYJETTY_HOME}/logs
 
CLASS=com.tct.server.GunLocationMQServer
nohup ${JAVA_HOME}/bin/java -classpath "$CLASSPATH" $CLASS > ${LOG_DIR}/myjetty.out 2>&1 < /dev/null &
