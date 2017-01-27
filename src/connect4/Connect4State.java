/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;

import java.util.Arrays;
import game.*;

public class Connect4State extends GameState {
	public static final Params gameParams = new Params(CONFIG_DIR + "connect4.txt");
	public static final int NUM_ROWS = gameParams.integer("ROWS");
	public static final int NUM_COLS = gameParams.integer("COLS");
	public static final int NUM_SPOTS = NUM_ROWS * NUM_COLS;
	public static final char homeSym = gameParams.character("HOMESYM");
	public static final char awaySym = gameParams.character("AWAYSYM");
	public static final char emptySym = gameParams.character("EMPTYSYM");

	public char[][] board = new char[NUM_ROWS][NUM_COLS];
	public int[] numInCol = new int[NUM_COLS];

	public Connect4State() {
		reset();
	}

	public Object clone() {
		Connect4State copy = new Connect4State();
		copy.copyInfo(this);
		Util.copy(copy.board, board);
		for (int c = 0; c < NUM_COLS; c++) {
			copy.numInCol[c] = numInCol[c];
		}
		return copy;
	}

	public void reset() {
		clear();
		Util.clear(board, emptySym);
		Arrays.fill(numInCol, 0);
	}

	public boolean moveOK(GameMove mv) {
		Connect4Move c4mv = (Connect4Move) mv;
		return status == Status.GAME_ON && mv != null
				&& Util.inrange(c4mv.col, NUM_COLS - 1)
				&& Util.inrange(numInCol[c4mv.col], NUM_ROWS - 1);
	}

	private boolean sol(int r, int c, int dr, int dc) {
		int endr = r + 3 * dr;
		int endc = c + 3 * dc;
		if (Util.inrange(endr, NUM_ROWS - 1)
				&& Util.inrange(endc, NUM_COLS - 1) && board[r][c] != emptySym) {
			for (int i = 1; i < 4; i++) {
				if (board[r][c] != board[r + dr * i][c + dc * i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void computeStatus() {
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				if (sol(r, c, +1, 0) || sol(r, c, +1, +1) || sol(r, c, +1, -1)
						|| sol(r, c, 0, +1)) {
					if (board[r][c] == homeSym) {
						status = GameState.Status.HOME_WIN;
					} else {
						status = GameState.Status.AWAY_WIN;
					}
					return;
				}
			}
		}
		if (getNumMoves() == NUM_SPOTS) {
			status = GameState.Status.DRAW;
		} else {
			status = GameState.Status.GAME_ON;
		}
	}

	public boolean makeMove(GameMove mv) {
		Connect4Move move = (Connect4Move) mv;
		if (Util.inrange(move.col, NUM_COLS - 1)
				&& Util.inrange(numInCol[move.col], NUM_ROWS - 1)) {
			int row = numInCol[move.col]++;
			board[row][move.col] = (who == Who.HOME ? homeSym : awaySym);
			super.newMove();
			computeStatus();
			return true;
		} else {
			return false;
		}
	}

	public void parseMsgString(String s) {
		reset();
		Util.parseMsgString(s, board, emptySym);
		parseMsgSuffix(s.substring(s.indexOf('[')));
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				if (board[r][c] != emptySym) {
					numInCol[c]++;
				}
			}
		}
	}

	public String toString() {
		return Util.toString(board) + msgSuffix();
	}

	public String msgString() {
		return Util.msgString(board) + this.msgSuffix();
	}
}
