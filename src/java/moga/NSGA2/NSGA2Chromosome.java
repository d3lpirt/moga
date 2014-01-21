package moga.NSGA2;

import core.moga.Chromosome;

/**
 * User: Dmitry Beshkarev
 * Date: 04/12/13 Time: 19:23
 */
public class NSGA2Chromosome extends Chromosome {

    private int rank;
    private double crowdingDistance;

    public NSGA2Chromosome(NSGA2Chromosome chromosome) {
        super(chromosome);
    }

    public NSGA2Chromosome(Integer numberOfObjectives, Integer numberOfVariables) {
        super(numberOfObjectives, numberOfVariables);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }
}
