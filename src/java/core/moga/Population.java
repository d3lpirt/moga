package core.moga;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 20:00
 */
public class Population<T extends Chromosome> extends ArrayList<T> {

    public Population() {
    }

    public Population(int initialCapacity) {
        super(initialCapacity);
    }

    public Population(Collection<? extends T> c) {
        super(c);
    }

    public Population<T> union(Population<T> population) {
        Population<T> union = new Population<T>(this);
        union.addAll(population);
        return union;
    }
}
