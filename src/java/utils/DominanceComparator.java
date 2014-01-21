package utils;

import core.moga.Chromosome;

import java.util.Comparator;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 22:57
 */
public class DominanceComparator<T extends Chromosome> implements Comparator<T> {

    public int compare(T chromosome1, T chromosome2) {
        int dominate1 = 0;
        int dominate2 = 0;

        int numberOfObjectives = chromosome1.getObjectives().length;
        for (int i = 0; i < numberOfObjectives; i++) {
            int compareResult = chromosome1.getObjective(i).compareTo(chromosome2.getObjective(i));
            if (compareResult < 0) {
                dominate1 = 1;
            }
            if (compareResult > 0) {
                dominate2 = 1;
            }
        }
        if (dominate1 == dominate2) {
            return 0;
        }
        if (dominate1 == 1) {
            return 1;
        }
        return -1;
    }
}
