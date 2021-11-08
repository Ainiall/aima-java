package aima.core.environment.nqueens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;
import aima.core.util.datastructure.XYLocation;

/**
 * A class whose purpose is to provide static utility methods for solving the
 * n-queens problem with genetic algorithms. This includes fitness function,
 * goal test, random creation of individuals and convenience methods for
 * translating between between an NQueensBoard representation and the Integer
 * list representation used by the GeneticAlgorithm.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class NQueensGenAlgoUtil {

	public static FitnessFunction<Integer> getFitnessFunction() {
		return new NQueensFitnessFunction();
	}

	public static Predicate<Individual<Integer>> getGoalTest() {
		return new NQueensGenAlgoGoalTest();
	}

	// non repeated
	public static Individual<Integer> generateRandomIndividual(int boardSize) {
		List<Integer> individualRepresentation = new ArrayList<>();
		for (int i = 0; i < boardSize; i++) {
			individualRepresentation.add(i);
		}
		Collections.shuffle(individualRepresentation);
		return new Individual<>(individualRepresentation);
	}

	public static Collection<Integer> getFiniteAlphabetForBoardOfSize(int size) {
		Collection<Integer> fab = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			fab.add(i);
		}

		return fab;
	}

	public static class NQueensFitnessFunction implements FitnessFunction<Integer> {

		public double apply(Individual<Integer> individual) {
			double fitness = 0;

			NQueensBoard board = getBoardForIndividual(individual);
			
			for (XYLocation queen : board.getQueenPositions()) {
				if (board.getNumberOfAttacksOn(queen) == 0) {
					fitness++;
				}
			}
			return fitness;
			
		}
	}

	public static class NQueensGenAlgoGoalTest implements Predicate<Individual<Integer>> {
		private final Predicate<NQueensBoard> goalTest = NQueensFunctions::testGoal;

		@Override
		public boolean test(Individual<Integer> state) {
			return goalTest.test(getBoardForIndividual(state));
		}
	}

	public static NQueensBoard getBoardForIndividual(Individual<Integer> individual) {
		int boardSize = individual.length();
		NQueensBoard board = new NQueensBoard(boardSize);
		for (int i = 0; i < boardSize; i++) {
			int pos = individual.getRepresentation().get(i);
			board.addQueenAt(new XYLocation(i, pos));
		}
		return board;
	}
}