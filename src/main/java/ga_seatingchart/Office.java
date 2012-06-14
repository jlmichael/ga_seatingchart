package ga_seatingchart;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import yagalib.Environment;
import yagalib.EvolutionManager;

import java.util.*;

public class Office implements Environment<SeatingChart> {

    private List<SeatingChart> seatingCharts = new ArrayList<SeatingChart>();
    private static Map<Cubicle, Map<Cubicle, Integer>> cubicleDistances = new HashMap<Cubicle, Map<Cubicle, Integer>>();
    private static Map<Worker, Map<Worker, Integer>> workerRelationships = new HashMap<Worker, Map<Worker, Integer>>();
    private Map<SeatingChart, Map<Worker, Cubicle>> assignments = new HashMap<SeatingChart, Map<Worker, Cubicle>>();
    public static List<Worker> allWorkers;
    public static List<Cubicle> allCubicles;
    private static Logger logger = Logger.getLogger(Office.class);

    static {
        Yaml yaml = new Yaml();
        cubicleDistances = (Map<Cubicle, Map<Cubicle, Integer>>)yaml.load(Office.class.getClassLoader()
                .getResourceAsStream("cubedistances.yaml"));
        workerRelationships = (Map<Worker, Map<Worker, Integer>>)yaml.load(Office.class.getClassLoader()
                .getResourceAsStream("workerrelationships.yaml"));
        allWorkers = new ArrayList<Worker>(workerRelationships.keySet());
        allCubicles = new ArrayList<Cubicle>(cubicleDistances.keySet());
    }

    public static Worker getRandomWorker() {
        List<Worker> workers = new ArrayList<Worker>();
        workers.addAll(workerRelationships.keySet());
        return workers.get(EvolutionManager.random.nextInt(workers.size()));
    }

    public static Cubicle getRandomCubicle() {
        List<Cubicle> cubicles = new ArrayList<Cubicle>();
        cubicles.addAll(cubicleDistances.keySet());
        return cubicles.get(EvolutionManager.random.nextInt(cubicles.size()));
    }
    
    public void populateWithOrganisms(Integer population) {
        for(int i = 0; i < population; i++) {
            seatingCharts.add(new SeatingChart());
        }
    }

    public void reset() {
        assignments.clear();
    }

    public List<SeatingChart> getOrganisms() {
        return seatingCharts;
    }

    public void clearOrganisms() {
        seatingCharts.clear();
    }

    public void doWorkOnOrganisms() {
        // For each organism, ask them to assign each worker to a cube.
        for(SeatingChart seatingChart : seatingCharts) {
            Map<Worker, Cubicle> assignment = seatingChart.assignWorkersToCubicles();
            assignments.put(seatingChart, assignment);
        }

        // Score each chart
        for(Map.Entry<SeatingChart, Map<Worker, Cubicle>> assignment : assignments.entrySet()) {
            SeatingChart chart = assignment.getKey();
            Map<Worker, Cubicle> workersToCubicles = assignment.getValue();

            // Sum up the edge weights - for each worker in the chart, get the relationships for that worker and
            // multiply the importance by the cubicle distance
            Integer totalWeight = 0;
            for(Map.Entry<Worker, Cubicle> entry : workersToCubicles.entrySet()) {
                Worker firstWorker = entry.getKey();
                Map<Worker, Integer> relationships = workerRelationships.get(firstWorker);
                if(relationships != null) {
                    for(Map.Entry<Worker, Integer> relationship : relationships.entrySet()) {
                        Worker secondWorker = relationship.getKey();
                        Cubicle firstCubicle = entry.getValue();
                        Cubicle secondCubicle = workersToCubicles.get(secondWorker);

                        Integer distance = cubicleDistances.get(firstCubicle).get(secondCubicle);
                        if(distance == null) {
                            logger.fatal("Got back null distance between " + firstCubicle.getName() + " and " + secondCubicle.getName());
                        }
                        Integer importance = relationship.getValue() + workerRelationships.get(secondWorker).get(firstWorker) / 2;
                        totalWeight += distance * importance; 
                    }
                }
            }

            chart.setSumOfWeights(totalWeight);
        }
    }
}
