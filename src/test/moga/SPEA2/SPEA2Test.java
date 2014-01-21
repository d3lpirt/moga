package moga.SPEA2;

import core.moga.Population;
import problems.SCHProblem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Dmitry Beshkarev
 * Date: 01/12/13 Time: 20:19
 */
public class SPEA2Test {

    public static void main(String[] args) throws IOException {
        SPEA2Settings spea2Settings = new SPEA2Settings();
        SCHProblem problem = new SCHProblem();
        Population<SPEA2Chromosome> population = new SPEA2(spea2Settings, problem).execute();

        File file = new File("spea2.txt");
        file.delete();
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        for (SPEA2Chromosome chromosome : population) {
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
