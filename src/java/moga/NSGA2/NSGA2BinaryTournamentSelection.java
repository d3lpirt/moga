package moga.NSGA2;

import core.moga.Population;
import core.utils.RandomGenerator;

import java.util.Comparator;

/**
 * User: Dmitry Beshkarev
 * Date: 04/12/13 Time: 19:43
 */
public class NSGA2BinaryTournamentSelection {

    public NSGA2Chromosome execute(Population<NSGA2Chromosome> population) {
        int populationSize = population.size();
        RandomGenerator randomGenerator = new RandomGenerator();

        NSGA2Chromosome chromosome1 = population.get(randomGenerator.nextInt(populationSize - 1));
        NSGA2Chromosome chromosome2 = population.get(randomGenerator.nextInt(populationSize - 1));
        while (chromosome1 == chromosome2) {
            chromosome2 = population.get(randomGenerator.nextInt(populationSize - 1));
        }
        int result = getComparator().compare(chromosome1, chromosome2);
        if (result > 0) {
            return chromosome1;
        } else if (result < 0) {
            return chromosome2;
        }
        return new RandomGenerator().nextDouble() < 0.5 ? chromosome1 : chromosome2;
    }
    
    private Comparator<NSGA2Chromosome> getComparator() {
        return new Comparator<NSGA2Chromosome>() {
            public int compare(NSGA2Chromosome o1, NSGA2Chromosome o2) {
                // The lower the rank the better
                int result = new Integer(o2.getRank()).compareTo(o1.getRank());
                return result != 0 ? result : new Double(o1.getCrowdingDistance()).compareTo(o2.getCrowdingDistance());
            }
        };
    }
}