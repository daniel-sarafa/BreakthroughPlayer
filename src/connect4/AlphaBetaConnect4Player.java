/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;
import game.*;


// AlphaBetaConnect4Player is identical to MiniMaxConnect4Player
// except for the search process, which uses alpha beta pruning.

public class AlphaBetaConnect4Player extends MiniMaxConnect4Player {
	public AlphaBetaConnect4Player(String nname, int d)
	{ super(nname, d); }

	/**
	 * Performs alpha beta pruning.
	 * @param brd
	 * @param currDepth
	 * @param alpha
	 * @param beta
	 */
	private void alphaBeta(Connect4State brd, int currDepth,
										double alpha, double beta)
	{
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean toMinimize = !toMaximize;

		boolean isTerminal = terminalValue(brd, mvStack[currDepth]);
		
		if (isTerminal) {
			;
		} else if (currDepth == depthLimit) {
			mvStack[currDepth].set(0, evalBoard(brd));
		} else {
			ScoredConnect4Move tempMv = new ScoredConnect4Move(0, 0);

			double bestScore = (toMaximize ? 
					Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
			ScoredConnect4Move bestMove = mvStack[currDepth];
			ScoredConnect4Move nextMove = mvStack[currDepth+1];

			bestMove.set(0, bestScore);
			GameState.Who currTurn = brd.getWho();

			int [] columns = new int [COLS];
			for (int j=0; j<COLS; j++) {
				columns[j] = j;
			}
			shuffle(columns);
			for (int i=0; i<Connect4State.NUM_COLS; i++) {
				int c = columns[i];
				if (brd.numInCol[c] < Connect4State.NUM_ROWS) {
					tempMv.col = c;				// initialize move
					brd.makeMove(tempMv);
		
					alphaBeta(brd, currDepth+1, alpha, beta);  // Check out move
					
					// Undo move
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

					// Update alpha and beta. Perform pruning, if possible.
					if (toMinimize) {
						beta = Math.min(bestMove.score, beta);
						if (bestMove.score <= alpha || bestMove.score == -MAX_SCORE) {
							return;
						}
					} else {
						alpha = Math.max(bestMove.score, alpha);
						if (bestMove.score >= beta || bestMove.score == MAX_SCORE) {
							return;
						}
					}
				}
			}
		}
	}
		
	public GameMove getMove(GameState brd, String lastMove)
	{ 
		alphaBeta((Connect4State)brd, 0, Double.NEGATIVE_INFINITY, 
										 Double.POSITIVE_INFINITY);
		System.out.println(mvStack[0].score);
		return mvStack[0];
	}
	
	public static char [] toChars(String x)
	{
		char [] res = new char [x.length()];
		for (int i=0; i<x.length(); i++)
			res[i] = x.charAt(i);
		return res;
	}
	
	public static void main(String [] args)
	{
		int depth = 8;
		GamePlayer p = new AlphaBetaConnect4Player("C4 A-B F1 " + depth, depth);
//		p.compete(args);

		p.init();
		String brd = "...BRR."+
					 "....B.."+
					 "......."+
					 "......."+
					 "......."+
					 "......."+
					 "[HOME 4 GAME_ON]";
		
		Connect4State state = new Connect4State();
		state.parseMsgString(brd);
		GameMove mv = p.getMove(state, "");
		System.out.println("Original board");
		System.out.println(state.toString());
		System.out.println("Move: " + mv.toString());
		System.out.println("Board after move");
		state.makeMove(mv);
		System.out.println(state.toString());
	}
}
