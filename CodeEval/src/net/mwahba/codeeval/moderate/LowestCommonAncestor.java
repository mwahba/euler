package net.mwahba.codeeval.moderate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.mwahba.library.BinaryTree;

public class LowestCommonAncestor {
	
	public static void main(String[] args) throws IOException {
		/* Creating the following tree:
		 		30
			    |
			  ____
			  |   |
			  8   52
			  |
			____
			|   |
			3  20
			    |
			   ____
			  |   |
			  10 29
		 * 
		 */
		BinaryTree<Integer> tree = new BinaryTree<Integer>(30, 
				new BinaryTree<Integer>(8, 
						new BinaryTree<Integer>(3), new BinaryTree<Integer>(20, 
								new BinaryTree<Integer>(10), new BinaryTree<Integer>(29))), 
				new BinaryTree<Integer>(52));
		tree.setLevels();
		
		File file = new File(args[0]);
        BufferedReader buffer = new BufferedReader(new FileReader(file));
        String line;
        
        while ((line = buffer.readLine()) != null) {
            String[] stringNums = line.split(" ");
            Integer firstNum = Integer.parseInt(stringNums[0]), secondNum = Integer.parseInt(stringNums[1]);
            
            if (tree.lookup(firstNum) > 0 && tree.lookup(secondNum) > 0) { // if the numbers are in the tree
            	System.out.println(getLowestCommonAncestor(firstNum, secondNum, tree));
            } else {
            	System.out.println();
            }
        }
        
        buffer.close();
		
	}

	private static int getLowestCommonAncestor(int firstNum,
			int secondNum, BinaryTree<Integer> tree) {
		
		int firstTraverse = tree.lookup(firstNum), secondTraverse = tree.lookup(secondNum);
		if (firstTraverse == secondTraverse) {
			if (firstTraverse == 2) {
				getLowestCommonAncestor(firstNum, secondNum, tree.traverseLeft());
			} else if (firstTraverse == 3) {
				getLowestCommonAncestor(firstNum, secondNum, tree.traverseRight());
			} else if (firstTraverse == 1) {
				return tree.getRoot();
			}
		} else if (firstTraverse != secondTraverse) {
			if (firstTraverse == 2 && secondTraverse == 3) {
				return tree.getRoot();
			} else {
				return -1;
			}
		}
		
		return -1;
	}
}