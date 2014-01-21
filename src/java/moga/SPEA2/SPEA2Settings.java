package moga.SPEA2;

import core.moga.MOGASettings;
import core.utils.PropertiesUtil;
import operators.crossover.SBXCrossover_2;
import operators.mutation.PolynomialMutation;
import operators.selection.BinaryTournamentSelection;
import utils.DominanceComparator;

import java.io.IOException;
import java.util.Comparator;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 20:58
 */
public class SPEA2Settings extends MOGASettings<SPEA2Chromosome> {

    public SPEA2Settings() throws IOException {
        super("SPEA2");
    }

    @Override
    protected void load(PropertiesUtil propertiesUtil) {
        super.load(propertiesUtil);
        mutation = new PolynomialMutation();
        crossover = new SBXCrossover_2<SPEA2Chromosome>(SPEA2Chromosome.class);
        // TODO: should also compare by maximum distance
        selection = new BinaryTournamentSelection<SPEA2Chromosome>(new DominanceComparator<SPEA2Chromosome>());
    }
}
