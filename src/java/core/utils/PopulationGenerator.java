package core.utils;

import core.moga.Chromosome;
import core.moga.Population;
import core.moga.Problem;

import java.lang.reflect.Constructor;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 20:21
 */
public class PopulationGenerator {

    public static Population<Chromosome> generatePopulation(int populationSize, Problem problem) {
        return generatePopulation(Chromosome.class, populationSize, problem);
    }

    public static <T extends Chromosome> Population<T> generatePopulation(Class<T> chromosomeClass, int populationSize, Problem problem) {
        int numberOfVariables = problem.getNumberOfVariables();
        int numberOfObjectives = problem.getNumberOfObjectives();

        Population<T> population = new Population<T>(populationSize);
        RandomGenerator randomGenerator = new RandomGenerator();
        for (int i = 0; i < populationSize; i++) {
            T chromosome = createChromosome(chromosomeClass, numberOfVariables, numberOfObjectives);
            for (int j = 0; j < numberOfVariables; j++) {
                double lowerLimit = problem.getLowerLimit(j);
                double upperLimit = problem.getUpperLimit(j);
                double value = randomGenerator.nextDouble() * (upperLimit - lowerLimit) + lowerLimit;
                chromosome.setVariable(j, value);
            }
            problem.evaluate(chromosome);
            population.add(chromosome);
        }
        return population;
    }

    private static <T extends Chromosome> T createChromosome(Class<T> chromosomeClass, int numberOfVariables, int numberOfObjectives) {
        try {
            Constructor<T> constructor = chromosomeClass.getConstructor(Integer.class, Integer.class);
            return constructor.newInstance(numberOfObjectives, numberOfVariables);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Chromosome instance of class " + chromosomeClass, e);
        }
    }
}
