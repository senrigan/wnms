package example;

import java.util.Arrays;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class SimpleOnFail extends TestWatcher{
	@Override
	protected void failed(Throwable e , Description description){
		System.out.println("ocurrio un fallo en el test");
		System.out.println("decription"+description);
		System.out.println("thorwabe"+e.getMessage());
		System.out.println("starcktrece"+Arrays.toString(e.getStackTrace()));
	}
	
	@Override
	 protected void succeeded(Description description) {
		System.out.println("paso el test"+description);
		System.out.println("method"+description.getMethodName());
		System.out.println("class name"+description.getClassName());
		System.out.println("display name"+description.getDisplayName());
	}
}
