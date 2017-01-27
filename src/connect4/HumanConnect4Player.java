/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;
import game.*;

public class HumanConnect4Player extends RandomConnect4Player {
	private GameFrame frame;
	private Connect4Move move = new Connect4Move(0);

	public HumanConnect4Player(String nname)
	{
		super(nname);
		frame = new GameFrame(nickname, new Connect4Canvas());
		frame.setVisible(true);
		gameState = new Connect4State();
	}
	public GameMove getMove(GameState game, String lastMove)
	{
		char ch = side == GameState.Who.HOME ? Connect4State.homeSym : Connect4State.awaySym;
		frame.setTitle("My move (" + ch + ")");
		if (!lastMove.equals("--") && frame.canvas.move != null) {
			((Connect4Move)frame.canvas.move).parseMove(lastMove);
		}
		frame.canvas.setBoard(game);
		frame.canvas.repaint();
		frame.canvas.getMove(move, game, this);
		try {
			frame.canvas.ready.acquire();
		}
		catch (Exception e) { 		}
		game.makeMove(move);
		frame.canvas.repaint();
		frame.setTitle("Waiting");
		return move;
	}
	public void endGame(int result)
	{
		char ch = side == GameState.Who.HOME ? Connect4State.homeSym : Connect4State.awaySym;
		if (result == 1) {
			frame.setTitle("Won (" + ch + ")");
		} else if (result == -1) {
			frame.setTitle("Loss (" + ch + ")");
		} else {
			frame.setTitle("Draw (" + ch + ")");
		}
	}
	public static void main(String[] args)
	{
		GamePlayer p = new HumanConnect4Player("HUMAN");	// must have this name
		p.compete(args);
	}

}
