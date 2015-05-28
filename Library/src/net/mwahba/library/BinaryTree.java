package net.mwahba.library;

public class BinaryTree<T> {

	private T root;
	private BinaryTree<T> parent = null;
	private BinaryTree<T> left = null;
	private BinaryTree<T> right = null;
	private int level = -1;
	
	public BinaryTree(T data) {
		this.root = data;
	}
	
	public BinaryTree(T data, BinaryTree<T> leftTree) {
		this.root = data;
		this.left = leftTree;
		this.left.setParent(this);
	}
	
	public BinaryTree(T data, BinaryTree<T> leftTree, BinaryTree<T> rightTree) {
		this.root = data;
		
		this.left = leftTree;
		this.left.setParent(this);
		
		this.right = rightTree;
		this.left.setParent(this);
	}
	
	/**
	 * Constructor with a custom child tree set and an option flag.
	 * @param data The root data for this tree.
	 * @param childTree The child tree element, already constructed.
	 * @param isLeftTree If this is the left tree, set to true, else set to false.
	 */
	public BinaryTree(T data, BinaryTree<T> childTree, boolean isLeftTree) {
		this.root = data;
		if (isLeftTree) {
			this.left = childTree;
			this.left.setParent(this);
		} else {
			this.right = childTree;
			this.right.setParent(this);
		}
	}
	
	public T getRoot () {
		return this.root;
	}
	
	public void setRootData (T data) {
		this.root = data;
	}
	
	public BinaryTree<T> traverseParent() {
		return this.parent;
	}
	
	public void setParent (BinaryTree<T> parent) {
		this.parent = parent;
	}
	
	public BinaryTree<T> traverseLeft() {
		return this.left;
	}
	
	public void setLeft(T data) {
		this.left = new BinaryTree<T>(data);
		this.left.setParent(this);
	}
	
	public boolean hasLeft() {
		return this.left != null;
	}
	
	public BinaryTree<T> traverseRight() {
		return this.right;
	}
	
	public void setRight(T data) {
		this.right = new BinaryTree<T>(data);
		this.right.setParent(this);
	}
	
	public boolean hasRight() {
		return this.right != null;
	}
	
	/**
	 * This is to identify which branch to traverse to find the data.
	 * @param data
	 * @return 0 when no data can be found
	 * 	1 if this is this current node is the data
	 * 	2 if the left node contains the data
	 * 	3 if the right node contains the data
	 * 	-1 otherwise
	 */
	public int lookup (T data) {
		if (this.root == null) {
			return 0;
		} else if (this.root == data) {
			return 1;
		} else if (this.left.lookup(data) > 1) {
			return 2;
		} else if (this.right.lookup(data) > 1) {
			return 3;
		} else {
			return -1;
		}
	}
	
	public BinaryTree<T> getTreeByNode(T data) {
		switch (lookup(data)) {
		case 1: // this current tree
			return this;
		case 2: // left tree
			return this.left.getTreeByNode(data);
		case 3: // right tree
			return this.right.getTreeByNode(data);
		}
		
		return null;
	}
	
	public void setLevels() {
		if (this.traverseParent() == null) { // this is the main root
			this.setLevel(0);
		} else {
			this.parent.setLevels();
		}
	}
	
	public void setLevel(int currentLevel) {
		this.level = currentLevel;
		
		if (this.left != null) {
			this.left.setLevel(currentLevel + 1);
		}
		
		if (this.right != null) {
			this.right.setLevel(currentLevel + 1);
		}
	}
	
	public int getLevel() {
		return new Integer(this.level);
	}
	
}
