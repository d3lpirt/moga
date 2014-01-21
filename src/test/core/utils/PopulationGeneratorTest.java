package core.utils;

import core.moga.Chromosome;
import core.moga.Population;
import core.moga.Problem;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 21:36
 */
public class PopulationGeneratorTest {

    @Test
    public void testGeneratePopulation() {
        int populationSize = 50;
        int numberOfObjectives = 2;
        int numberOfVariables = 5;
        double[] lowerLimitArray = new double[] {-1, -2, -3, -4, -5};
        double[] upperLimitArray = new double[] {1, 2, 3, 4, 5};

        Problem testProblem = getTestProblem(numberOfObjectives, numberOfVariables, lowerLimitArray, upperLimitArray);
        Population<Chromosome> population = PopulationGenerator.generatePopulation(populationSize, testProblem);
        
        assertEquals("Unexpected population size.", populationSize, population.size());
        for (Chromosome chromosome : population) {
            for (int i = 0; i < numberOfVariables; i++) {
                double variable = chromosome.getVariable(i);
                assertTrue("Variable should be in the specified bounds.", variable >= lowerLimitArray[i] && variable <= upperLimitArray[i]);
                assertNotNull(chromosome.getObjective(0));
                assertNotNull(chromosome.getObjective(1));
            }
        }
    }
    
    private Problem getTestProblem(int numberOfObjectives, int numberOfVariables, double[] lowerLimitArray, double[] upperLimitArray) {
        return new Problem(numberOfObjectives, numberOfVariables, lowerLimitArray, upperLimitArray) {
            @Override
            public void evaluate(Chromosome chromosome) {
                double fitness1 = 0;
                double fitness2 = 0;
                for (int j = 0; j < getNumberOfVariables(); j++) {
                    double variable = chromosome.getVariable(j);
                    fitness1 += variable;
                    fitness2 -= variable;
                }
                chromosome.setObjective(0, fitness1);
                chromosome.setObjective(1, fitness2);
            }
        };
    }
}
