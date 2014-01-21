package core.moga;

import utils.DominanceComparator;

import java.util.Iterator;

/**
 * User: Dmitry Beshkarev
 * Date: 08/12/13 Time: 22:17
 */
public class AdaptiveGridArchive<T extends Chromosome> extends Population<T> {

    private int archiveSize;

    private AdaptiveGrid<T> grid;

    public AdaptiveGridArchive(int archiveSize, int bisections, int objectives) {
        super(archiveSize);
        this.archiveSize = archiveSize;
        this.grid = new AdaptiveGrid<T>(bisections, objectives);
    }

    public boolean add(T newChromosome) {
        Iterator<T> iterator = iterator();

        DominanceComparator<T> dominanceComparator = new DominanceComparator<T>();
        while (iterator.hasNext()) {
            T chromosome = iterator.next();
            int flag = dominanceComparator.compare(newChromosome, chromosome);
            if (flag > 0) {
                iterator.remove();
                int location = grid.location(chromosome);
                if (grid.getLocationDensity(location) > 1) {
                    grid.removeSolution(location);
                } else {
                    grid.updateGrid(this);
                }
            } else if (flag < 0) {
                return false;
            }
        }

        if (size() == 0) {
            super.add(newChromosome);
            grid.updateGrid(this);
            return true;
        }

        if (size() < archiveSize) {
            grid.updateGrid(newChromosome, this);
            int location = grid.location(newChromosome);
            grid.addSolution(location);
            super.add(newChromosome);
            return true;
        }

        grid.updateGrid(newChromosome, this);
        int location = grid.location(newChromosome);
        if (location == grid.getMostPopulated()) {
            return false;
        } else {
            iterator = iterator();
            while (iterator.hasNext()) {
                T nextChromosome = iterator.next();
                int nextLocation = grid.location(nextChromosome);
                if (nextLocation == grid.getMostPopulated()) {
                    iterator.remove();
                    grid.removeSolution(nextLocation);
                    break;
                }
            }
            grid.addSolution(location);
            super.add(newChromosome);
        }
        return true;
    }

    public AdaptiveGrid<T> getGrid() {
        return grid;
    }
}
