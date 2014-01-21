package operators.mutation;

import core.moga.Chromosome;
import core.moga.Problem;
import core.moga.operators.Mutation;
import core.utils.RandomGenerator;

/**
 * User: Dmitry Beshkarev
 * Date: 01/12/13 Time: 17:28
 */
public class PolynomialMutation implements Mutation {

    private static final double ETA_M = 20.0;
    private double distributionIndex;
    private double probability;

    public PolynomialMutation() {
        probability = 0.1;
        distributionIndex = 20.0;
    }

    public PolynomialMutation(double probability, double distributionIndex) {
        this.distributionIndex = distributionIndex;
        this.probability = probability;
    }

    public void execute(Chromosome chromosome, Problem problem) {
        int numberOfVariables = chromosome.getVariables().length;

        double rnd, delta1, delta2, mut_pow, deltaq;
        double y, yl, yu, val, xy;

        RandomGenerator randomGenerator = new RandomGenerator();

        for (int var = 0; var < numberOfVariables; var++) {
            if (randomGenerator.nextDouble() <= probability) {
                y = chromosome.getVariable(var);
                yl = problem.getLowerLimit(var);
                yu = problem.getUpperLimit(var);
                delta1 = (y - yl) / (yu - yl);
                delta2 = (yu - y) / (yu - yl);
                rnd = randomGenerator.nextDouble();
                mut_pow = 1.0 / (ETA_M + 1.0);
                if (rnd <= 0.5) {
                    xy = 1.0 - delta1;
                    val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, (distributionIndex + 1.0)));
                    deltaq = Math.pow(val, mut_pow) - 1.0;
                } else {
                    xy = 1.0 - delta2;
                    val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, (distributionIndex + 1.0)));
                    deltaq = 1.0 - (Math.pow(val, mut_pow));
                }
                y = y + deltaq * (yu - yl);
                if (y < yl) {
                    y = yl;
                }
                if (y > yu) {
                    y = yu;
                }
                chromosome.setVariable(var, y);
            }
        }
    }
}
