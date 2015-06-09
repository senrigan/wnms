package com.gdc.nms.server.testutil;

import java.util.List;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestInformed {
	private JUnitCore junit=new JUnitCore();
	private Result resulTest;
	private long ignoreCaseCount;
	private long failuresCaseCount;
	private long TestedCaseCount;
	private long getRuntimeCount;
	private List<Failure> failuresTrace;
	
	public void runTestClass(Class<?> testClass){
		resulTest=junit.run(testClass);
		failuresCaseCount=resulTest.getFailureCount();
		ignoreCaseCount=resulTest.getIgnoreCount();
		getRuntimeCount=resulTest.getRunTime();
		TestedCaseCount=resulTest.getRunCount();
		failuresTrace=resulTest.getFailures();
		
	}
	
	public List<Failure> getFailuresTrace() {
		return failuresTrace;
	}

	public boolean testIsSucessful(){
		return resulTest.wasSuccessful();
	}
	public long getIgnoreCaseCount() {
		return ignoreCaseCount;
	}

	public long getFailuresCaseCount() {
		return failuresCaseCount;
	}

	public long getTestedCaseCount() {
		return TestedCaseCount;
	}

	public long getGetRuntimeCount() {
		return getRuntimeCount;
	}

	@Override
	public String toString() {
		return "TestInformed [junit=" + junit + ", resulTest=" + resulTest
				+ ", ignoreCaseCount=" + ignoreCaseCount
				+ ", failuresCaseCount=" + failuresCaseCount
				+ ", TestedCaseCount=" + TestedCaseCount + ", getRuntimeCount="
				+ getRuntimeCount + ", failuresTrace=" + failuresTrace + "]";
	}

}
