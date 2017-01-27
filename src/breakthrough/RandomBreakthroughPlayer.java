/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package breakthrough;
import game.*;
import java.util.*;

public class RandomBreakthroughPlayer extends GamePlayer {
	public RandomBreakthroughPlayer(String n) 
	{
		super(n, "Breakthrough");
	}
	public GameMove getMove(GameState state, String lastMove)
	{
		BreakthroughState board = (BreakthroughState)state;
		ArrayList<BreakthroughMove> list = new ArrayList<BreakthroughMove>();  
		BreakthroughMove mv = new BreakthroughMove();
		int dir = state.who == GameState.Who.HOME ? +1 : -1;
		for (int r=0; r<BreakthroughState.N; r++) {
			for (int c=0; c<BreakthroughState.N; c++) {
				mv.startRow = r;
				mv.startCol = c;
				mv.endingRow = r+dir; mv.endingCol = c;
				if (board.moveOK(mv)) {
					list.add((BreakthroughMove)mv.clone());
				}
				mv.endingRow = r+dir; mv.endingCol = c+1;
				if (board.moveOK(mv)) {
					list.add((BreakthroughMove)mv.clone());
				}
				mv.endingRow = r+dir; mv.endingCol = c-1;
				if (board.moveOK(mv)) {
					list.add((BreakthroughMove)mv.clone());
				}
			}
		}
		int which = Util.randInt(0, list.size()-1);
		return list.get(which);
	}
	public static void main(String [] args)
	{
		GamePlayer p = new RandomBreakthroughPlayer("Random BT+");
		p.compete(args);
		//p.solvePuzzles(new String [] {"BTPuzzle1", "BTPuzzle2"});
	}
}
