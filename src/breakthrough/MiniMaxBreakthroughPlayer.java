package breakthrough;

import java.util.ArrayList;
import game.*;

public class MiniMaxBreakthroughPlayer extends BaseBreakthroughPlayer {
	public int depthLim = 5;
	public final int MAX_DEPTH = 50;
	protected ScoredBreakthroughMove[] moveStack;

    public MiniMaxBreakthroughPlayer(String name) {
        super(name);
    }
    
    public void init(){
    	moveStack = new ScoredBreakthroughMove[MAX_DEPTH];
    	for(int i = 0; i < MAX_DEPTH; i++){
    		moveStack[i] = new ScoredBreakthroughMove(0, 0, 0, 0, 0);
    	}
    }
    
    /**
     * Checks to see if a move is terminal (win/loss state)
     * @param board
     * @param move
     * @return whether the move is terminal
     */
    protected static boolean terminalValue(GameState board, ScoredBreakthroughMove move, int depth, int limit) {
        GameState.Status status = board.getStatus();
        boolean isTerm = true;

        // Check to see if the home team won
        if (status == GameState.Status.HOME_WIN) {
        	move.set(0, 0, 0, 0, MAX_SCORE);
        }
        // Check to see if the away team won
        else if (status == GameState.Status.AWAY_WIN) {
            move.set(0, 0, 0, 0, MIN_SCORE);
        } else {
            isTerm = false;
        }
        return isTerm;
    }

    /**
     * Returns a list of all possible moves for ONLY the current player.
     * @param state of the BreakthroughGame
     * @return a list of ScoredBreakThrough moves
     */
    protected ArrayList<BreakthroughMove> getMoves(BreakthroughState board, char who) {
    	ArrayList<BreakthroughMove> moves = new ArrayList<BreakthroughMove>();
		for(int i = 0; i < BreakthroughState.N; i++){
			for(int j = 0; j < BreakthroughState.N; j++){
				if(who == BreakthroughState.homeSym){
					if(moveAOK(board, who, i, j, i+1, j-1)) 
						moves.add(new BreakthroughMove(i,j,i+1,j-1));
					if(moveAOK(board, who, i, j, i+1, j)) 
						moves.add(new BreakthroughMove(i,j,i+1,j));
					if(moveAOK(board, who, i, j, i+1, j+1)) 
						moves.add(new BreakthroughMove(i,j, i+1, j+1));
				}
				else{
					if(moveAOK(board, who, i, j, i-1, j-1)) 
						moves.add(new BreakthroughMove(i, j, i-1, j-1));
					if(moveAOK(board, who, i, j, i-1, j)) 
						moves.add(new BreakthroughMove(i, j, i-1, j));
					if(moveAOK(board, who, i, j, i-1, j+1)) 
						moves.add(new BreakthroughMove(i, j, i-1, j+1));	
				}
			}
		}
		return moves;
    }
    
    //checks if moves are ok, along with if its in index
    public boolean moveAOK(BreakthroughState board, char who, int r1, int c1, int r2, int c2){
    	if(r1 < 0 || c1 < 0 || r2 < 0 || c2 < 0 || 
    			r1 >= BreakthroughState.N || c1 >= BreakthroughState.N || r2 >= BreakthroughState.N || c2 >= BreakthroughState.N) 
    		return false;
    	BreakthroughMove mv = new BreakthroughMove(r1, c1, r2, c2);
		if(board.moveOK(mv)){
			return true;
		}
		return false;
	}
    
    //returns a cloned state after a move is made
    public BreakthroughState makeNewMove(BreakthroughState state, BreakthroughMove move){
    	BreakthroughState clone = state;
    	clone.makeMove(move);
    	return clone;
    }
    
    /**
     * Play the breakthrough game using the minimax player
     * @param board of game to be played
     * @param currentDepth of minimax algo
     * @param depthLimit
     * @param alpha value
     * @param beta value
     */
    protected void minimax(BreakthroughState board, int currentDepth, int depthLimit, double alpha, double beta) {
    	boolean maximizer = (board.getWho() == GameState.Who.HOME);	
		boolean minimizer = !maximizer;
		boolean isTerm = terminalValue(board, moveStack[currentDepth], currentDepth, depthLimit);
		char who = (board.getWho() == GameState.Who.HOME ? BreakthroughState.homeSym : BreakthroughState.awaySym);
		if(isTerm) {
			;
		} 
		else if(currentDepth == depthLim) {
			 moveStack[currentDepth].set(0,0,0,0, eval(board, who));
		} 
		else {
			double bestScore = (board.getWho() == GameState.Who.HOME ? MIN_SCORE: MAX_SCORE);
			ArrayList<BreakthroughMove> moves = getMoves(board, who);
			shuffle(moves);
			ScoredBreakthroughMove bestMove = moveStack[currentDepth];
			ScoredBreakthroughMove nextMove = moveStack[currentDepth+1];
			bestMove.set(moves.get(0), bestScore);
			for(BreakthroughMove mv : moves) {
				minimax(makeNewMove((BreakthroughState)board.clone(), mv), currentDepth + 1, depthLimit, alpha, beta);
				if(maximizer && nextMove.score > bestMove.score) {
					bestMove.set(mv, nextMove.score);
				}else if(!maximizer && nextMove.score < bestMove.score) {
					bestMove.set(mv, nextMove.score);
				}
				if(minimizer) {
					beta = Math.min(bestMove.score, beta);
					if (bestMove.score <= alpha || bestMove.score == MIN_SCORE) {
						return;
					}
				} else {
					alpha = Math.max(bestMove.score, alpha);
					if (bestMove.score >= beta || bestMove.score == MAX_SCORE) {
						return;
					}
				}
				moveStack[currentDepth] = bestMove;
			}
		}
//      
    }
    
    //shuffles moves list
    public void shuffle(ArrayList<BreakthroughMove> list){
    	for(int i = 0; i < list.size(); i++){
    		int spot = Util.randInt(i,  list.size() - 1);
    		BreakthroughMove temp = list.get(i);
    		list.set(i, list.get(spot));
    		list.set(spot, temp);
    	}
    }

    @Override
    /**
     * Returns a BreakthroughMove after running MiniMax to determine the best one
     */
    public GameMove getMove(GameState board, String lastMv) {
    	minimax((BreakthroughState) board, 0, depthLim, MIN_SCORE, MAX_SCORE);
        return moveStack[0];
    }
    
    
    public static void main(String[] args) {
        GamePlayer p = new MiniMaxBreakthroughPlayer("Raptor");
        p.compete(args);
        p.solvePuzzles(new String [] {"BTPuzzle1", "BTPuzzle2"});
    }
}