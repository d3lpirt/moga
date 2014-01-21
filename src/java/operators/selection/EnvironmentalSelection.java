package operators.selection;

import core.moga.Population;
import moga.SPEA2.SPEA2Chromosome;
import moga.SPEA2.SPEA2Utils;

import java.util.*;

/**
 * EnvironmentalSelection<br/>
 *
 * @author <a href="mailto:dmitry.beshkarev@ctco.lv">Dmitry Beshkarev</a>
 * @version 1.0 date: 11/29/13 time: 10:41 AM
 */
public class EnvironmentalSelection {

    public Population<SPEA2Chromosome> execute(Population<SPEA2Chromosome> population, int size) {
        if (population.size() < size) {
            size = population.size();
        }

        Population<SPEA2Chromosome> archive = new Population<SPEA2Chromosome>(size);

        // All individuals with Wimpiness = 0 will have < 1 fitness
        for (SPEA2Chromosome spea2Chromosome : population) {
            if (spea2Chromosome.getFitness() < 1.0) {
                archive.add(spea2Chromosome);
            }
        }

        Comparator<SPEA2Chromosome> fitnessComparator = getFitnessComparator();
        if (archive.size() < size) {
            Collections.sort(population, fitnessComparator);
            for (SPEA2Chromosome spea2Chromosome : population) {
                if (spea2Chromosome.getFitness() >= 1) {
                    archive.add(spea2Chromosome);
                }
                if (archive.size() == size) {
                    return archive;
                }
            }
        } else if (archive.size() == size) {
            return archive;
        }

        SPEA2Utils spea2Utils = new SPEA2Utils();
        while (archive.size() > size) {
            int closestIndex = 0;
            // TODO: Can optimize this by not building the distance matrix on every iteration.
            // TODO: Need a reference to the chromosome that is being removed and remove it from the distance matrix as well.
            // TODO: In this case the double dimension array should be replaced with a List<List>.
            double[][] distanceMatrix = spea2Utils.getDistanceMatrix(archive);
            for (int i = 0; i < archive.size(); i++) {
                if (closestIndex == i) {
                    continue;
                }
                int k = 0;
                double closestDistance = distanceMatrix[closestIndex][k];
                double iDistance = distanceMatrix[i][k];
                while (closestDistance == iDistance && k < size) {
                    k++;
                }
                if (closestDistance > iDistance) {
                    closestIndex = i;
                }
            }
            archive.remove(closestIndex);
        }
        return archive;
    }

    private Comparator<SPEA2Chromosome> getFitnessComparator() {
        return new Comparator<SPEA2Chromosome>() {
            public int compare(SPEA2Chromosome o1, SPEA2Chromosome o2) {
                return new Double(o1.getFitness()).compareTo(o2.getFitness());
            }
        };
    }
}
