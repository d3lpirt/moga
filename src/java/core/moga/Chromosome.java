package core.moga;

import java.util.Arrays;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 19:45
 */
public class Chromosome {

    private Double[] variables;
    private Double[] objectives;

    public Chromosome(Chromosome chromosome) {
        variables = Arrays.copyOf(chromosome.variables, chromosome.variables.length);
        objectives = Arrays.copyOf(chromosome.objectives, chromosome.objectives.length);
    }

    public Chromosome(Integer numberOfObjectives, Integer numberOfVariables) {
        objectives = new Double[numberOfObjectives];
        variables = new Double[numberOfVariables];
    }

    public Double[] getVariables() {
        return variables;
    }

    public Double getVariable(int index) {
        return getVariables()[index];
    }

    public void setVariable(int index, double value) {
        getVariables()[index] = value;
    }

    public Double[] getObjectives() {
        return objectives;
    }

    public Double getObjective(int index) {
        return getObjectives()[index];
    }

    public void setObjective(int index, double value) {
        getObjectives()[index] = value;
    }
}
