import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Copy Right Information: CompressedSuffixTrie 
 * JDK version used: jdk1.8.0_11
 * 
 * @version: 1.0
 * @author Xi Zhang (3472528)
 */
public class CompressedSuffixTrie {
	char[] T;
	SuffixTrieNode root = null;

	/**
	 * This is my data structures for the compressed suffix trie
	 * 
	 * @param f
	 */
	public CompressedSuffixTrie(String f) // Create a compressed suffix trie
											// from file f
	{
		String text = "";
		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new FileInputStream(f));
			while (s.hasNext()) {
				text = text + s.next();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		/*
		 * Adding "$" at the end of the string indicates that this is the end of
		 * a string.
		 */
		if (text.length() == 0 || text.charAt(text.length() - 1) != '$') {
			text = text + "$";
		}
		T = new char[text.length()];
		T = text.toCharArray(); // change the string to a char array
		makeTree();
	}

	/**
	 * This method is used to act as a main function of making tree. The time
	 * complexity of this method is O((|m|+2)*(|m|+1)/2), where m is the length
	 * of the string and (|m|+1) represents the length of string plus "$".
	 */
	private void makeTree() {
		root = new SuffixTrieNode();
		for (int i = 0; i < T.length; i++) {
			List<Integer> suffixList = new ArrayList<Integer>();
			for (int k = i; k < T.length; k++) {
				suffixList.add(k);
			}
			root.concatenate(suffixList, T);
		}
		root = compactNodes(root);
	}

	/**
	 * This method is used to making a compressed suffix tree by compact every
	 * node who has only one child and its child is not "$". The suffix tries
	 * will have |m| paths, where |m| is the length of string. The time
	 * complexity will be O(|m|*(|m|-1)/2). ("$" will be ignored when compacting
	 * a tree.)
	 * 
	 * @param node
	 * @return
	 */
	private SuffixTrieNode compactNodes(SuffixTrieNode node) {
		for (SuffixTrieNode child : node.children) {
			while (child.children.size() == 1) {
				SuffixTrieNode grandchild = child.children.get(0);
				/* do not add "$" in to a compacted node. */
				if (grandchild.label.equals("$")) {
					break;
				}
				child.label += grandchild.label;
				child.endPos++;
				child.children = grandchild.children;
				for (SuffixTrieNode grandchild1 : child.children)
					grandchild1.parent = child;
			}
			/*
			 * return the node which has at least two children as a start node
			 * for further compressing.
			 */
			child = compactNodes(child);
		}
		return node;
	}

	/**
	 * Method for finding the first occurrence of a pattern s in the DNA
	 * sequence. Since compressed suffix tree is used for storing the original
	 * string, the matching time complexity is O(|s|) (|s| is the length of s).
	 * 
	 * @param s
	 * @return
	 */
	public int findString(String s) {
		int p = s.length();
		char[] P = s.toCharArray();
		int j = 0;
		SuffixTrieNode v = this.root;
		while (true) {
			boolean flag = true;
			for (SuffixTrieNode w : v.children) {
				int i = w.startPos;
				if (P[j] == T[i]) {
					int x = w.endPos - i + 1;
					if (p <= x) {
						for (int p1 = 1; p1 < p; ++p1) {
							if (P[j + p1] != T[i + p1]) {
								return -1;
							}
						}
						return i - j;
					} else {
						for (int p1 = 1; p1 < x; ++p1) {
							if (P[j + p1] != T[i + p1]) {
								return -1;
							}
						}
						p = p - x;
						j = j + x;
						v = w;
						flag = false;
						break;
					}
				}
			}
			if (flag || v.label.equals("$")) {
				break;
			}
		}
		return -1;
	}

	/**
	 * Method for computing the degree of similarity of two DNA sequences stored
	 * in the text files f1 and f2. The time complexity is O(m*n).
	 * 
	 * @param f1
	 * @param f2
	 * @param f3
	 * @return
	 */
	public static float similarityAnalyser(String f1, String f2, String f3) {
		String text1 = "";
		String text2 = "";
		String text3 = "";
		int[][] L;

		try {
			@SuppressWarnings("resource")
			Scanner s1 = new Scanner(new FileInputStream(f1));
			while (s1.hasNext()) {
				text1 = text1 + s1.next();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			@SuppressWarnings("resource")
			Scanner s2 = new Scanner(new FileInputStream(f2));
			while (s2.hasNext()) {
				text2 = text2 + s2.next();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int x_len = text1.length();
		int y_len = text2.length();
		L = new int[x_len + 1][y_len + 1];
		char[] X = text1.toCharArray();
		char[] Y = text2.toCharArray();
		for (int i = 0; i <= x_len; ++i) {
			L[i][0] = 0;
		}
		for (int j = 0; j <= y_len; ++j) {
			L[0][j] = 0;
		}
		/* Time complexity here is O(m*n) */
		for (int i = 1; i <= x_len; ++i) {
			for (int j = 1; j <= y_len; ++j) {
				if (X[i - 1] == Y[j - 1]) {
					L[i][j] = L[i - 1][j - 1] + 1;
				} else {
					L[i][j] = Math.max(L[i - 1][j], L[i][j - 1]);
				}
			}
		}
		while (x_len > 0 && y_len > 0) {
			if (X[x_len - 1] == Y[y_len - 1]
					&& L[x_len][y_len] == L[x_len - 1][y_len - 1] + 1) {
				text3 = X[x_len - 1] + text3;
				x_len--;
				y_len--;
			} else {
				@SuppressWarnings("unused")
				int m = (L[x_len - 1][y_len] > L[x_len][y_len - 1]) ? x_len--
						: y_len--;
			}
		}
		File writename = new File(f3);
		try {
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(text3);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (float) L[X.length][Y.length]
				/ Math.max(text1.length(), text2.length());
	}

	/**
	 * used for test
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		/**
		 * Construct a trie named trie1
		 */
		CompressedSuffixTrie trie1 = new CompressedSuffixTrie("file1");

		System.out.println("ACTTCGTAAG is at: "
				+ trie1.findString("ACTTCGTAAG"));

		System.out.println("AAAACAACTTCG is at: "
				+ trie1.findString("AAAACAACTTCG"));

		System.out.println("ACTTCGTAAGGTT : "
				+ trie1.findString("ACTTCGTAAGGTT"));

		System.out.println(CompressedSuffixTrie.similarityAnalyser("file2",
				"file3", "file4"));
	}
}

/**
 * This class is used to construct a tree node.
 *
 */
class SuffixTrieNode {

	String label = null;
	int startPos = -1;
	int endPos = -1;
	List<SuffixTrieNode> children = null;
	SuffixTrieNode parent = null;

	/**
	 * This is the constructor of tree node class.
	 * 
	 * @param parent
	 * @param label
	 * @param pos
	 */
	public SuffixTrieNode(SuffixTrieNode parent, String label, int pos) {
		children = new ArrayList<SuffixTrieNode>();
		this.label = label;
		startPos = pos;
		endPos = pos;
		this.parent = parent;
	}

	/**
	 * This default constructor is for tree root.
	 */
	public SuffixTrieNode() {
		children = new ArrayList<SuffixTrieNode>();
		label = "";
	}

	/**
	 * This method is used to add a new node after an existing node. The worst
	 * case of this method is the first insertion when the full-long string
	 * comes and there is no other node in the tree for comparing. In this
	 * situation the time complexity is O(|m|), where |m| is the length of the
	 * suffix-list.
	 * 
	 * @param suffix
	 * @param T
	 */
	public void concatenate(List<Integer> suffix, char[] T) {
		SuffixTrieNode currentNode = this;
		currentNode = compare(this, suffix, T);
		/*
		 * connect nodes after a insert point which is the parent of the first
		 * node whose element is different form that at the top of the suffix
		 * list.
		 */
		for (int x : suffix) {
			SuffixTrieNode child = new SuffixTrieNode(currentNode, T[x] + "", x);
			currentNode.children.add(child);
			currentNode = child;
		}
	}

	/**
	 * This method is used to search which node is the start node for insertion.
	 * The time complexity of this method depends on how fast it finds the
	 * different letter between suffix-list and the tree nodes. The worst case
	 * is O(|m|), where |m| is the length of the suffix-list.
	 * 
	 * @param startNode
	 * @param suffix
	 * @param T
	 * @return
	 */
	private SuffixTrieNode compare(SuffixTrieNode node, List<Integer> suffix,
			char[] T) {
		if (suffix.isEmpty()) {
			throw new IllegalArgumentException("Empty suffix.");
		}
		for (SuffixTrieNode child : node.children) {
			if (child.label.equals(T[suffix.get(0)] + "")) {
				suffix.remove(0);
				if (suffix.isEmpty()) {
					return child;
				}
				/*
				 * if the element of current node's child is equal to the first
				 * letter in the list to be inserted, set that node as the start
				 * node then again call the search method starting from it.
				 */
				return compare(child, suffix, T);
			}
		}
		return node;
	}
}