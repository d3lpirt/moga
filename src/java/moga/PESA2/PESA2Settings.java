package moga.PESA2;

import core.moga.MOGASettings;
import core.utils.PropertiesUtil;
import operators.crossover.SBXCrossover_2;
import operators.mutation.PolynomialMutation;

import java.io.IOException;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 20:46
 */
public class PESA2Settings extends MOGASettings<PESA2Chromosome> {

    public static final String ARCHIVE_SIZE = "archiveSize";
    public static final String NUMBER_OF_BISECTIONS = "numberOfBisections";

    private int archiveSize;
    private int numberOfBisections;

    public PESA2Settings() throws IOException {
        super("PESA2");
    }

    @Override
    protected void load(PropertiesUtil propertiesUtil) {
        super.load(propertiesUtil);
        this.archiveSize = propertiesUtil.getIntProperty(ARCHIVE_SIZE);
        this.numberOfBisections = propertiesUtil.getIntProperty(NUMBER_OF_BISECTIONS);
        this.selection = new PESA2Selection();
        this.crossover = new SBXCrossover_2<PESA2Chromosome>(PESA2Chromosome.class);
        this.mutation = new PolynomialMutation();
    }

    public int getArchiveSize() {
        return archiveSize;
    }

    public int getNumberOfBisections() {
        return numberOfBisections;
    }
}
