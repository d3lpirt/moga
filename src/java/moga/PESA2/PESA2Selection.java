package moga.PESA2;

import core.moga.AdaptiveGridArchive;
import core.moga.Population;
import core.moga.operators.Selection;
import core.utils.RandomGenerator;

import java.util.Comparator;
import java.util.HashMap;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 22:32
 */
public class PESA2Selection extends Selection<PESA2Chromosome> {

    public PESA2Selection() {
        super(null);
    }

    @Override
    public PESA2Chromosome execute(Population<PESA2Chromosome> population) {
        RandomGenerator randomGenerator = new RandomGenerator();

        AdaptiveGridArchive<PESA2Chromosome> archive = (AdaptiveGridArchive<PESA2Chromosome>) population;
        int selected;
        int hypercube1 = archive.getGrid().randomOccupiedHypercube();
        int hypercube2 = archive.getGrid().randomOccupiedHypercube();

        // TODO: if hypercube == hypercube2 then reselect hypercube2
        if (hypercube1 != hypercube2) {
            if (archive.getGrid().getLocationDensity(hypercube1) < archive.getGrid().getLocationDensity(hypercube2)) {
                selected = hypercube1;
            } else if (archive.getGrid().getLocationDensity(hypercube2) < archive.getGrid().getLocationDensity(hypercube1)) {
                selected = hypercube2;
            } else {
                if (randomGenerator.nextDouble() < 0.5) {
                    selected = hypercube2;
                } else {
                    selected = hypercube1;
                }
            }
        } else {
            selected = hypercube1;
        }

        int base = randomGenerator.nextInt(0, archive.size() - 1);
        int cnt = 0;
        while (cnt < archive.size()) {
            PESA2Chromosome chromosome = archive.get((base + cnt) % archive.size());
            if (archive.getGrid().location(chromosome) != selected) {
                cnt++;
            } else {
                return chromosome;
            }
        }
        return archive.get((base + cnt) % archive.size());
    }
}
