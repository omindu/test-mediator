# test-mediator

## How to build

1. Navigate to project root and build with `mvn clean install`.

## How to deploy

1. Copy the built jar from `target` directory to `<EI_HOME>/dropins` direcoty.
2. Restart the server.

## Clear mediator cache from JMX console

1. Connect to the EI server using Jconsole.
2. Locate `org.wso2.sample.mediator.mbean.TestMediatorCacheClearMXBean`.
3. Select the operations of the mbean and click `clear`.

