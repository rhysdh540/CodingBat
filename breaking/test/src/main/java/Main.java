import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Vector;

public class Main {
	public static void main(String[] args) throws Throwable {
		sun.corba.Bridge b = sun.corba.Bridge.get();
		sun.misc.Unsafe u = (sun.misc.Unsafe) b.getObject(b, 16);

//		ClassLoader cl = Main.class.classLoader;
		ClassLoader cl = (ClassLoader) u.getObject(Main.class, 24L);
		if (cl == null) {
			throw new NullPointerException("ClassLoader is null");
		}

		System.out.println(cl);
	}
}
