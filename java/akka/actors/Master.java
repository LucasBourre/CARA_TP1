package java.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import java.akka.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Master extends AbstractActor {

    private Master() { }

    static public Props props() {
        return Props.create(Master.class, () -> new Master());
    }

    private void distributeRowsToMappers(File file) {
        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                getContext().actorSelection(Main.MAPPERS_SYSTEM_PATH + "/user/mapper" + i% Main.NB_MAPPERS).tell(line.toUpperCase(), getSelf());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callReducersToprintOccurencesByWord() {
        for (int i=0; i < Main.NB_REDUCERS; i++) {
            getContext().actorSelection(Main.MASTER_REDUCERS_SYSTEM_PATH + "/user/reducer" + i%Main.NB_REDUCERS).tell("PRINT_OCCURRENCES_BY_WORD", getSelf());
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("PRINT_OCCURRENCES_BY_WORD", line -> callReducersToprintOccurencesByWord())
                .match(File.class, this::distributeRowsToMappers)
                .build();
    }
}