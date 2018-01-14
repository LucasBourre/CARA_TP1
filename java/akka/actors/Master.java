package java.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.akka.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


//Line distribution with Master class
public class Master extends AbstractActor {

    private Master() { }

    static public Props props() {
        return Props.create(Master.class, () -> new Master());
    }

	//Distribution
    private void distributeRowsToMappers(File file) {
        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                getContext().actorSelection("/user/mapper" + i% Main.MAPPERS).tell(line.toUpperCase(), getSelf());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callReducersToprintOccurencesByWord() {
        for (int i=0; i < Main.REDUCERS; i++) {
            getContext().actorSelection("/user/reducer" + i%Main.REDUCERS).tell("OCCURRENCES", getSelf());
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("OCCURRENCES", line -> callReducersToprintOccurencesByWord()).match(File.class, this::distributeRowsToMappers).build();
    }
}
