package java.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;

//Reducer actor to count occurences for each word
public class Reducer extends AbstractActor {

    private HashMap<String, Integer> occurences;

    static public Props props() {
        return Props.create(Reducer.class, Reducer::new);
    }

    private Reducer() {
        this.occurences = new HashMap<>();
    }


    private void computeOccurences(String word) {
        if (!this.occurences.containsKey(word)) {
            occurences.put(word, 1);
        } else {
            occurences.put(word, occurences.get(word) + 1);
        }
    }


    private void printOccurences() {
        for (Map.Entry<String, Integer> entry : occurences.entrySet()) {
            System.out.println(getSelf() + ": " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("OCCURRENCES", line -> printOccurences()).match(String.class, line -> computeOccurences(line)).build();
    }
}
