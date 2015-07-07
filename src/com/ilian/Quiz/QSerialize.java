package com.ilian.Quiz;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
@Deprecated
public class QSerialize  {

	public static Object deserialize2(final String name) {
		Object o = null;
		Class<?> c = null;
		try {
			c = Class.forName(name);
		} catch ( Exception ex1) { }
		try {
			o = c.newInstance();
			
		} catch ( Exception ex2 ) { }
		try {
			
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			o = ois.readObject();
			ois.close();
			fis.close();
			return o;
		} catch (Exception ex) {
			System.out.println("REFLECTION DESREILIAZATION FAILED");
			return null;
		}
	}
	
	public static void serialize2(final Object o, final String name) {
		
				Class<?> c = null;
				try {
					c = Class.forName(name);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Object type = null;
				try {
					type = c.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					FileOutputStream fos = new FileOutputStream(name);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(type);
					oos.close();
					fos.close();
				} catch (Exception ex2) {
					System.out.println("REFLECTION SERIALIZE FAILED");
				}
	}
	public static synchronized void serialize(final Object obj, final String name) {
		new Thread(new Runnable() {
			
		public void run() {
			
			try {
				FileOutputStream fos = new FileOutputStream(name+".ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(obj);
				oos.close();
				fos.close();
				System.out.println("Serialized - OK");
			} catch ( Exception ex ) {
				ex.printStackTrace();
				System.out.println(obj.toString()+" failed to serialize");
			}
			
		}
	}).start();
	}
	
	public static Object deserialize(String name) {
		Object ob = null;
		try {
			FileInputStream ios = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(ios);
			ob = ois.readObject();
			ois.close();
			ios.close();
		
		} catch ( Exception ex ) {
			System.out.println(ob.toString()+" failed to deserialize");
			ob = null;
		}
		return ob;
		
	}	
	
}
