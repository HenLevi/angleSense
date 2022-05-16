package anglesense;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Main {
	private static final String LINE = "--------------------";

	private String getName(Object obj) {
		return obj.getClass().getSimpleName();
	}

	private boolean isString(Field field) {
		return String.class.isAssignableFrom(field.getType());
	}

	private String leftPadding(String input, int length) {
		return "\t".repeat(length) + input;
	}

	private void printClassName(Object obj, int pos) {
		System.out.println("\n" + leftPadding("Object of Class " + getName(obj), pos));
		System.out.println(leftPadding(LINE, pos));
	}

	private void printFields(String fieldName, String fieldValue, int pos) {
		System.out.println(leftPadding(fieldName + " = " + fieldValue, pos));
	}

	private boolean isPrimitiveArray(Object array) {
		return array.getClass().getComponentType().isPrimitive();
	}

	private Object[] toArray(Object obj) {
		int len = Array.getLength(obj);
		Object[] res = new Object[len];
		for (int i = 0; i < len; i++)
			res[i] = Array.get(obj, i);
		return res;
	}

	private String getArrayValue(Object array) {
		return Arrays.toString(toArray(array));
	}

	private void printObjectArray(Object array, int pos) throws IllegalAccessException {
		pos++;
		for (Object o : (Object[]) array)
			if (o != null)
				printStructure(o, pos);
	}

	private void analyzeArray(int pos, String fieldName, Object fieldValue) throws IllegalAccessException {
		if (fieldValue == null) {
			printFields(fieldName, null, pos);
		} else
			if (isPrimitiveArray(fieldValue)) { //if primitive array
			printFields(fieldName, getArrayValue(fieldValue), pos);
		} else {                                                               
			System.out.println(leftPadding(fieldName + " = ", pos));
			printObjectArray(fieldValue, pos);         
		}
	}
	

	@SuppressWarnings("unchecked")
	private void analyzeCollection(Object obj, int pos) throws IllegalAccessException {
		pos++;
		for (Object o : (Collection<Object>) obj)
			if (o != null)
				System.out.println(leftPadding(o.toString(), pos));
	}

	private void analyzeCollection(int pos, String fieldName, Object fieldValue) throws IllegalAccessException {
		if (fieldValue == null)
			printFields(fieldName, null, pos);
		else {
			System.out.println(leftPadding(fieldName + " = ", pos));
			analyzeCollection(fieldValue, pos);
		}
	}

	public void printStructure(Object obj) throws IllegalArgumentException, IllegalAccessException {
		printStructure(obj, 0);
	}

	private void printStructure(Object obj, int pos) throws IllegalArgumentException, IllegalAccessException {

		obj.getClass();
		pos++;
		printClassName(obj, pos);

		// print the field
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Object fieldValue = field.get(obj);

			if (isString(field)) {
				System.out.println(leftPadding(fieldName + " = " + fieldValue, pos));
			} else if (field.getType().isPrimitive()) {
				pos++;
				System.out.println(leftPadding(fieldName + " = " + fieldValue, pos));
			} else if (field.getType().isArray()) {
				System.out.println();
				analyzeArray(pos, fieldName, fieldValue);
			} else if (Collection.class.isAssignableFrom(field.getType())) {
				analyzeCollection(pos, fieldName, fieldValue);
			} else {
				System.out.println(leftPadding(fieldName + " = ", pos));
				printStructure(fieldValue, pos);
			}
		}
	}

	public static void main(String[] args) {

		Name n = new Name();
		n.firstName = "Hen";
		n.lastName = "Levi";

		Name n1 = new Name();
		n1.firstName = "Rotem";
		n1.lastName = "Levi";

		Name n2 = new Name();
		n2.firstName = "Lenny";
		n2.lastName = "Levi";

		Name n3 = new Name();
		n3.firstName = "Neil";
		n3.lastName = "Levi";

		n.arrayOfObj = new Object[] { n1, n2 };
		n.arrayOfInt = new int[] { 1, 2, 3 };

		n.ls = new ArrayList<>();
		n.ls.add("one");
		n.ls.add("two");

		Person p1 = new Person();
		p1.age = 36;
		p1.name = n;

		Main main = new Main();

		try {
			main.printStructure(p1, 0);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
