package core.moga;

import core.moga.operators.Crossover;
import core.moga.operators.Mutation;
import core.moga.operators.Selection;
import core.utils.PropertiesUtil;

import java.io.IOException;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 20:45
 */
public abstract class MOGASettings<T extends Chromosome> {

    public static final String POPULATION_SIZE = "populationSize";
    public static final String NUMBER_OF_GENERATIONS = "numberOfGenerations";
    public static final String CROSSOVER = "crossover";
    public static final String MUTATION = "mutation";

    private int populationSize;
    private int numberOfGenerations;

    protected Mutation mutation;
    protected Crossover<T> crossover;
    protected Selection<T> selection;

    public MOGASettings(String mogaName) throws IOException {
        PropertiesUtil propertiesUtil = new PropertiesUtil(mogaName);
        load(propertiesUtil);
    }

    protected void load(PropertiesUtil propertiesUtil) {
        this.populationSize = propertiesUtil.getIntProperty(POPULATION_SIZE);
        this.numberOfGenerations = propertiesUtil.getIntProperty(NUMBER_OF_GENERATIONS);
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getNumberOfGenerations() {
        return numberOfGenerations;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public Crossover<T> getCrossover() {
        return crossover;
    }

    public Selection<T> getSelection() {
        return selection;
    }
}
