package com.laboon;

public class Cell {
	
	private State _state = State.DEAD;
	
	private int _xLoc = 0;
	private int _yLoc = 0;
	
	/** 
	 * Public getter for the cell
	 * @return Current state of the cell
	 */
	
	public State state() {
		return _state;
	}
	
	/**
	 * Returns whether or not the cell is alive.
	 * @return True if cell alive, false if dead
	 */
	
	public boolean isAlive() {
		if (_state == State.ALIVE) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Any live cell with fewer than two live neighbors dies, as if caused by under-population.
	 * Any live cell with two or three live neighbors lives on to the next generation.
	 * Any live cell with more than three live neighbors dies, as if by overcrowding.
	 * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
	 * @param int numNeighbors - the current number of neighbors
	 * @return State - the new state of the cell (alive or dead)
	 */
	
	public State iterate(int numNeighbors) {
		State toReturn = null;
		if (_state == State.ALIVE) {
			if (numNeighbors < 2 || numNeighbors > 3) {
				toReturn = State.DEAD;
			} else {
				toReturn = State.ALIVE;
			}
		} else if (_state == State.DEAD) {
			if (numNeighbors == 3) {
				toReturn = State.ALIVE;
			} else {
				toReturn = State.DEAD;
			}
		}
		return toReturn;
	}
	
	/**
	 * Get the character to display for the current state.
	 * Modifying this code will allow you to change the terminal display
	 * of when a cell is alive or dead.
	 * @return Character representation of the cell's state
	 */
	
	public char getStateRep() {
		char toReturn = ' ';
		if (_state == State.DEAD) {
			toReturn = '.';
		} else if (_state == State.ALIVE) {
			toReturn = 'X';
		} else {
			toReturn = '?';
		}
		return toReturn;
	}
	
	/**
	 * Constructor for a new cell.
	 * @param initialState - Initial state of a cell, dead or alive
	 * @param xLoc - x location
	 * @param yLoc - y location
	 */
	
	public Cell(State initialState, int xLoc, int yLoc) {
		_state = initialState;
		_xLoc = xLoc;
		_yLoc = yLoc;
	}
	
}
