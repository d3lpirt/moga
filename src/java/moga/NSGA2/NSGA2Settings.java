package moga.NSGA2;

import core.moga.MOGASettings;
import core.utils.PropertiesUtil;
import operators.crossover.SBXCrossover_2;
import operators.mutation.PolynomialMutation;
import operators.selection.BinaryTournamentSelection;

import java.io.IOException;
import java.util.Comparator;

/**
 * User: Dmitry Beshkarev
 * Date: 04/12/13 Time: 19:24
 */
public class NSGA2Settings extends MOGASettings<NSGA2Chromosome> {

    public NSGA2Settings() throws IOException {
        super("NSGA2");
    }

    @Override
    protected void load(PropertiesUtil propertiesUtil) {
        super.load(propertiesUtil);
        mutation = new PolynomialMutation();
        crossover = new SBXCrossover_2<NSGA2Chromosome>(NSGA2Chromosome.class);
        selection = new BinaryTournamentSelection<NSGA2Chromosome>(getComparator());
    }

    private Comparator<NSGA2Chromosome> getComparator() {
        return new Comparator<NSGA2Chromosome>() {
            public int compare(NSGA2Chromosome o1, NSGA2Chromosome o2) {
                // The lower the rank the better
                int result = new Integer(o2.getRank()).compareTo(o1.getRank());
                return result != 0 ? result : new Double(o1.getCrowdingDistance()).compareTo(o2.getCrowdingDistance());
            }
        };
    }
}
