package core.moga;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 19:39
 */
public abstract class Problem {

    private int numberOfVariables;
    private int numberOfObjectives;

    private double[] lowerLimitArray;
    private double[] upperLimitArray;

    protected Problem(int numberOfObjectives, int numberOfVariables, double[] lowerLimitArray, double[] upperLimitArray) {
        this.numberOfVariables = numberOfVariables;
        this.numberOfObjectives = numberOfObjectives;
        this.lowerLimitArray = lowerLimitArray;
        this.upperLimitArray = upperLimitArray;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public double[] getLowerLimitArray() {
        return lowerLimitArray;
    }

    public double[] getUpperLimitArray() {
        return upperLimitArray;
    }

    public double getLowerLimit(int index) {
        return getLowerLimitArray()[index];
    }

    public double getUpperLimit(int index) {
        return getUpperLimitArray()[index];
    }

    public abstract void evaluate(Chromosome chromosome);
}
