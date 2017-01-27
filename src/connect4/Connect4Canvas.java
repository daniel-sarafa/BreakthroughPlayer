/* Copyright (C) Mike Zmuda - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Mike Zmuda <zmudam@miamioh.edu>, 2010-2015
 */

package connect4;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import game.*;

public class Connect4Canvas extends GameCanvas {
	public static final long serialVersionUID = 0;
	public static final int SQR_SZ = 26;
	public static final int LEFT = 20;
	public static final int TOP = 20;
	
	public int getH()
	{ return Connect4State.NUM_ROWS * SQR_SZ + 150; }
	public int getW()
	{ return Connect4State.NUM_ROWS * SQR_SZ + 150; }
	public void getMove(GameMove move, GameState state, Object waiting)
	{
		this.move = move;
		this.waiting = waiting;
		this.state = state;
		this.gettingMove = true;
	}
	public Connect4Canvas()
    { addMouseListener(this); }
    public void paint(Graphics g)
    {
    	Connect4State brd = (Connect4State)state;

    	for (int R=Connect4State.NUM_ROWS - 1; R>=0; R--) {
    		int r = Connect4State.NUM_ROWS - 1 - R; 
        	for (int c=0; c<Connect4State.NUM_COLS; c++) {
        		Connect4Move lastMove = (Connect4Move)move;
        		square(g, c, R);
        		if (brd.board[r][c] == 'B') {
        			Color color = Color.BLACK;
        			if (c == lastMove.col && (r == Connect4State.NUM_ROWS-1 || brd.board[r+1][c] == '.')) {
        				color = new Color(125, 125, 125);
        			}
        			circle(g, c, R, color);
        		} else if (brd.board[r][c] == 'R') {
        			Color color = Color.RED;
        			if (c == lastMove.col && (r == Connect4State.NUM_ROWS-1 || brd.board[r+1][c] == '.')) {
            			color = new Color(150, 0, 0);
            		}
        			circle(g, c, R, color);
        		}
        	}
    	}
    }
    public void mousePressed(MouseEvent mouseEvent) 
    { 
    	Connect4Move mv = (Connect4Move)move;
        int c = (mouseEvent.getX() - LEFT) / (SQR_SZ+2);
       	mv.col = c;
    	if (!state.moveOK(mv)) {
    		JOptionPane.showMessageDialog(null, "Bad move" + mv.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
       	ready.release();
    }
    private void square(Graphics g, int x, int y)
    {
    	int lx = x * (SQR_SZ + 2) + LEFT;
    	int ly = y * (SQR_SZ + 2) + TOP;
    	g.setColor(Color.DARK_GRAY);
    	g.drawRect(lx, ly, SQR_SZ+2, SQR_SZ+2);
    	g.setColor(Color.LIGHT_GRAY);
    	g.fillRect(lx+1, ly+1, SQR_SZ, SQR_SZ);
    }
    private void circle(Graphics g, int x, int y, Color color)
    {
    	int diam = (int)(SQR_SZ*0.8);
    	int ws = SQR_SZ - diam;
    	int lx = x * (SQR_SZ + 2) + LEFT;
    	int ly = y * (SQR_SZ + 2) + TOP;
    	g.setColor(color);
    	g.fillOval(lx+ws/2+1, ly+ws/2+1, diam, diam);
    }
}
