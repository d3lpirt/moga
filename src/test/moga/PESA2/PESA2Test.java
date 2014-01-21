package moga.PESA2;

import core.moga.Population;
import problems.SCHProblem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 22:52
 */
public class PESA2Test {

    public static void main(String[] args) throws IOException {
        PESA2Settings spea2Settings = new PESA2Settings();
        SCHProblem problem = new SCHProblem();
        Population<PESA2Chromosome> population = new PESA2(spea2Settings, problem).execute();

        File file = new File("pesa2.txt");
        file.delete();
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        for (PESA2Chromosome chromosome : population) {
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
