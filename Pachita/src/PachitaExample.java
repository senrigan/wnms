import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.gdc.nms.model.Device;
import com.gdc.nms.server.ServerManager;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.DriverManager;


public class PachitaExample {
	public static void main(String[] args) {

		System.out.println(new Date(1432844787006L));
		
		String palabra="12345678910.txt";
		String pal2=String.format("%-20s%10s",palabra,"mundo");
		PachitaExample pach=new PachitaExample();
		pach.updateArray(new ArrayList<Integer>());
		
		
		
	}
	
	private static void updateArray(List<Integer> numer){
		System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
		for(Integer nm:numer){
			nm=0;
			System.out.println(nm);
		}
		System.out.println(numer);
	}
}
