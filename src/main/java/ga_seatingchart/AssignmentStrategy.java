package ga_seatingchart;

import com.google.common.collect.Sets;
import yagalib.EvolutionManager;
import yagalib.Genome;

import java.util.*;

public class AssignmentStrategy implements Genome {

    Map<Worker, Cubicle> genes = new HashMap<Worker, Cubicle>();

    public AssignmentStrategy() {
        // a brand new strategy is a random assignment of all workers to cubicles
        List<Worker> workers = getUnassignedWorkers();
        List<Cubicle> unassignedCubicles = getUnassignedCubicles();
        for(Worker worker : workers) {
            Cubicle randomCubicle = unassignedCubicles.get(EvolutionManager.random.nextInt(unassignedCubicles.size()));
            unassignedCubicles.remove(randomCubicle);
            genes.put(worker, randomCubicle);
        }
    }

    public List<Map.Entry<Worker, Cubicle>> getGenes() {
        List<Map.Entry<Worker, Cubicle>> geneList = new ArrayList<Map.Entry<Worker, Cubicle>>();
        for(Map.Entry<Worker, Cubicle> gene : genes.entrySet()) {
            geneList.add(gene);
        }
        return geneList;
    }

    // If the number of cubicles == the number of workers, then a point mutation amounts to swapping two workers.
    // If the number of cubicles > the number of workers, then a point mutation reassigns a random worker to an empty
    // cubicle.
    // We ignore the case where number of cubicles < the number of workers, as we assume we have enough space for
    // everyone.  If not, there are no acceptable solutions, anyways.
    public void pointMutateOneGene() {
        List<Map.Entry<Worker, Cubicle>> geneList = getGenes();
        if(geneList.size() == 0) {
            return;
        }

        List<Cubicle> unassignedCubicles = getUnassignedCubicles();

        // cubicles == workers
        if(unassignedCubicles.isEmpty()) {
            // Get 2 random assignments and swap their values
            Map.Entry<Worker, Cubicle> mutant1 = geneList.get(EvolutionManager.random.nextInt(geneList.size()));
            Map.Entry<Worker, Cubicle> mutant2 = geneList.get(EvolutionManager.random.nextInt(geneList.size()));
            genes.remove(mutant1.getKey());
            genes.remove(mutant2.getKey());
            genes.put(mutant1.getKey(), mutant2.getValue());
            genes.put(mutant2.getKey(), mutant1.getValue());
        } else {
            // cubicles > workers
            // reassign a random worker to a random unassigned cubicle
            Map.Entry<Worker, Cubicle> mutant = geneList.get(EvolutionManager.random.nextInt(geneList.size()));
            genes.remove(mutant.getKey());
            genes.put(mutant.getKey(),
                    unassignedCubicles.get(EvolutionManager.random.nextInt(unassignedCubicles.size())));
        }
    }

    public Genome crossoverWith(Genome otherGenome) {
        // Create a new Genome, with half the assignments from one Genome, and half from the other.
        AssignmentStrategy offspringGenome = new AssignmentStrategy();
        List<Map.Entry<Worker, Cubicle>> otherGenes = otherGenome.getGenes();
        // Build an actual map for the other genome.  This is gross.
        Map<Worker, Cubicle> otherGenesMap = new HashMap<Worker, Cubicle>();
        for(Map.Entry<Worker, Cubicle> otherGene : otherGenes) {
            otherGenesMap.put(otherGene.getKey(), otherGene.getValue());
        }

        int i = 0;
        for(Worker worker : Office.allWorkers) {
            if(i % 2 == 0) {
                offspringGenome.genes.put(worker, genes.get(worker));
            } else {
                offspringGenome.genes.put(worker, otherGenesMap.get(worker));
            }
        }

        return offspringGenome;
    }


    public void complexify() {
        // This is a no-op
    }

    private List<Cubicle> getUnassignedCubicles() {
        Set<Cubicle> allCubicles = new HashSet<Cubicle>(Office.allCubicles);
        Set<Cubicle> assignedCubicles = new HashSet<Cubicle>(genes.values());
        List<Cubicle> unassignedCubicles = new ArrayList<Cubicle>(Sets.difference(allCubicles, assignedCubicles));
        return unassignedCubicles;
    }

    private List<Worker> getUnassignedWorkers() {
        Set<Worker> allWorkers = new HashSet<Worker>(Office.allWorkers);
        Set<Worker> assignedWorkers = genes.keySet();
        List<Worker> unassignedWorkers = new ArrayList<Worker>(Sets.difference(allWorkers, assignedWorkers));
        return unassignedWorkers;
    }

    public void simplify() {
        // This is a no-op
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for(Map.Entry<Worker, Cubicle> entry : genes.entrySet()) {
            sb.append("\tWorker: " + entry.getKey().getName());
            sb.append(" : ");
            sb.append("Cubicle: " + entry.getValue().getName());
            sb.append("\n");
        }
        return sb.toString();
    }
}
