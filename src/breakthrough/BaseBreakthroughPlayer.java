package breakthrough;
import game.*;
import game.GameState.Status;

public abstract class BaseBreakthroughPlayer extends GamePlayer {
	
	public static final int MAX_SCORE = Integer.MAX_VALUE;
	public static final int MIN_SCORE = Integer.MIN_VALUE;
	
	protected class ScoredBreakthroughMove extends BreakthroughMove {
		public double score;
		
		public ScoredBreakthroughMove(int r1, int c1, int r2, int c2, double s) {
			super(r1, c1, r2, c2);
			score = s;
		}

		public void set(int r1, int c1, int r2, int c2, double s) {
			startRow = r1;
			startCol = c1;
			endingRow = r2;
			endingCol = c2;
			score = s;
		}
		
		public void set(BreakthroughMove m, double s) {
			startRow = m.startRow;
			startCol = m.startCol;
			endingRow = m.endingRow;
			endingCol = m.endingCol;
			score = s;
		}
	}
	
	public BaseBreakthroughPlayer(String name){
		super(name, "Breakthrough");
	}
	
	//eval function. keeps track of difference in pieces, if attacks are possible, and 
	//the sum of the distances away from a win
		protected static double eval(BreakthroughState board, char who) {
			int awayPieces = 0, homePieces = 0;
			int attacksAway = 0, attacksHome = 0;
			int distancesFromWinAway = 0, distancesFromWinHome = 0;
			
			for (int row = 0; row < BreakthroughState.N; row++) {
				for (int column = 0; column < BreakthroughState.N; column++) {
					char player = board.board[row][column];
					if (player == BreakthroughState.awaySym) {
						if (column != 0) {
							if (attackPossible(board, who, row-1, column-1))
								attacksAway++;
						}
						if (column != (BreakthroughState.N - 1)) {
							if (attackPossible(board, who, row-1, column+1))
								attacksAway++;
						}
						awayPieces++;
						distancesFromWinAway += (BreakthroughState.N - 1 - row);
					} else if(player == BreakthroughState.homeSym) {
						if (column != 0) {
							if (attackPossible(board, who, row+1, column-1))
								attacksHome++;
						}
						if (column != (BreakthroughState.N - 1)) {
							if (attackPossible(board, who, row+1, column+1))
								attacksHome++;
						}
						homePieces++;
						distancesFromWinHome += row;
					}
				}
			}
			if((board.status == Status.HOME_WIN && who == BreakthroughState.homeSym)
					|| board.status == Status.AWAY_WIN && who == BreakthroughState.awaySym){
				return MAX_SCORE;
			}
			else if((board.status == Status.HOME_WIN && who == BreakthroughState.awaySym)
					|| board.status == Status.AWAY_WIN && who == BreakthroughState.homeSym){
				return MIN_SCORE;
			}
			return .52*(homePieces - awayPieces) - .35*(attacksHome - attacksAway)  + .13*(distancesFromWinHome - distancesFromWinAway);
	}
	
		//checks if attack possible
	public static boolean attackPossible(BreakthroughState state, char player, int i, int j) {
			char opposition = player == BreakthroughState.homeSym ? BreakthroughState.awaySym : BreakthroughState.homeSym;
			boolean attack = false;
			if (state.board[i][j] == player) {
				if (state.board[i][j] == opposition) {
					attack = true;
				}
			}
			return attack;
		}
}