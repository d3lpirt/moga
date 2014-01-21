package moga.NSGA2;

import core.moga.Population;
import problems.SCHProblem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * NSGA2Test<br/>
 *
 * @author <a href="mailto:dmitriy.beshkarev@gmail.com">Dmitry Beshkarev</a>
 *         Created: 12/4/13 11:24 PM
 */
public class NSGA2Test {

    public static void main(String[] args) throws IOException {
        NSGA2Settings nsga2Settings = new NSGA2Settings();
        SCHProblem problem = new SCHProblem();
        Population<NSGA2Chromosome> population = new NSGA2(nsga2Settings, problem).execute();

        File file = new File("nsga2.txt");
        file.delete();
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        for (NSGA2Chromosome chromosome : population) {
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
