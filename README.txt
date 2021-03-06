INTRODUCTION:

This component demonstrates the use of the JMS Consumer and 
JMS Producer embedded adapters with XML-formatted TextMessages. 
The default JMS TextMessage converters (provided with the 
adapter) are used to convert incoming JMS messages to XML 
strings. These converters only require StreamBase and generic 
JMS classes, so they can be used with multiple JMS providers. 

Example configurations for Tibco EMS and Apache ActiveMQ are included 
in this project.

The XML strings are converted to StreamBase tuples based on a 
defined named schema (QuoteSchema). This additional conversion 
is done using the XML-to-Tuple operator that ships with 
StreamBase CEP (see the product help for details on configuring 
the XML-to-Tuple operator).

SETUP:

TIBCO EMS

This project assumes that there's a Tibco EMS 8.3 server 
running on localhost (tcp://localhost:7222), as configured in
the project's sbd.sbconf.
 
The above setting directs the application to include the 
tibems.xml options in the project's configuration. These 
additional options specify the necessary EMS libraries that 
are referenced by the JMS adapters. For EMS 8.3, these are:

${TIBCO_HOME}/ems/8.3/lib/tibjms.jar
${TIBCO_HOME}/ems/8.3/lib/jms-2.0.jar
${TIBCO_HOME}/ems/8.3/lib/tibcrypt.jar

Apache ActiveMQ

However, the project can also used with a local ActiveMQ 
server by selecting the Server Name property in the JMS adapters
to either activemqjndi or activemqdirect.

The sbconf file also adds the necessary ActiveMQ libraries to
the server classpath. For ActiveMQ 5.15.0, for example,
there is only one library reference needed:

${ACTIVEMQ_HOME}/activemq-all-5.15.0.jar

For each supported provider, you'll also need to set an 
additional environment variable to specify its home directory. 
For example:

  TIBCO_HOME=C:/tibco

     or
  
  ACTIVEMQ_HOME=C:/apache-activemq-5.15.0

These variables are used by the sbconf settings for 
each provider.
 
**Note: make sure these variables are set in the environment which 
launches Studio, to ensure Studio will see the values on startup. 
So these variables should be set *before* launching Studio.. they 
should *not* be set in the application's Run Configuration.

ENSURE THAT THE JMS PROVIDER IS RUNNING AND CONFIGURED FOR THIS
SAMPLE:

1. Open a StreamBase Command Prompt (or terminal 
window if you're on Linux), and set your provider-specific
environment variables.

  Tibco EMS example (Linux):
    set JMS_PROVIDER=tibems
    set TIBCO_HOME=C:/tibco 

  Tibco EMS example (Windows CMD shell):
    set JMS_PROVIDER=tibems
    set TIBCO_HOME=C:\TIBCO
	PATH=%PATH%;%TIBCO_HOME%\ems\8.3\bin
	
  ActiveMQ example (Linux):
    set JMS_PROVIDER=activemq
    set ACTIVEMQ_HOME=C:/apache-activemq
    
  ActiveMQ example (Windows CMD shell):
    set JMS_PROVIDER=activemq
    set ACTIVEMQ_HOME=C:\apache-activemq

2. If necessary, start up the JMS Provider and create the topics used
   by this sample application

  Tibco EMS example (Linux):
    tibemsd &

  Tibco EMS example (Windows CMD shell):
    start tibemsd
    
  ActiveMQ example (Linux):
    cd $ACTIVEMQ_HOME
    ./bin/activemq start
    
  ActiveMQ example (Windows CMD shell):
    run_activemq.cmd
    
3. If not already done once for this JMS provider instance, create the
   connection factory and topics.

   Tibco EMS example
    tibemsadmin -script emscreate.scr  

   ActiveMQ
    <not needed - connection factory and topics create by default or dynamically with default install>

RUNNING THE SAMPLE FROM STUDIO:

1. Open a StreamBase Command Prompt (or terminal 
window if you're on Linux), and set your provider-specific
environment variables.

  Tibco EMS example:
    set JMS_PROVIDER=tibems
    set TIBCO_HOME=C:/tibco   

  ActiveMQ example:
    set JMS_PROVIDER=activemq
    set ACTIVEMQ_HOME=C:\java\apache-activemq-5.15.0

2. From the same prompt, launch Studio using the command:

  sbstudio
  
3. Although you have included references to your JMS provider's 
libraries via the <sb-include> setting in sbd.sbconf, these also 
need to be explicitly referenced in the Studio Build Path.  In the 
Package Explorer in Studio, right-click on the project root folder, 
and select "Build Path > Configure Build Path", then select 
'add external JARs' on the 'Libraries' tab.  Then select the JARs 
for your provider (e.g. tibjms.jar, jms.jar, tibcrypt.jar).

4. Right-click on the 'jms-sample.sbapp' in the project root, and 
select "Run As > StreamBase Application".  The console should 
report that the connection to your provider is up and topics 
have been created.

5. You can run the supplied 'MessagesToSendToJMS.sbfs' feed 
simulation to simulate data that is converted to JMS messages 
and sent to the JMS server.  There are also manual input streams 
to enter single XML strings or tuples. This can be done in the 
Manual Input view in the Test/Debug perspective.


RUNNING THE SAMPLE FROM THE COMMAND LINE:

1. Open a StreamBase Command Prompt (or terminal 
window if you're on Linux), and set your provider-specific
environment variables.

  Tibco EMS example (Linux):
    set JMS_PROVIDER=tibems
    set TIBCO_HOME=/opt/tibco 

  Tibco EMS example (Windows CMD shell):
    set JMS_PROVIDER=tibems
    set TIBCO_HOME=C:\tibco 

  ActiveMQ example (Linux):
    set JMS_PROVIDER=activemq
    set ACTIVEMQ_HOME=/opt/apache-activemq
    
  ActiveMQ example (Windows CMD shell):
    set JMS_PROVIDER=activemq
    set ACTIVEMQ_HOME=C:\java\apache-activemq-5.15.0
    
2. Start the application using the following command:

  sbd -f sbd.sbconf jms-xml-sample.sbapp
   
3. Open a second StreamBase Command Prompt, and confirm that tuples 
are flowing through to the 'TupleOut' and 'XMLOut' output streams:

  sbc deq TupleOut XMLOut

4. Open a third StreamBase Command Prompt, and initiate the 
feed simulation using the command:

  sbfeedsim MessagesToSendToJMS.sbfs
  
5. Open a fourth StreamBase Command Prompt, and shut down all
the processes:

  sbadmin shutdown
  
For TIBCO EMS only (because the 
jms-xml-dynamic-destination-sample.sbapp currently uses the 
EMS-specific > wildcard topic name):
  
6. In the first command prompt, start the application using
the following command:

  sbd -f sbd.sbconf jms-xml-dynamic-destination-sample.sbapp
   
7. Using the second StreamBase Command Prompt, and confirm that tuples 
are flowing through to the 'TupleOut' and 'XMLOut' output streams:

  sbc deq TupleOut XMLOut

8. Using the third StreamBase Command Prompt, and initiate the 
feed simulation using the command:

  sbfeedsim MessagesToSendToJMSVRZN.sbfs
  
9. Using the fourth StreamBase Command Prompt, and shut down all
the processes:

  sbadmin shutdown  

 
Version History:
1.5 Issue #7: 
      add jms-xml-dynamic-destination-sample.sbapp
      add instructions for jms-xml-dynamic-destination-sample.sbapp to README.txt
    Issue #8
      add conditional bid generation for each symbol in the feedsims
    Issue #9
      update to Apache ActiveMQ 15.5.0 and validate
    Issue #10
      update README.txt to EMS 8.3 (correction)
    Issue #11
      Add JMS Server configuration for Apache ActiveMQ direct connection (ConnectionFactory)
            
1.4 Issue #4: Update use JMS Consumer and JMS Producer adapters under StreamBase
    7.7.0; use TIBCO EMS 8.3; use Apache ActiveMQ 5.14.5; corrections
    to run instructions; replace YHOO with VRZN; remove dynamic 
    destination consume and produce; add MessagesToSendToJMSVRZN.sbfs
    
    Issue #2: use TupleToXML Passthrough field and simplify producer logic

    Issue #3: format bid to 2 decimal places in XML

1.3 Update to StreamBase CEP 7.4, EMS 8.0, and ActiveMQ 5.10.0; use
    TupleToXML operator in place of string concatenation; rename
    JMS Adapter configuration file suffixes to jmsconf from sbconf;
    add topics.sample.conf, emscreate.scr, emsdelete.scr, and
    run_activemq.cmd to project; add Windows JMS startup;
    commands to README.txt; reorder cmd line deq and sbfeedsim;
    add SB JUnit test for YHOO 100.0; rename jms-sample.sbapp to
    jms-xml-sample.sbapp; add Version History to README.txt

1.2 Conflicting update to CEP 7.4, EMS 8.0 and ActiveMQ 5.10.0

1.1 Erroneous version that should not have been uploaded to the exchange
       
1.0 Initial version; based on StreamBase 7.3 and EMS 7.0; tuple-to-XML
    translation done by string concatenation


-end-
