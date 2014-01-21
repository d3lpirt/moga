package moga.PAES;

import core.moga.Population;
import problems.SCHProblem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * PAESTest<br/>
 *
 * @author <a href="mailto:dmitry.beshkarev@ctco.lv">Dmitry Beshkarev</a>
 * @version 1.0 date: 12/11/13 time: 4:50 PM
 */
public class PAESTest {

    public static void main(String[] args) throws IOException {
        PAESSettings spea2Settings = new PAESSettings();
        SCHProblem problem = new SCHProblem();
        Population<PAESChromosome> population = new PAES(spea2Settings, problem).execute();

        File file = new File("paes.txt");
        file.delete();
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        for (PAESChromosome chromosome : population) {
            for (Double var : chromosome.getVariables()) {
                out.write(String.format("%-15.5f", var));
            }
            out.write("|| ");
            for (Double var : chromosome.getObjectives()) {
                out.write(String.format("%-15.5f", var));
            }
            out.write("\n");
        }
        out.close();
    }
}
