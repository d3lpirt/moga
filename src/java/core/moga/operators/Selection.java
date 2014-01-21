package core.moga.operators;

import core.moga.Chromosome;
import core.moga.Population;

import java.util.Comparator;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 12:13
 */
public abstract class Selection<T extends Chromosome> {

    private Comparator<T> comparator;

    protected Selection(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public abstract T execute(Population<T> population);

    public Comparator<T> getComparator() {
        return comparator;
    }
}
