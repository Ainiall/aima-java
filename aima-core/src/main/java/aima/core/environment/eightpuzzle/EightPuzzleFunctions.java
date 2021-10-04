package aima.core.environment.eightpuzzle;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.util.datastructure.XYLocation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Useful functions for solving EightPuzzle problems.
 * 
 * @author Ruediger Lunde
 */
public class EightPuzzleFunctions {

	public static final EightPuzzleBoard GOAL_STATE = new EightPuzzleBoard(new int[] { 1, 2, 3, 8, 0, 4, 7, 6, 5 });

	public static List<Action> getActions(EightPuzzleBoard state) {
		return Stream.of(EightPuzzleBoard.UP, EightPuzzleBoard.DOWN, EightPuzzleBoard.LEFT, EightPuzzleBoard.RIGHT)
				.filter(state::canMoveGap).collect(Collectors.toList());
	}

	public static EightPuzzleBoard getResult(EightPuzzleBoard state, Action action) {
		EightPuzzleBoard result = state.clone();

		if (result.canMoveGap(action)) {
			if (action == EightPuzzleBoard.UP)
				result.moveGapUp();
			else if (action == EightPuzzleBoard.DOWN)
				result.moveGapDown();
			else if (action == EightPuzzleBoard.LEFT)
				result.moveGapLeft();
			else if (action == EightPuzzleBoard.RIGHT)
				result.moveGapRight();
		}
		return result;
	}

	public static double getManhattanDistance(Node<EightPuzzleBoard, Action> node) {
		EightPuzzleBoard currState = node.getState();
		int result = 0;
		for (int val = 1; val <= 8; val++) {
			XYLocation locCurr = currState.getLocationOf(val);
			XYLocation locGoal = GOAL_STATE.getLocationOf(val);
			result += Math.pow(2, val) * Math.abs(locGoal.getX() - locCurr.getX());
			result += Math.pow(2, val) * Math.abs(locGoal.getY() - locCurr.getY());
		}
		return result;
	}

	public static double getEpsilonWeightedManhattanDistance(Node<EightPuzzleBoard, Action> node) {
		double epsilon = 0.8;
		return ( 1+ epsilon) * getManhattanDistance(node);
	}

	public static int getNumberOfMisplacedTiles(Node<EightPuzzleBoard, Action> node) {
		EightPuzzleBoard currState = node.getState();
		int result = 0;
		for (int val = 1; val <= 8; val++)
			if (!(currState.getLocationOf(val).equals(GOAL_STATE.getLocationOf(val))))
				result += Math.pow(2, val);
		return result;
	}

	public static double stepCostFunction(EightPuzzleBoard s1, Action a, EightPuzzleBoard s2) {
		int val = 0;
		int i = 0;
		while (i < 8 && s2.getState()[i] != 0) {
			i++;
		}
		val = s1.getState()[i];
		return (long) Math.pow(2, val);
	}

	public static int getNullHeuristic(Node<EightPuzzleBoard, Action> node) {
		return 0;
	}

	public static int getConsistentHeuristic(Node<EightPuzzleBoard, Action> node) {
		EightPuzzleBoard currState = node.getState();
		int result = 0;
		for (int val = 1; val <= 8; val++) {
			XYLocation locCurr = currState.getLocationOf(val);
			XYLocation locGoal = GOAL_STATE.getLocationOf(val);
			int distance = Math.abs(locGoal.getX() - locCurr.getX())+
					Math.abs(locGoal.getY() - locCurr.getY());
			if (distance == 2)
				result += distance * Math.pow(2, val);
		}
		return result;
	}
}