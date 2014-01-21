package moga.SPEA2;

import core.moga.Chromosome;
import core.moga.*;
import core.moga.operators.Crossover;
import core.moga.operators.Mutation;
import core.moga.operators.Selection;
import core.utils.PopulationGenerator;
import core.moga.Problem;
import operators.selection.EnvironmentalSelection;
import utils.DominanceComparator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 20:13
 */
public class SPEA2 extends MOGA<SPEA2Chromosome, SPEA2Settings> {

    public SPEA2(SPEA2Settings settings, Problem problem) throws IOException {
        super(settings, problem);
    }

    @Override
    public Population<SPEA2Chromosome> execute() {
        SPEA2Settings settings = getSettings();
        int populationSize = settings.getPopulationSize();
        int numberOfGenerations = settings.getNumberOfGenerations();

        Problem problem = getProblem();
        Population<SPEA2Chromosome> population = PopulationGenerator.generatePopulation(SPEA2Chromosome.class, populationSize, problem);

        Population<SPEA2Chromosome> archive = new Population<SPEA2Chromosome>(populationSize);

        int currentGeneration = 0;
        while (currentGeneration < numberOfGenerations) {
            Population<SPEA2Chromosome> union = population.union(archive);
            assignFitness(union);
            archive = new EnvironmentalSelection().execute(union, populationSize);
            population = generateNextPopulation(archive, problem, populationSize);
            currentGeneration++;
        }
        return population;
    }

    private Population<SPEA2Chromosome> generateNextPopulation(Population<SPEA2Chromosome> archive, Problem problem, int populationSize) {
        Mutation mutation = getSettings().getMutation();
        Crossover<SPEA2Chromosome> crossover = getSettings().getCrossover();
        Selection<SPEA2Chromosome> selection = getSettings().getSelection();

        Population<SPEA2Chromosome> newPopulation = new Population<SPEA2Chromosome>(populationSize);
        while (newPopulation.size() < populationSize) {
            SPEA2Chromosome parent1 = selection.execute(archive);
            SPEA2Chromosome parent2 = selection.execute(archive);
            while (parent1 == parent2) {
                parent2 = selection.execute(archive);
            }

            List<SPEA2Chromosome> children = crossover.execute(parent1, parent2, problem);
            SPEA2Chromosome child1 = children.get(0);
            SPEA2Chromosome child2 = children.get(1);
            mutation.execute(child1, problem);
            mutation.execute(child2, problem);
            problem.evaluate(child1);
            problem.evaluate(child2);
            newPopulation.add(child1);
            newPopulation.add(child2);
        }
        return newPopulation;
    }

    private void assignFitness(Population<SPEA2Chromosome> population) {
        DominanceComparator<SPEA2Chromosome> dominanceComparator = new DominanceComparator<SPEA2Chromosome>();
        calculateStrength(population, dominanceComparator);
        calculateWimpiness(population, dominanceComparator);
        calculateFitness(population);
    }

    private void calculateFitness(Population<SPEA2Chromosome> population) {
        int k = (int) Math.round(Math.sqrt(population.size()));
        double[][] distanceMatrix = getDistanceMatrix(population);
        for (int i = 0; i < population.size(); i++) {
            double[] distances = distanceMatrix[i];
            SPEA2Chromosome chromosome = population.get(i);
            double fitness = chromosome.getWimpiness() + (1 / (2 + distances[k]));
            chromosome.setFitness(fitness);
        }
    }

    private void calculateStrength(Population<SPEA2Chromosome> population, DominanceComparator<SPEA2Chromosome> dominanceComparator) {
        for (int i = 0; i < population.size(); i++) {
            SPEA2Chromosome chromosome1 = population.get(i);
            for (int j = 0; j < population.size(); j++) {
                if (i == j) {
                    continue;
                }
                SPEA2Chromosome chromosome2 = population.get(j);
                if (dominanceComparator.compare(chromosome1, chromosome2) > 0) {
                    chromosome1.setStrength(chromosome1.getStrength() + 1);
                }
            }
        }
    }

    private void calculateWimpiness(Population<SPEA2Chromosome> population, DominanceComparator<SPEA2Chromosome> dominanceComparator) {
        for (int i = 0; i < population.size(); i++) {
            SPEA2Chromosome chromosome1 = population.get(i);
            for (int j = 0; j < population.size(); j++) {
                if (i == j) {
                    continue;
                }
                SPEA2Chromosome chromosome2 = population.get(j);
                if (dominanceComparator.compare(chromosome1, chromosome2) < 0) {
                    int wimpiness = chromosome1.getWimpiness() + chromosome2.getStrength();
                    chromosome1.setWimpiness(wimpiness);
                }
            }
        }
    }

    private double[][] getDistanceMatrix(Population<SPEA2Chromosome> population) {
        int size = population.size();
        double[][] distanceMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            SPEA2Chromosome chromosomeFrom = population.get(i);
            for (int j = 0; j < size; j++) {
                SPEA2Chromosome chromosomeTo = population.get(j);
                double distance = i != j ? getDistance(chromosomeFrom, chromosomeTo) : 0;
                distanceMatrix[i][j] = distance;
            }
            Arrays.sort(distanceMatrix[i]);
        }
        return distanceMatrix;
    }

    public double getDistance(Chromosome chromosomeFrom, Chromosome chromosomeTo) {
        double distance = 0.0;
        int numberOfObjectives = chromosomeFrom.getObjectives().length;
        for (int i = 0; i < numberOfObjectives; i++) {
            double diff = chromosomeFrom.getObjective(i) - chromosomeTo.getObjective(i);
            distance += Math.pow(diff, 2);
        }
        return Math.sqrt(distance);
    }
}
