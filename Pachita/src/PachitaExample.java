import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.Device.Type;


public class PachitaExample {
	public static void main(String[] args) {

		System.out.println(new Date(1432844787006L));
		
		String palabra="12345678910.txt";
		String pal2=String.format("%-20s%10s",palabra,"mundo");
		PachitaExample pach=new PachitaExample();
		pach.updateArray(new ArrayList<Integer>());
		Device device=new Device("192.168.207.40");
		device.setType(Type.MOCK);
		
		if(device.getType()==Type.MOCK){
			System.out.println("si entra ");
		}
		
		
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
