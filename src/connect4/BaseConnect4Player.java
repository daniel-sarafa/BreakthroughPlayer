/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;
import game.*;

public abstract class BaseConnect4Player extends GamePlayer {
	public static int ROWS = Connect4State.NUM_ROWS;
	public static int COLS = Connect4State.NUM_COLS;
	public static final int MAX_SCORE = 12*Connect4State.NUM_SPOTS + 1;
	public BaseConnect4Player(String nname)
	{ super(nname, "Connect4");	}
	private static int possible(Connect4State brd, char who, int r, int c, int dr, int dc)
	{
		int cnt = 0;
		for (int i=0; i<4; i++) {
			int row = r + dr * i;
			int col = c + dc * i;
			if (!Util.inrange(row, 0, ROWS-1) || !Util.inrange(col, 0, COLS-1)) {
				return 0;
			} else if (brd.board[row][col] == who) {
				cnt++;
			} else if (brd.board[row][col] == Connect4State.emptySym) {
				;
			} else {			// opposing player in the region
				return 0;
			}
		}
		return cnt;
	}
	/**
	 * Counts the number of adjacent pairs of spots with same
	 * player's piece. 
	 * @param brd board to be evaluated
	 * @param who 'R' or 'B'
	 * @return number of adjacent pairs equal to who
	 */
	private static int eval(Connect4State brd, char who)
	{
		int cnt = 0;
		for (int r=0; r<ROWS; r++) {
			for (int c=0; c<COLS; c++) {
				cnt += possible(brd, who, r, c, 1, 0);
				cnt += possible(brd, who, r, c, 0, 1);
				cnt += possible(brd, who, r, c, 1, 1);
				cnt += possible(brd, who, r, c, -1, 1);
			}
		}
		return cnt;
	}
	/**
	 * The evaluation function
	 * @param brd board to be evaluated
	 * @return Black evaluation - Red evaluation
	 */
	public static int evalBoard(Connect4State brd)
	{ 
		int score = eval(brd, Connect4State.homeSym) - eval(brd, Connect4State.awaySym);
		if (Math.abs(score) > MAX_SCORE) {
			System.err.println("Problem with eval");
			System.exit(0);
		}
		return score;
	}

		
	private static int countNeighbors(Connect4State brd, int r, int c, char what)
	{
		int cnt = 0;
		if (r == 0) {
			if (c < COLS - 1) {
				cnt += brd.board[r+1][c] == what ? 1 : 0; 
				cnt += brd.board[r+1][c+1] == what ? 1 : 0; 
				cnt += brd.board[r][c+1] == what ? 1 : 0; 
			} else {
				cnt += brd.board[r+1][c] == what ? 1 : 0; 
			}
		} else if (r == ROWS-1) {
			if (c < COLS - 1) {
				cnt += brd.board[r][c+1] == what ? 1 : 0; 
				cnt += brd.board[r-1][c+1] == what ? 1 : 0; 
			} else {
				; 
			}
		} else {
			if (c < COLS - 1) {
				cnt += brd.board[r+1][c] == what ? 1 : 0; 
				cnt += brd.board[r+1][c+1] == what ? 1 : 0; 
				cnt += brd.board[r][c+1] == what ? 1 : 0; 
				cnt += brd.board[r-1][c+1] == what ? 1 : 0; 
			} else {
				cnt += brd.board[r+1][c] == what ? 1 : 0; 
			}
		}
		return cnt;
	}
	/**
	 * Counts the number of adjacent pairs of spots with same
	 * player's piece. 
	 * @param brd board to be evaluated
	 * @param who 'R' or 'B'
	 * @return number of adjacent pairs equal to who
	 */
	private static int eval2(Connect4State brd, char who)
	{
		int cnt = 0;
		for (int r=0; r<ROWS; r++) {
			for (int c=0; c<COLS; c++) {
				if (brd.board[r][c] == who) {
					cnt += countNeighbors(brd, r, c, who);
				}
			}
		}
		return cnt;
	}
	/**
	 * The evaluation function
	 * @param brd board to be evaluated
	 * @return Black evaluation - Red evaluation
	 */
	public static int evalBoard2(Connect4State brd)
	{ return eval2(brd, Connect4State.homeSym) - eval2(brd, Connect4State.awaySym); } 
	
}
