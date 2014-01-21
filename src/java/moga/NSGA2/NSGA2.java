package moga.NSGA2;

import core.moga.MOGA;
import core.moga.Population;
import core.moga.Problem;
import core.moga.operators.Crossover;
import core.moga.operators.Mutation;
import core.moga.operators.Selection;
import core.utils.PopulationGenerator;
import utils.DominanceComparator;

import java.io.IOException;
import java.util.*;

/**
 * User: Dmitry Beshkarev
 * Date: 04/12/13 Time: 19:24
 */
public class NSGA2 extends MOGA<NSGA2Chromosome, NSGA2Settings> {

    public NSGA2(NSGA2Settings settings, Problem problem) throws IOException {
        super(settings, problem);
    }

    @Override
    public Population<NSGA2Chromosome> execute() {
        NSGA2Settings settings = getSettings();
        int populationSize = settings.getPopulationSize();
        int numberOfGenerations = settings.getNumberOfGenerations();

        Problem problem = getProblem();
        Population<NSGA2Chromosome> population = PopulationGenerator.generatePopulation(NSGA2Chromosome.class, populationSize, problem);

        List<Population<NSGA2Chromosome>> rankedFronts = nonDominatedSort(population);
        for (Population<NSGA2Chromosome> front : rankedFronts) {
            assignCrowdingDistance(front, problem.getNumberOfObjectives());
        }

        int currentGeneration = 0;
        while (currentGeneration < numberOfGenerations) {
            population = generateNextPopulation(population, problem, populationSize);
            currentGeneration++;
        }
        return population;
    }

    private Population<NSGA2Chromosome> generateNextPopulation(Population<NSGA2Chromosome> population, Problem problem, int populationSize) {
        Population<NSGA2Chromosome> offspringPopulation = generateOffspringPopulation(population, problem, populationSize);
        offspringPopulation = offspringPopulation.union(population);
        List<Population<NSGA2Chromosome>> rankedFronts = nonDominatedSort(offspringPopulation);

        Population<NSGA2Chromosome> nextPopulation = new Population<NSGA2Chromosome>(populationSize);
        Iterator<Population<NSGA2Chromosome>> frontIterator = rankedFronts.iterator();
        while (nextPopulation.size() < populationSize) {
            Population<NSGA2Chromosome> front = frontIterator.next();
            assignCrowdingDistance(front, problem.getNumberOfObjectives());
            if (nextPopulation.size() + front.size() <= populationSize) {
                nextPopulation.addAll(front);
            } else {
                Collections.sort(front, getCrowdingDistanceComparator());
                Iterator<NSGA2Chromosome> iterator = front.iterator();
                while (nextPopulation.size() < populationSize) {
                    nextPopulation.add(iterator.next());
                }
            }
        }
        return nextPopulation;
    }

    private List<Population<NSGA2Chromosome>> nonDominatedSort(Population<NSGA2Chromosome> population) {
        Population<NSGA2Chromosome> sortingPopulation = new Population<NSGA2Chromosome>(population);
        ArrayList<Population<NSGA2Chromosome>> fronts = new ArrayList<Population<NSGA2Chromosome>>();
        DominanceComparator dominanceComparator = new DominanceComparator();

        int currentRank = 0;
        while (!sortingPopulation.isEmpty()) {
            int[] dominatedArray = new int[sortingPopulation.size()];
            for (int i = 0; i < sortingPopulation.size(); i++) {
                for (int j = 0; j < sortingPopulation.size(); j++) {
                    if (i == j) continue;
                    int compare = dominanceComparator.compare(sortingPopulation.get(i), sortingPopulation.get(j));
                    if (compare < 0) {
                        dominatedArray[i]++;
                    }
                }
            }
            Population<NSGA2Chromosome> currentFront = new Population<NSGA2Chromosome>();
            fronts.add(currentFront);
            for (int i = 0; i < dominatedArray.length; i++) {
                if (dominatedArray[i] == 0) {
                    NSGA2Chromosome currentRankChromosome = sortingPopulation.get(i);
                    currentRankChromosome.setRank(currentRank);
                    currentFront.add(currentRankChromosome);
                }
            }
            sortingPopulation.removeAll(currentFront);
            currentRank++;
        }
        return fronts;
    }

    private Population<NSGA2Chromosome> generateOffspringPopulation(Population<NSGA2Chromosome> population, Problem problem, int populationSize) {
        Mutation mutation = getSettings().getMutation();
        Selection<NSGA2Chromosome> selection = getSettings().getSelection();
        Crossover<NSGA2Chromosome> crossover = getSettings().getCrossover();

        Population<NSGA2Chromosome> offspringPopulation = new Population<NSGA2Chromosome>();

        for (int i = 0; i < (populationSize / 2); i++) {
            NSGA2Chromosome parent1 = selection.execute(population);
            NSGA2Chromosome parent2 = selection.execute(population);
            while (parent1 == parent2) {
                parent2 = selection.execute(population);
            }

            List<NSGA2Chromosome> children = crossover.execute(parent1, parent2, problem);
            NSGA2Chromosome child1 = children.get(0);
            NSGA2Chromosome child2 = children.get(1);

            mutation.execute(child1, problem);
            mutation.execute(child2, problem);
            problem.evaluate(child1);
            problem.evaluate(child2);
            offspringPopulation.add(child1);
            offspringPopulation.add(child2);
        }
        return offspringPopulation;
    }

    public void assignCrowdingDistance(Population<NSGA2Chromosome> population, int numberOfObjectives) {
        int size = population.size();

        if (size == 0) {
            return;
        }
        if (size == 1) {
            population.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            return;
        }
        if (size == 2) {
            population.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            population.get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);
            return;
        }

        Population<NSGA2Chromosome> front = new Population<NSGA2Chromosome>(population);
        for (int i = 0; i < size; i++) {
            front.get(i).setCrowdingDistance(0.0);
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            Collections.sort(front, getObjectiveComparator(i));
            double objectiveMinN = front.get(0).getObjective(i);
            double objectiveMaxN = front.get(front.size() - 1).getObjective(i);

            front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            front.get(size - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            for (int j = 1; j < size - 1; j++) {
                double distance = front.get(j + 1).getObjective(i) - front.get(j - 1).getObjective(i);
                distance = distance / (objectiveMaxN - objectiveMinN);
                distance += front.get(j).getCrowdingDistance();
                front.get(j).setCrowdingDistance(distance);
            }
        }
    }

    private Comparator<NSGA2Chromosome> getObjectiveComparator(final int objectiveIndex) {
        return new Comparator<NSGA2Chromosome>() {
            public int compare(NSGA2Chromosome o1, NSGA2Chromosome o2) {
                return o1.getObjective(objectiveIndex).compareTo(o2.getObjective(objectiveIndex));
            }
        };
    }

    private Comparator<NSGA2Chromosome> getCrowdingDistanceComparator() {
        return new Comparator<NSGA2Chromosome>() {
            public int compare(NSGA2Chromosome o1, NSGA2Chromosome o2) {
                return new Double(o2.getCrowdingDistance()).compareTo(o1.getCrowdingDistance());
            }
        };
    }
}
