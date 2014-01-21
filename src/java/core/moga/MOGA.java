package core.moga;

import java.io.IOException;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 19:51
 */
public abstract class MOGA<C extends Chromosome, S extends MOGASettings> {

    private Problem problem;
    private S settings;

    protected MOGA(S settings, Problem problem) throws IOException {
        this.problem = problem;
        this.settings = settings;
    }

    public abstract Population<C> execute();

    public Problem getProblem() {
        return problem;
    }

    public S getSettings() {
        return settings;
    }
}
