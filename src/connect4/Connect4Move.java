/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;
import java.util.StringTokenizer;

import game.*;

public class Connect4Move extends GameMove {
	public int col;
	public static boolean indexOK(int v)
	{ return v >= 0 && v < Connect4State.NUM_COLS; }
	public String toString()
	{ return Integer.toString(col); } 
	public Object clone()
	{ return new Connect4Move(col); }
	public void parseMove(String s)
	{
		StringTokenizer toks = new StringTokenizer(s);
		col = Integer.parseInt(toks.nextToken());
	}
	public Connect4Move(int c)
	{
		col = c;
		if (!indexOK(c)) {
			System.err.println("problem");
		}
	}
	public Connect4Move()
	{
		this(0);
	}
}
