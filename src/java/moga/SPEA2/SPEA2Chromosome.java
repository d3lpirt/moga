package moga.SPEA2;

import core.moga.Chromosome;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 22:52
 */
public class SPEA2Chromosome extends Chromosome {

    private int strength = 0;
    private int wimpiness = 0;
    private double fitness = 0.0;

    public SPEA2Chromosome(SPEA2Chromosome chromosome) {
        super(chromosome);
        this.strength = chromosome.getStrength();
        this.wimpiness = chromosome.getWimpiness();
        this.fitness = chromosome.getFitness();
    }

    public SPEA2Chromosome(Integer numberOfObjectives, Integer numberOfVariables) {
        super(numberOfObjectives, numberOfVariables);
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getWimpiness() {
        return wimpiness;
    }

    public void setWimpiness(int wimpiness) {
        this.wimpiness = wimpiness;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
