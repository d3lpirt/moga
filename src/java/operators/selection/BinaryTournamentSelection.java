package operators.selection;

import core.moga.Chromosome;
import core.moga.Population;
import core.moga.operators.Selection;
import core.utils.RandomGenerator;
import utils.DominanceComparator;

import java.util.Comparator;

/**
 * User: Dmitry Beshkarev
 * Date: 01/12/13 Time: 15:04
 */
public class BinaryTournamentSelection<T extends Chromosome> extends Selection<T> {

    public BinaryTournamentSelection(Comparator<T> comparator) {
        super(comparator);
    }

    public T execute(Population<T> population) {
        int populationSize = population.size();
        RandomGenerator randomGenerator = new RandomGenerator();

        T chromosome1 = population.get(randomGenerator.nextInt(populationSize - 1));
        T chromosome2 = population.get(randomGenerator.nextInt(populationSize - 1));
        while (chromosome1 == chromosome2) {
            chromosome2 = population.get(randomGenerator.nextInt(populationSize - 1));
        }

        // TODO: fitness values should be compared and if they are equal then compare distance to kth closes
        int result = getComparator().compare(chromosome1, chromosome2);
        if (result > 0) {
            return chromosome1;
        } else if (result < 0) {
            return chromosome2;
        }
        return randomGenerator.nextDouble() < 0.5 ? chromosome1 : chromosome2;
    }
}
