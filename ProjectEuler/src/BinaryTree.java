/**
 * Binary Class to create a binary tree given an array of arrays.
 * Uses the first array as the root of the tree.
 * Should have methods for traversing the trees.
 * @author wahbam2
 *
 */
public class BinaryTree {
	
	BinaryTree left;
	BinaryTree right;
	int current;
	int max = 0;
	
	public void setMax(int choice) {
		max = choice;
	}
	
	public BinaryTree max() {
		if (max == 1) {
			return left;
		} else {
			return right;
		}
	}
	
	public boolean hasRight() {
		if (right != null) return true;
		return false;
	}
	
	public boolean hasLeft() {
		if (left != null) return true;
		return false;
	}
	
	public BinaryTree traverseRight() {
		return right;
	}
	
	public BinaryTree traverseLeft() {
		return left;
	}
	
	public int rootValue() {
		return current;
	}
	
	public int length() {
		int total = 1;
		if (left != null) {
			total += left.length();
		}
		if (right != null) {
			total += right.length();
		}
		
		return total;
	}
	
	public void printMaxPath() {
		System.out.print(current);
		
		if (hasLeft()) {
			System.out.print(" --> ");
			if (max == 1) {
				left.printMaxPath();
			} else if (max == 2) {
				right.printMaxPath();
			} else {
				System.out.println("Error on node, max not set.");
			}
		}
	}
	
	public BinaryTree(int[][] source, int currentLevel, int currentPos) {
		current = source[currentLevel][currentPos];
		if (currentLevel < (source.length - 1)) {
			left = new BinaryTree(source, currentLevel + 1, currentPos);
			right = new BinaryTree(source, currentLevel + 1, currentPos + 1);
		}
	}
	
	public BinaryTree(int[][] source) {
		this(source, 0, 0);
	}

}
