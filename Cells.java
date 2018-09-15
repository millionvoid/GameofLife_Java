import java.util.Random;

public class Cells {
	// data
	public int row;
	public int column;
	public boolean board[][];
	
	private static float thres = (float) 0.6;
	
	// interface
	public Cells(int rows, int columns) {
		row = rows;
		column = columns;
		board = new boolean[rows+2][columns+2];
		//randomize
		Random gen = new Random();
		for(int r = 1; r <= row; r ++) { 
			for(int c = 1; c <= column; c ++) {
				float rand = gen.nextFloat();
				if(rand > thres) {
					board[r][c] = true;
				} else {
					board[r][c] = false;
				}
			}
		}
	}
	
	
	public void evaluate() {
		boolean new_board[][] = new boolean[row+2][column+2];
		for(int r = 1; r <= row; r ++) {
			for(int c = 1; c <= column; c ++) {
				int surround = 0;
				for(int i = -1; i <= 1; i ++) {
					for(int j = -1; j <= 1; j ++) {
						if(board[i+r][j+c]) {
							surround ++;
						}
					}
				}

				new_board[r][c] = (surround == 3||(surround==4&&board[r][c]));
			}
		}
		board = new_board;
	}
	
	public String toString() {
		String ans = "";
		for(int r = 1; r <= row; r ++) {
			for(int c = 1; c <= column; c ++) {
				ans += (board[r][c]==true)?"1 ":"0 ";
			}
			ans += "\n";
		}
		return ans;
	}
	
	public boolean getAt(int r, int c) {
		return board[r+1][c+1];
	}
	
	public static void main(String[] argv) {
		Cells cells = new Cells(5, 5);
		for(int i = 0; i < 10; i ++) {
			System.out.println(cells);
			cells.evaluate();
		}
	}
}
