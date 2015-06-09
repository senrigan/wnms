package example;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

public class TestRuler {
	@Rule
	public SimpleOnFail ruleExample=new SimpleOnFail();
	@Test
	public void test1(){
		assertTrue(1==2);
	}
	
	
	@Test
	public void test2(){
		assertTrue("hola".equals("hola"));
	}
}
