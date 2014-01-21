package moga.PAES;

import core.moga.Chromosome;

/**
 * PAESChromosome<br/>
 *
 * @author <a href="mailto:dmitry.beshkarev@ctco.lv">Dmitry Beshkarev</a>
 * @version 1.0 date: 12/11/13 time: 4:39 PM
 */
public class PAESChromosome extends Chromosome {

    public PAESChromosome(PAESChromosome paesChromosome) {
        super(paesChromosome);
    }

    public PAESChromosome(Integer numberOfObjectives, Integer numberOfVariables) {
        super(numberOfObjectives, numberOfVariables);
    }
}
