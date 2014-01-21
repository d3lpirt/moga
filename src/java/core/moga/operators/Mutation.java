package core.moga.operators;

import core.moga.Chromosome;
import core.moga.Problem;

/**
 * User: Dmitry Beshkarev
 * Date: 01/12/13 Time: 17:43
 */
public interface Mutation {

    void execute(Chromosome chromosome, Problem problem);
}
