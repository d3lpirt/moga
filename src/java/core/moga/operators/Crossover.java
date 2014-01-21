package core.moga.operators;

import core.moga.Chromosome;
import core.moga.Problem;

import java.util.List;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 22:18
 */
public interface Crossover<T extends Chromosome> {

    public List<T> execute(T parent1, T parent2, Problem problem);
}
