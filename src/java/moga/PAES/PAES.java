package moga.PAES;

import core.moga.AdaptiveGridArchive;
import core.moga.MOGA;
import core.moga.Population;
import core.moga.Problem;
import core.moga.operators.Mutation;
import core.utils.PopulationGenerator;
import utils.DominanceComparator;

import java.io.IOException;

/**
 * PAES<br/>
 *
 * @author <a href="mailto:dmitry.beshkarev@ctco.lv">Dmitry Beshkarev</a>
 * @version 1.0 date: 12/11/13 time: 4:39 PM
 */
public class PAES extends MOGA<PAESChromosome, PAESSettings> {

    public PAES(PAESSettings settings, Problem problem) throws IOException {
        super(settings, problem);
    }

    @Override
    public Population<PAESChromosome> execute() {

        Problem problem = getProblem();
        int numberOfBisections = getSettings().getNumberOfBisections();
        int archiveSize = getSettings().getArchiveSize();
        int maxEvaluations = getSettings().getNumberOfGenerations();

        Mutation mutation = getSettings().getMutation();

        DominanceComparator<PAESChromosome> dominance = new DominanceComparator<PAESChromosome>();
        AdaptiveGridArchive<PAESChromosome> archive = new AdaptiveGridArchive<PAESChromosome>(archiveSize, numberOfBisections, problem.getNumberOfObjectives());

        PAESChromosome chromosome = PopulationGenerator.generatePopulation(PAESChromosome.class, 1, problem).get(0);
        problem.evaluate(chromosome);
        archive.add(new PAESChromosome(chromosome));

        int currentGeneration = 0;
        do {
            PAESChromosome mutatedIndividual = new PAESChromosome(chromosome);
            mutation.execute(mutatedIndividual, problem);

            problem.evaluate(mutatedIndividual);
            int compareResult = dominance.compare(chromosome, mutatedIndividual);

            if (compareResult < 0) {
                chromosome = new PAESChromosome(mutatedIndividual);
                archive.add(mutatedIndividual);
            } else if (compareResult == 0) {
                if (archive.add(mutatedIndividual)) {
                    chromosome = test(chromosome, mutatedIndividual, archive);
                }
            }
            currentGeneration++;
        } while (currentGeneration < maxEvaluations);

        return archive;
    }

    public PAESChromosome test(PAESChromosome chromosome, PAESChromosome mutatedPAESChromosome, AdaptiveGridArchive<PAESChromosome> archive) {
        int originalLocation = archive.getGrid().location(chromosome);
        int mutatedLocation = archive.getGrid().location(mutatedPAESChromosome);

        if (originalLocation == -1) {
            return new PAESChromosome(mutatedPAESChromosome);
        }

        if (mutatedLocation == -1) {
            return new PAESChromosome(chromosome);
        }

        if (archive.getGrid().getLocationDensity(mutatedLocation) < archive.getGrid().getLocationDensity(originalLocation)) {
            return new PAESChromosome(mutatedPAESChromosome);
        }

        return new PAESChromosome(chromosome);
    }
}
