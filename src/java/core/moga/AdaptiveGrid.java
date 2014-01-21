package core.moga;

import core.utils.RandomGenerator;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 21:48
 */
public class AdaptiveGrid<T extends Chromosome> {

    private int numberOfBisections;
    private int numberOfObjectives;
    private int mostPopulated;
    private int[] hypercubes;
    private int[] occupied;
    private double[] lowerLimits;
    private double[] upperLimits;
    private double[] divisionSize;

    public AdaptiveGrid(int numberOfBisections, int numberOfObjectives) {
        this.numberOfBisections = numberOfBisections;
        this.numberOfObjectives = numberOfObjectives;
        lowerLimits = new double[numberOfObjectives];
        upperLimits = new double[numberOfObjectives];
        divisionSize = new double[numberOfObjectives];
        hypercubes = new int[(int) Math.pow(2.0, numberOfBisections * numberOfObjectives)];

        for (int i = 0; i < hypercubes.length; i++) {
            hypercubes[i] = 0;
        }
    }

    private void updateLimits(Population<T> population) {
        for (int obj = 0; obj < numberOfObjectives; obj++) {
            lowerLimits[obj] = Double.MAX_VALUE;
            upperLimits[obj] = Double.MIN_VALUE;
        }

        for (T chromosome : population) {
            for (int i = 0; i < numberOfObjectives; i++) {
                if (chromosome.getObjective(i) < lowerLimits[i]) {
                    lowerLimits[i] = chromosome.getObjective(i);
                }
                if (chromosome.getObjective(i) > upperLimits[i]) {
                    upperLimits[i] = chromosome.getObjective(i);
                }
            }
        }
    }

    private void addPopulation(Population<T> solutionSet) {
        mostPopulated = 0;
        int location;

        for (T chromosome : solutionSet) {
            location = location(chromosome);
            hypercubes[location]++;
            if (hypercubes[location] > hypercubes[mostPopulated])
                mostPopulated = location;
        }
        calculateOccupied();
    }

    public void updateGrid(Population<T> population) {
        updateLimits(population);

        for (int obj = 0; obj < numberOfObjectives; obj++) {
            divisionSize[obj] = upperLimits[obj] - lowerLimits[obj];
        }
        for (int i = 0; i < hypercubes.length; i++) {
            hypercubes[i] = 0;
        }

        addPopulation(population);
    }

    public void updateGrid(T chromosome, Population<T> population) {
        int location = location(chromosome);
        if (location == -1) {
            updateLimits(population);

            for (int obj = 0; obj < numberOfObjectives; obj++) {
                if (chromosome.getObjective(obj) < lowerLimits[obj]) {
                    lowerLimits[obj] = chromosome.getObjective(obj);
                } else if (chromosome.getObjective(obj) > upperLimits[obj]) {
                    upperLimits[obj] = chromosome.getObjective(obj);
                }
            }
            for (int obj = 0; obj < numberOfObjectives; obj++) {
                divisionSize[obj] = upperLimits[obj] - lowerLimits[obj];
            }
            for (int i = 0; i < hypercubes.length; i++) {
                hypercubes[i] = 0;
            }

            addPopulation(population);
        }
    }


    public int location(T chromosome) {
        int[] position = new int[numberOfObjectives];

        for (int obj = 0; obj < numberOfObjectives; obj++) {
            if (chromosome.getObjective(obj) > upperLimits[obj] || chromosome.getObjective(obj) < lowerLimits[obj]) {
                return -1;
            } else if (chromosome.getObjective(obj) == lowerLimits[obj]) {
                position[obj] = 0;
            } else if (chromosome.getObjective(obj) == upperLimits[obj]) {
                position[obj] = ((int) Math.pow(2.0, numberOfBisections)) - 1;
            } else {
                double tmpSize = divisionSize[obj];
                double objectiveValue = chromosome.getObjective(obj);
                double account = lowerLimits[obj];
                int ranges = (int) Math.pow(2.0, numberOfBisections);
                for (int b = 0; b < numberOfBisections; b++) {
                    tmpSize /= 2.0;
                    ranges /= 2;
                    if (objectiveValue > (account + tmpSize)) {
                        position[obj] += ranges;
                        account += tmpSize;
                    }
                }
            }
        }

        int location = 0;
        for (int obj = 0; obj < numberOfObjectives; obj++) {
            location += position[obj] * Math.pow(2.0, obj * numberOfBisections);
        }
        return location;
    }

    public void removeSolution(int location) {
        hypercubes[location]--;

        if (location == mostPopulated) {
            for (int i = 0; i < hypercubes.length; i++) {
                if (hypercubes[i] > hypercubes[mostPopulated]) {
                    mostPopulated = i;
                }
            }
        }
        if (hypercubes[location] == 0) {
            calculateOccupied();
        }
    }

    public void addSolution(int location) {
        hypercubes[location]++;

        if (hypercubes[location] > hypercubes[mostPopulated]) {
            mostPopulated = location;
        }
        if (hypercubes[location] == 1) {
            calculateOccupied();
        }
    }

    public int getNumberOfBisections() {
        return numberOfBisections;
    }

    public int rouletteWheel() {
        double inverseSum = 0.0;
        for (int aHypercubes_ : hypercubes) {
            if (aHypercubes_ > 0) {
                inverseSum += 1.0 / (double) aHypercubes_;
            }
        }

        double random = new RandomGenerator().nextDouble(0.0, inverseSum);
        int hypercube = 0;
        double accumulatedSum = 0.0;
        while (hypercube < hypercubes.length) {
            if (hypercubes[hypercube] > 0) {
                accumulatedSum += 1.0 / (double) hypercubes[hypercube];
            }
            if (accumulatedSum > random) {
                return hypercube;
            }
            hypercube++;
        }
        return hypercube;
    }

    public int calculateOccupied() {
        int total = 0;
        for (int aHypercubes_ : hypercubes) {
            if (aHypercubes_ > 0) {
                total++;
            }
        }
        occupied = new int[total];
        int base = 0;
        for (int i = 0; i < hypercubes.length; i++) {
            if (hypercubes[i] > 0) {
                occupied[base] = i;
                base++;
            }
        }
        return total;
    }

    public int occupiedHypercubes() {
        return occupied.length;
    }

    public int randomOccupiedHypercube() {
        int rand = new RandomGenerator().nextInt(0, occupied.length - 1);
        return occupied[rand];
    }

    public int getMostPopulated() {
        return mostPopulated;
    }

    public int getLocationDensity(int location) {
        return hypercubes[location];
    }
}
