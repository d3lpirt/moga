package moga.PAES;

import core.moga.MOGASettings;
import core.utils.PropertiesUtil;
import operators.crossover.SBXCrossover_2;
import operators.mutation.PolynomialMutation;

import java.io.IOException;

/**
 * PAESSettings<br/>
 *
 * @author <a href="mailto:dmitry.beshkarev@ctco.lv">Dmitry Beshkarev</a>
 * @version 1.0 date: 12/11/13 time: 4:40 PM
 */
public class PAESSettings extends MOGASettings<PAESChromosome> {

    public static final String ARCHIVE_SIZE = "archiveSize";
    public static final String NUMBER_OF_BISECTIONS = "numberOfBisections";
    
    private int archiveSize;
    private int numberOfBisections;

    public PAESSettings() throws IOException {
        super("PAES");
    }

    @Override
    protected void load(PropertiesUtil propertiesUtil) {
        super.load(propertiesUtil);
        this.archiveSize = propertiesUtil.getIntProperty(ARCHIVE_SIZE);
        this.numberOfBisections = propertiesUtil.getIntProperty(NUMBER_OF_BISECTIONS);
        this.crossover = new SBXCrossover_2<PAESChromosome>(PAESChromosome.class);
        this.mutation = new PolynomialMutation();
    }

    public int getArchiveSize() {
        return archiveSize;
    }

    public int getNumberOfBisections() {
        return numberOfBisections;
    }
}
