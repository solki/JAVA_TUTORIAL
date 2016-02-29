import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CompressedSuffixTrie {
	String text = "";
	char[] T;
	SuffixTreeNode root = null;
	int inputAlphabetSize = -1;

	/** You need to define your data structures for the compressed trie */
	/** Constructor */
	public CompressedSuffixTrie(String f) // Create a compressed suffix trie
											// from file f
	{
		try {
			Scanner s = new Scanner(new FileInputStream(f));
			while (s.hasNext()) {
				text = text + s.next();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (text.length() == 0 || text.charAt(text.length() - 1) != '$') {
			text = text + "$";
		}
		T = new char[text.length()];
		T = text.toCharArray();
		constructTree();
		//System.out.println(T);
	}
	
	private void constructTree() {
		root = new SuffixTreeNode();
		//char[] s = text.toCharArray();
		for (int i = 0; i < T.length; i++) {
			List<String> suffixList = new ArrayList<String>();
			for (int k = i; k < T.length; k++) {
				//Character.toString(T[k]);
				suffixList.add(Character.toString(T[k]));
			}
			root.addSuffix(suffixList, i + 1);
		}
		root = compactNodes(root, 0);
	}
	
	private SuffixTreeNode compactNodes(SuffixTreeNode node, int nodeDepth) {
		node.nodeDepth = nodeDepth;
		for (SuffixTreeNode child : node.children) {
			while (child.children.size() == 1) {
				SuffixTreeNode grandchild = child.children.iterator().next();
				child.incomingEdge.label += ", "
						+ grandchild.incomingEdge.label;
				child.stringDepth += grandchild.incomingEdge.label.length();
				child.children = grandchild.children;
				for (SuffixTreeNode grandchild1 : child.children)
					grandchild1.parent = node;
			}
			child = compactNodes(child, nodeDepth + 1);
		}
		return node;
	}

	/**
	 * Method for finding the first occurrence of a pattern s in the DNA
	 * sequence
	 */
	public int findString(String s) {
		int patLength = s.length();
		int j = 0;

		return 0;
	}

	/**
	 * Method for computing the degree of similarity of two DNA sequences stored
	 * in the text files f1 and f2
	 */
	public static float similarityAnalyser(String f1, String f2, String f3) {
		return 0;
	}

	public static void main(String args[]) throws Exception {

		/**
		 * Construct a trie named trie1
		 */
		CompressedSuffixTrie trie1 = new CompressedSuffixTrie("file4");

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

class SuffixTreeNode {

	SuffixTreeEdge incomingEdge = null;
	int nodeDepth = -1;
	int label = -1;
	Collection<SuffixTreeNode> children = null;
	SuffixTreeNode parent = null;
	int stringDepth;
	int id = 0;
	public static int c;

	public SuffixTreeNode(SuffixTreeNode parent, String incomingLabel,
			int depth, int label, int id) {
		children = new ArrayList<SuffixTreeNode>();
		incomingEdge = new SuffixTreeEdge(incomingLabel, label);
		nodeDepth = depth;
		this.label = label;
		this.parent = parent;
		stringDepth = parent.stringDepth + incomingLabel.length();
		this.id = id;
	}

	public SuffixTreeNode() {
		children = new ArrayList<SuffixTreeNode>();
		nodeDepth = 0;
		label = 0;
	}

	public void addSuffix(List<String> suffix, int pathIndex) {
		SuffixTreeNode insertAt = this;
		insertAt = search(this, suffix);
		insert(insertAt, suffix, pathIndex);
	}

	private SuffixTreeNode search(SuffixTreeNode startNode, List<String> suffix) {
		if (suffix.isEmpty()) {
			throw new IllegalArgumentException(
					"Empty suffix. Probably no valid simple suffix tree exists for the input.");
		}
		Collection<SuffixTreeNode> children = startNode.children;//
		for (SuffixTreeNode child : children) {
			if (child.incomingEdge.label.equals(suffix.get(0))) {
				suffix.remove(0);
				if (suffix.isEmpty()) {
					return child;
				}
				return search(child, suffix);
			}
		}
		return startNode;
	}

	private void insert(SuffixTreeNode insertAt, List<String> suffix,
			int pathIndex) {
		for (String x : suffix) {
			SuffixTreeNode child = new SuffixTreeNode(insertAt, x,
					insertAt.nodeDepth + 1, pathIndex, id);
			insertAt.children.add(child);
			insertAt = child;
		}
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		String incomingLabel = this.isRoot() ? "" : this.incomingEdge.label;
		for (int i = 1; i <= this.nodeDepth; i++)
			result.append("\t");
		if (this.isRoot()) {
			c = 1;
			this.id = 1;
		} else {
			this.id = c;
			result.append(this.parent.id + " -> ");
			result.append(this.id + "[label=\"" + incomingLabel + "\"];\n");
		}
		for (SuffixTreeNode child : children) {
			c++;
			child.id = c;
			result.append(child.toString());
		}
		return result.toString();
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}
}

class SuffixTreeEdge {

	String label = null;
	@SuppressWarnings("unused")
	private int branchIndex = -1;

	public SuffixTreeEdge(String label, int branchIndex) {
		this.label = label;
		this.branchIndex = branchIndex;
	}
}
