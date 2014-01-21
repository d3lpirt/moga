package moga.PESA2;

import core.moga.AdaptiveGridArchive;
import core.moga.MOGA;
import core.moga.Population;
import core.moga.Problem;
import core.moga.operators.Crossover;
import core.moga.operators.Mutation;
import core.moga.operators.Selection;
import core.utils.PopulationGenerator;

import java.io.IOException;
import java.util.List;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 20:46
 */
public class PESA2 extends MOGA<PESA2Chromosome, PESA2Settings> {

    public PESA2(PESA2Settings settings, Problem problem) throws IOException {
        super(settings, problem);
    }

    @Override
    public Population<PESA2Chromosome> execute() {
        Problem problem = getProblem();
        int populationSize = getSettings().getPopulationSize();
        int archiveSize = getSettings().getArchiveSize();
        int numberOfBisections = getSettings().getNumberOfBisections();
        int numberOfGenerations = getSettings().getNumberOfGenerations();

        Population<PESA2Chromosome> population = PopulationGenerator.generatePopulation(PESA2Chromosome.class, populationSize, problem);
        AdaptiveGridArchive<PESA2Chromosome> archive = new AdaptiveGridArchive<PESA2Chromosome>(archiveSize, numberOfBisections, problem.getNumberOfObjectives());

        for (PESA2Chromosome chromosome : population) {
            archive.add(chromosome);
        }

        Selection<PESA2Chromosome> selection = getSettings().getSelection();
        Crossover<PESA2Chromosome> crossover = getSettings().getCrossover();
        Mutation mutation = getSettings().getMutation();

        population.clear();

        int currentGeneration = 0;
        do {
            while (population.size() < populationSize) {
                PESA2Chromosome parent1 = selection.execute(archive);
                PESA2Chromosome parent2 = selection.execute(archive);

                List<PESA2Chromosome> children = crossover.execute(parent1, parent2, problem);
                PESA2Chromosome child1 = children.get(0);
                //PESA2Chromosome child2 = children.get(1);

                mutation.execute(child1, problem);
                problem.evaluate(child1);
                population.add(child1);
            }
            for (PESA2Chromosome chromosome : population) {
                archive.add(chromosome);
            }

            population.clear();
            currentGeneration++;
        } while (currentGeneration < numberOfGenerations);
        return archive;
    }
}
