package ga_seatingchart;

import yagalib.Organism;

import java.util.*;

public class SeatingChart implements Organism<AssignmentStrategy> {

    private int sumOfWeights = 0;
    private AssignmentStrategy strategy = new AssignmentStrategy();

    public Map<Worker, Cubicle> assignWorkersToCubicles() {
        Map<Worker, Cubicle> assignments = new HashMap<Worker, Cubicle>();
        
        // For each assignment in this organism's strategy, add it to the result
        Set<Worker> unassignedWorkers = new HashSet<Worker>(Office.allWorkers);
        Set<Cubicle> unassignedCubicles = new HashSet<Cubicle>(Office.allCubicles);
        for(Map.Entry<Worker, Cubicle> gene : strategy.genes.entrySet()) {
            assignments.put(gene.getKey(), gene.getValue());
            unassignedWorkers.remove(gene.getKey());
            unassignedCubicles.remove(gene.getValue());
        }

        // Then, for each worker not assigned, assign them to a random, empty cubicle
        int i = 0;
        Object[] cubicles = unassignedCubicles.toArray();
        for(Worker unassignedWorker : unassignedWorkers) {
            assignments.put(unassignedWorker, (Cubicle)cubicles[i++]);
        }

        return assignments;
    }

    public void setSumOfWeights(int sumOfWeights) {
        this.sumOfWeights = sumOfWeights;
    }

    public void setStrategy(AssignmentStrategy strategy) {
        this.strategy = strategy;
    }

    public Integer getFitness() {
        return sumOfWeights * -1;
    }

    public void resetFitness() {
        sumOfWeights = 0;
    }

    public AssignmentStrategy getGenome() {
        return strategy;
    }

    public void pointMutate() {
        strategy.pointMutateOneGene();
    }

    public SeatingChart reproduceWith(Organism otherOrganism) {
        SeatingChart offspring = new SeatingChart();
        AssignmentStrategy newStrategy = (AssignmentStrategy)strategy.crossoverWith(otherOrganism.getGenome());
        offspring.setStrategy(newStrategy);
        return offspring;
    }

    public void complexifyGenome() {
        strategy.complexify();
    }

    public void simplifyGenome() {
        strategy.simplify();
    }
}
