package ga_seatingchart;

import com.google.common.collect.Sets;
import yagalib.EvolutionManager;
import yagalib.Genome;

import java.util.*;

public class AssignmentStrategy implements Genome {

    Map<Worker, Cubicle> genes = new HashMap<Worker, Cubicle>();

    public List<Map.Entry<Worker, Cubicle>> getGenes() {
        List<Map.Entry<Worker, Cubicle>> geneList = new ArrayList<Map.Entry<Worker, Cubicle>>();
        for(Map.Entry<Worker, Cubicle> gene : genes.entrySet()) {
            geneList.add(gene);
        }
        return geneList;
    }

    public void pointMutateOneGene() {
        List<Worker> unassignedWorkers = getUnassignedWorkers();
        List<Cubicle> unassignedCubicles = getUnassignedCubicles();

        if(unassignedWorkers.isEmpty() || unassignedCubicles.isEmpty()) {
            return;
        }

        // Fetch a random assignment and change either the worker or the cubicle
        List<Map.Entry<Worker, Cubicle>> geneList = getGenes();
        if(geneList.size() == 0) {
            return;
        }
        Map.Entry<Worker, Cubicle> mutant = geneList.get(EvolutionManager.random.nextInt(geneList.size()));
        genes.remove(mutant.getKey());
        if(EvolutionManager.random.nextInt(2) == 0) {
            Worker randomWorker = unassignedWorkers.get(EvolutionManager.random.nextInt(unassignedWorkers.size()));
            genes.put(randomWorker, mutant.getValue());
        } else {
            Cubicle randomCubicle = unassignedCubicles.get(EvolutionManager.random.nextInt(unassignedCubicles.size()));
            genes.put(mutant.getKey(), randomCubicle);
        }
    }

    public Genome crossoverWith(Genome otherGenome) {
        // Create a new Genome, with half the rules from one Genome, and half from the other.
        AssignmentStrategy offspringGenome = new AssignmentStrategy();
        List<Map.Entry<Worker, Cubicle>> ourGenes = getGenes();
        if(ourGenes.size() > 0) {
            for(int i = 0; i < ourGenes.size(); i += 2) {
                offspringGenome.addGene(ourGenes.get(i));
            }
        }
        List<Map.Entry<Worker, Cubicle>> otherGenes = otherGenome.getGenes();
        if(otherGenes.size() > 0) {
            for(int i = 0; i < otherGenes.size(); i += 2) {
                offspringGenome.addGene(otherGenes.get(i));
            }
        }

        return offspringGenome;
    }

    private void addGene(Map.Entry<Worker, Cubicle> newGene) {
        if(genes.containsKey(newGene.getKey()) || genes.containsValue(newGene.getValue())) {
            return;
        }
        genes.put(newGene.getKey(), newGene.getValue());
    }

    public void complexify() {
        // Get the list of unassigned workers and cubicles from current genes
        List<Worker> unassignedWorkers = getUnassignedWorkers();
        List<Cubicle> unassignedCubicles = getUnassignedCubicles();

        if(unassignedWorkers.isEmpty() || unassignedCubicles.isEmpty()) {
            return;
        }

        Worker randomWorker = unassignedWorkers.get(EvolutionManager.random.nextInt(unassignedWorkers.size()));
        Cubicle randomCubicle = unassignedCubicles.get(EvolutionManager.random.nextInt(unassignedCubicles.size()));

        genes.put(randomWorker, randomCubicle);
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
        List<Worker> keys = new ArrayList<Worker>();
        for(Worker worker : genes.keySet()) {
            keys.add(worker);
        }
        genes.remove(keys.get(EvolutionManager.random.nextInt(keys.size())));
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
