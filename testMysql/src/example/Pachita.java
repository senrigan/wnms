package example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class Pachita {
	private static int num=0;
	public static void main(String[] args) {
		Pachita.num++;
		Pachita.num++;
		System.out.println(""+Pachita.num);
//		String hola="hola";
//		 String s = String.format("%"+hola.length()+5+"s",hola);
//		 System.out.println(s);
//		 s=StringUtils.leftPad("12.23429837482",15);
//		 s+=StringUtils.leftPad("9.10212023134", 18);
//		 System.out.println(s);
		
		
		StringBuffer s2=new StringBuffer("holammm");
		String s=String.format("%s%10s",s2,"hola");
		System.out.println(s);
		//System.out.println(s2.indexOf("las"));
		System.out.println("contians"+s.contains("LA"));
		
		
	}
	
	
	@Test
	public void a() throws Exception  {
			ArrayList<String> ls=new ArrayList<String>();
			ls.add("a");
			ls.add("a");
			ls.add("a");
			ls.add("a");
			ls.add("a");
			ls.add("a");
//			ls.add("b");
			
			for(String s:ls){
				assertEquals("a",s);
			}
			throw new Exception("hola");
		
	
	}
}
