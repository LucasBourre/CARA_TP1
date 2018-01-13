package java.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;


public class Reducer extends AbstractActor {

    private HashMap<String, Integer> occurencesByWord;

    static public Props props() {
        return Props.create(Reducer.class, Reducer::new);
    }

    private Reducer() {
        this.occurencesByWord = new HashMap<>();
    }


    private void computeOccurencesByWord(String word) {
        if (!this.occurencesByWord.containsKey(word)) {
            occurencesByWord.put(word, 1);
        } else {
            occurencesByWord.put(word, occurencesByWord.get(word) + 1);
        }
    }


    private void printOccurencesByWord() {
        for (Map.Entry<String, Integer> entry : occurencesByWord.entrySet()) {
            System.out.println(getSelf() + ": " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("PRINT_OCCURRENCES_BY_WORD", line -> printOccurencesByWord())
                .match(String.class, line -> computeOccurencesByWord(line))
                .build();
    }
}