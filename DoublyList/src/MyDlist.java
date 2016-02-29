/**
 * Copy Right Information: MyDlist
 * JDK version used: jdk1.8.0_11
 * @version: 1.0
 * @author Xi Zhang (3472528)
 */
import java.io.*;
import java.util.Scanner;

/**
 * This subclass extends the doubly linked list class Dlist given in the
 * textbook Chapter3.3.3, implementing the following constructors and methods.
 */
public class MyDlist extends DList {

	private Scanner s;
	private BufferedReader buffer;

	/** This constructor creates an empty doubly linked list. */
	public MyDlist() {
		super();
	}

	/**
	 * This constructor creates a doubly linked list by reading all strings from
	 * a file named f. Assume that adjacent strings in the file f are separated
	 * by one or more white space characters. If f is “stdin”, MyDlist(“stdin”)
	 * creates a doubly linked list by reading all strings from the standard
	 * input. Assume that each input line is a string and an empty line denotes
	 * end of input.
	 * 
	 * @throws IOException
	 */
	public MyDlist(String f) throws IOException {
		super();
		if (f.equals("stdin")) {
			s = new Scanner(System.in);
			while (true) {
				String m = s.nextLine();
				if (m.equals("")) {
					break;
				}
				DNode v1 = new DNode(m, null, null);
				addLast(v1);
			}
		} else {
			File file = new File(f);
			FileReader reader = new FileReader(file);
			buffer = new BufferedReader(reader);
			String str1 = buffer.readLine();
			while (str1 != null) {
				for (String m : str1.split("[ \t]+")) {
					if (!m.equals("")) {
						DNode v1 = new DNode(m, null, null);
						addLast(v1);
					} else
						continue;
				}
				str1 = buffer.readLine();
			}
		}
	}

	/**
	 * This class method creates an identical copy of a doubly linked list u and
	 * returns a reference to the resulting doubly linked list.
	 * 
	 * @return newList(cloned)
	 */
	public static MyDlist cloneList(MyDlist u) {
		MyDlist newList = new MyDlist();
		DNode node = u.header.next;
		while (node != u.trailer) {
			DNode v1 = new DNode(node.element, null, null);
			newList.addLast(v1);
			node = node.next;
		}
		return newList;
	}

	/**
	 * This instance method removes all the nodes whose elements are equal to e.
	 * If such a node does not exist, this method will print “ no node contains
	 * e!” on the standard output.
	 */
	public void removeNode(String e) {
		DNode node = header.next;
		int tmpSize = this.size;
		while (node != trailer) {
			if (node.element == e) {
				node = getNext(node);
				remove(node.prev);
			} else {
				node = getNext(node);
			}
		}
		if (this.size == tmpSize) {
			System.out.println(" no node contains " + e + "!");
		}
	}

	/**
	 * This class method concatenates two doubly linked lists u and v into a
	 * single doubly linked list and returns a reference to the resulting doubly
	 * linked list. In the resulting doubly linked list, the linked list u
	 * precedes the linked list v.
	 * 
	 * @return newList(concatenated)
	 */
	public static MyDlist concatenateList(MyDlist u, MyDlist v) {
		MyDlist newList = new MyDlist();
		DNode node = u.header.next;
		while (node != u.trailer) {
			DNode v1 = new DNode(node.element, null, null);
			newList.addLast(v1);
			node = node.next;
		}
		node = v.header.next;
		while (node != v.trailer) {
			DNode v1 = new DNode(node.element, null, null);
			newList.addLast(v1);
			node = node.next;
		}
		return newList;
	}

	/**
	 * This instance method prints all elements of a list on the standard
	 * output, one element per line.
	 */
	public void printList() {
		DNode node = header.next;
		while (node != trailer) {
			System.out.println(node.element);
			node = getNext(node);
		}
	}

	public static void main(String[] args) throws Exception {

		System.out
				.println("please type some strings, one string each line and an empty line for the end of input:");
		/**
		 * Create the first doubly linked list by reading all the strings from
		 * the standard input.
		 */
		MyDlist firstList = new MyDlist("stdin");

		/** Print all elements in firstList */
		firstList.printList();

		/**
		 * Create the second doubly linked list by reading all the strings from
		 * the file myfile that contains some strings.
		 */
		MyDlist secondList = new MyDlist("myfile");

		/** Print all elements in secondList */
		secondList.printList();

		/** Insert "data" into firstList */
		firstList.addFirst(new DNode("data", null, null));

		/** insert "structures" into firstList */
		firstList.addFirst(new DNode("structures", null, null));

		/** Print all elements in firstList. */
		firstList.printList();

		/** Insert "data" into secondtList */
		secondList.addFirst(new DNode("data", null, null));

		/** insert "structures" into secondList */
		secondList.addFirst(new DNode("structures", null, null));

		/** Print all elements in secondList. */
		secondList.printList();

		/** Concatenate firstList and secondList into thirdList */
		MyDlist thirdList = concatenateList(firstList, secondList);

		/** Print all elements in thirdList. */
		thirdList.printList();

		/** Remove all the nodes in thirdList that contains "data". */
		thirdList.removeNode("data");

		/** Print thirdList. */
		thirdList.printList();

		/** Remove all the nodes in thirdList that contains "structures". */
		thirdList.removeNode("structures");

		/** Print thirdList. */
		thirdList.printList();

		/** Clone thirdList */
		MyDlist fourthList = cloneList(thirdList);

		/** Print all elements in fourthList. */
		fourthList.printList();
	}
}
