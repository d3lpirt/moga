package moga.SPEA2;

import core.moga.Chromosome;
import core.moga.Population;

import java.util.Arrays;

/**
 * SPEA2Utils<br/>
 *
 * @author <a href="mailto:dmitry.beshkarev@ctco.lv">Dmitry Beshkarev</a>
 * @version 1.0 date: 11/29/13 time: 11:13 AM
 */
public class SPEA2Utils {

    public double[][] getDistanceMatrix(Population<SPEA2Chromosome> population) {
        int size = population.size();
        double[][] distanceMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            SPEA2Chromosome chromosomeFrom = population.get(i);
            for (int j = 0; j < size; j++) {
                SPEA2Chromosome chromosomeTo = population.get(j);
                double distance = i != j ? getDistance(chromosomeFrom, chromosomeTo) : 0;
                distanceMatrix[i][j] = distance;
            }
            Arrays.sort(distanceMatrix[i]);
        }
        return distanceMatrix;
    }

    public double getDistance(Chromosome chromosomeFrom, Chromosome chromosomeTo) {
        double diff;
        double distance = 0.0;
        int numberOfObjectives = chromosomeFrom.getObjectives().length;
        for (int i = 0; i < numberOfObjectives; i++) {
            diff = chromosomeFrom.getObjective(i) - chromosomeTo.getObjective(i);
            distance += Math.pow(diff, 2.0);
        }
        return Math.sqrt(distance);
    }
}
