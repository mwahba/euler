
public class Problem28 {

	public static final int SIZE = 1001;
	public enum currDir {
		LEFT, DOWN, RIGHT, UP
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Setup the grid
		// can start doing it counter clockwise
		// or devise algorithm to do it somehow?
		currDir direction = currDir.LEFT;
		int currNum = (SIZE * SIZE) - 1, sum = SIZE * SIZE, lengthCounter = SIZE, currentCounter = lengthCounter-1;
		
		while (currNum > 1) {
			
			currentCounter--;
			
			if (currentCounter == 0) {
				switch (direction) {
					case LEFT:
						direction = currDir.DOWN;
						lengthCounter--;
						sum += currNum;
						break;
					case DOWN:
						direction = currDir.RIGHT;
						sum += currNum;
						break;
					case RIGHT:
						direction = currDir.UP;
						lengthCounter--;
						sum += currNum;
						break;
					case UP:
						direction = currDir.LEFT;
						sum += (currNum - 1);
						break;
				}
				
				currentCounter = lengthCounter;
			}
			
			//System.out.println(currNum + "\t" + direction.toString() + "\t" + currentCounter + "\t" + lengthCounter + "\t" + sum);
			
			currNum--;
		}
		
		System.out.println(sum);
	}

}
