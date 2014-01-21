package operators.crossover;

import core.moga.Chromosome;
import core.moga.Problem;
import core.moga.operators.Crossover;
import core.utils.RandomGenerator;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

/**
 * User: Dmitry Beshkarev
 * Date: 07/12/13 Time: 17:14
 */
public class SBXCrossover_2<T extends Chromosome> implements Crossover<T> {

    private double probability;
    private double distributionIndex;

    private Class<T> chromosomeClass;

    public SBXCrossover_2(Class<T> chromosomeClass) {
        this.probability = 0.9;
        this.distributionIndex = 20.0;
        this.chromosomeClass = chromosomeClass;
    }

    public SBXCrossover_2(Class<T> chromosomeClass, double probability, double distributionIndex) {
        this.probability = probability;
        this.distributionIndex = distributionIndex;
        this.chromosomeClass = chromosomeClass;
    }

    public List<T> execute(T parent1, T parent2, Problem problem) {
        T offspring1 = createChromosome(parent1);
        T offspring2 = createChromosome(parent2);

        double rand;
        double y1, y2, lowerLimit, upperLimit;
        double c1, c2;
        double alpha, beta, betaq;

        RandomGenerator randomGenerator = new RandomGenerator();
        if (randomGenerator.nextDouble() <= probability) {
            for (int i = 0; i < problem.getNumberOfVariables(); i++) {
                double valueX1 = parent1.getVariable(i);
                double valueX2 = parent2.getVariable(i);

                if (valueX1 < valueX2) {
                    y1 = valueX1;
                    y2 = valueX2;
                } else {
                    y1 = valueX2;
                    y2 = valueX1;
                }

                lowerLimit = problem.getLowerLimit(i);
                upperLimit = problem.getUpperLimit(i);
                rand = randomGenerator.nextDouble();
                beta = 1.0 + (2.0 * (y1 - lowerLimit) / (y2 - y1));
                alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                if (rand <= (1.0 / alpha)) {
                    betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                } else {
                    betaq = Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (distributionIndex + 1.0)));
                }

                c1 = 0.5 * ((y1 + y2) - betaq * (y2 - y1));
                beta = 1.0 + (2.0 * (upperLimit - y2) / (y2 - y1));
                alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                if (rand <= (1.0 / alpha)) {
                    betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                } else {
                    betaq = Math.pow((1.0 / (2.0 - rand * alpha)), (1.0 / (distributionIndex + 1.0)));
                }

                c2 = 0.5 * ((y1 + y2) + betaq * (y2 - y1));

                if (c1 < lowerLimit)
                    c1 = lowerLimit;

                if (c2 < lowerLimit)
                    c2 = lowerLimit;

                if (c1 > upperLimit)
                    c1 = upperLimit;

                if (c2 > upperLimit)
                    c2 = upperLimit;

                if (randomGenerator.nextDouble() <= 0.5) {
                    offspring1.setVariable(i, c2);
                    offspring2.setVariable(i, c1);
                } else {
                    offspring1.setVariable(i, c1);
                    offspring2.setVariable(i, c2);
                }
            }
        }
        return Arrays.asList(offspring1, offspring2);
    }

    private T createChromosome(T chromosome) {
        try {
            Constructor<T> constructor = chromosomeClass.getConstructor(chromosomeClass);
            return constructor.newInstance(chromosome);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
