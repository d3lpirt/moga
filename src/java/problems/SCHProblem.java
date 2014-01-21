package problems;

import core.moga.Chromosome;
import core.moga.Problem;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 21:09
 */
public class SCHProblem extends Problem {

    public SCHProblem() {
        super(2, 1,
                new double[]{-100},
                new double[]{100});
    }

    @Override
    public void evaluate(Chromosome chromosome) {
        double x = chromosome.getVariable(0);
        chromosome.setObjective(0, x * x);
        chromosome.setObjective(1, (x - 2) * (x - 2));
    }
}
