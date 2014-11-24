package com.tibco.streambase.contrib.jmsxml.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.streambase.sb.unittest.Expecter;
import com.streambase.sb.unittest.JSONSingleQuotesTupleMaker;
import com.streambase.sb.unittest.SBServerManager;
import com.streambase.sb.unittest.ServerManagerFactory;

public class TestJMSXML {

	private static SBServerManager server;

	@BeforeClass
	public static void setupServer() throws Exception {
		// create a StreamBase server and load applications once for all tests in this class
		server = ServerManagerFactory.getEmbeddedServer();
		server.startServer();
		// this will run with the default JMS provider -- for this project, this is tibems
		server.loadApp("jms-xml-sample.sbapp");
	}

	@AfterClass
	public static void stopServer() throws Exception {
		if (server != null) {
			server.shutdownServer();
			server = null;
		}
	}

	@Before
	public void startContainers() throws Exception {
		// before each test, startup fresh container instances
		server.startContainers();
	}

	@Test
	public void testYHOO100() throws Exception {
		server.getEnqueuer("TestYHOO100").enqueue(
				JSONSingleQuotesTupleMaker.MAKER, "{}");
		new Expecter(server.getDequeuer("XMLOut"))
				.expect(JSONSingleQuotesTupleMaker.MAKER,
						"{'text':'<quote><symbol>YHOO</symbol><bid>100.0</bid></quote>'}");
		new Expecter(server.getDequeuer("TupleOut")).expect(
				JSONSingleQuotesTupleMaker.MAKER,
				"{'quote':{'symbol':'YHOO','bid':100.0}}");
	}

	@After
	public void stopContainers() throws Exception {
		// after each test, dispose of the container instances
		server.stopContainers();
	}

}
