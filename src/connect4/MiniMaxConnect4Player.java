/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;

import game.*;

public class MiniMaxConnect4Player extends BaseConnect4Player {
	public final int MAX_DEPTH = 50;
	public int depthLimit;

	// mvStack is where the search procedure places it's move recommendation.
	// If the search is at depth, d, the move is stored on mvStack[d].
	// This was done to help efficiency (i.e., reduce number constructor calls)
	// (Not sure how much it improves things.)
	protected ScoredConnect4Move[] mvStack;

	// A Connect4Move with a scored (how well it evaluates)
	protected class ScoredConnect4Move extends Connect4Move {
		public ScoredConnect4Move(int c, double s) {
			super(c);
			score = s;
		}

		public void set(int c, double s) {
			col = c;
			score = s;
		}

		public double score;
	}

	public MiniMaxConnect4Player(String nname, int d) {
		super(nname);
		depthLimit = d;
	}

	protected static void shuffle(int[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			int spot = Util.randInt(i, len - 1);
			int tmp = ary[i];
			ary[i] = ary[spot];
			ary[spot] = tmp;
		}
	}

	/**
	 * Initializes the stack of Moves.
	 */
	public void init() {
		mvStack = new ScoredConnect4Move[MAX_DEPTH];
		for (int i = 0; i < MAX_DEPTH; i++) {
			mvStack[i] = new ScoredConnect4Move(0, 0);
		}
	}

	/**
	 * Determines if a board represents a completed game. If it is, the
	 * evaluation values for these boards is recorded (i.e., 0 for a draw +X,
	 * for a HOME win and -X for an AWAY win.
	 * 
	 * @param brd
	 *            Connect4 board to be examined
	 * @param mv
	 *            where to place the score information; column is irrelevant
	 * @return true if the brd is a terminal state
	 */
	protected boolean terminalValue(GameState brd, ScoredConnect4Move mv) {
		GameState.Status status = brd.getStatus();
		boolean isTerminal = true;

		if (status == GameState.Status.HOME_WIN) {
			mv.set(0, MAX_SCORE);
		} else if (status == GameState.Status.AWAY_WIN) {
			mv.set(0, -MAX_SCORE);
		} else if (status == GameState.Status.DRAW) {
			mv.set(0, 0);
		} else {
			isTerminal = false;
		}
		return isTerminal;
	}

	/**
	 * Performs the a depth limited minimax algorithm. It leaves it's move
	 * recommendation at mvStack[currDepth].
	 * 
	 * @param brd
	 *            current board state
	 * @param currDepth
	 *            current depth in the search
	 */
	private void minimax(Connect4State brd, int currDepth) {
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean isTerminal = terminalValue(brd, mvStack[currDepth]);

		if (isTerminal) {
			;
		} else if (currDepth == depthLimit) {
			mvStack[currDepth].set(0, evalBoard(brd));
		} else {
			ScoredConnect4Move tempMv = new ScoredConnect4Move(0, 0);

			double bestScore = (brd.getWho() == GameState.Who.HOME ? Double.NEGATIVE_INFINITY
					: Double.POSITIVE_INFINITY);
			ScoredConnect4Move bestMove = mvStack[currDepth];
			ScoredConnect4Move nextMove = mvStack[currDepth + 1];

			bestMove.set(0, bestScore);
			GameState.Who currTurn = brd.getWho();

			int[] columns = new int[COLS];
			for (int j = 0; j < COLS; j++) {
				columns[j] = j;
			}
			shuffle(columns);
			for (int i = 0; i < Connect4State.NUM_COLS; i++) {
				int c = columns[i];
				if (brd.numInCol[c] < Connect4State.NUM_ROWS) {
					// Make move on board
					tempMv.col = c;
					brd.makeMove(tempMv);

					// Check out worth of this move
					minimax(brd, currDepth + 1);

					// Undo the move
					brd.numInCol[c]--;
					int row = brd.numInCol[c];
					brd.board[row][c] = Connect4State.emptySym;
					brd.numMoves--;
					brd.status = GameState.Status.GAME_ON;
					brd.who = currTurn;

					// Check out the results, relative to what we've seen before
					if (toMaximize && nextMove.score > bestMove.score) {
						bestMove.set(c, nextMove.score);
					} else if (!toMaximize && nextMove.score < bestMove.score) {
						bestMove.set(c, nextMove.score);
					}
				}
			}
		}
	}

	public GameMove getMove(GameState brd, String lastMove) {
		minimax((Connect4State) brd, 0);
		return mvStack[0];
	}

	public static void main(String[] args) {
		int depth = 6;
		GamePlayer p = new MiniMaxConnect4Player("C4 MM F1 " + depth, depth);
		p.compete(args);
	}
}
