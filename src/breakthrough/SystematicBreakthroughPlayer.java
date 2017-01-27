/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package breakthrough;
import game.*;

public class SystematicBreakthroughPlayer extends GamePlayer {
	public static boolean isConservative;
	public SystematicBreakthroughPlayer(String n, boolean isConservative) 
	{
		super(n, "Breakthrough");
		SystematicBreakthroughPlayer.isConservative = isConservative;
	}
	public GameMove getMove(GameState state, String lastMove)
	{
		BreakthroughState board = (BreakthroughState)state;
		BreakthroughMove mv = new BreakthroughMove();
		int dir = state.who == GameState.Who.HOME ? +1 : -1;
		int startRow, endRow, inc;
		if (dir == +1) {
			if (isConservative) {
				startRow = 0;
				endRow = BreakthroughState.N;
				inc = 1;
			} else {
				startRow = BreakthroughState.N;
				endRow = -1;
				inc = -1;
			}
		} else {
			if (isConservative) {
				startRow = BreakthroughState.N;
				endRow = -1;
				inc = -1;
			} else {
				startRow = 0;
				endRow = BreakthroughState.N;
				inc = 1;
			}
		}
		for (int r=startRow; r!=endRow; r+=inc) {
			for (int c=0; c<BreakthroughState.N; c++) {
				mv.startRow = r;
				mv.startCol = c;
				mv.endingRow = r+dir; mv.endingCol = c;
				if (board.moveOK(mv)) {
					return mv;
				}
				mv.endingRow = r+dir; mv.endingCol = c+1;
				if (board.moveOK(mv)) {
					return mv;
				}
				mv.endingRow = r+dir; mv.endingCol = c-1;
				if (board.moveOK(mv)) {
					return mv;
				}
			}
		}
		return null;
	}
	public static void main(String [] args)
	{
		GamePlayer p = new SystematicBreakthroughPlayer("Systematic", false);
		p.compete(args);
	}
}
