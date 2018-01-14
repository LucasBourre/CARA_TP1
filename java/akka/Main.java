package java.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import java.akka.actors.Master;
import java.akka.actors.Reducer;

import java.io.File;
import java.util.Scanner;


public class Main {
	
  public static final int REDUCERS = 5;
  public static final int MAPPERS = 10;

  public static void main(String[] args) {
	  ActorSystem system = ActorSystem.create("MasterReducersSystem", ConfigFactory.load("master-reducers"));
	  ActorRef master = system.actorOf(Master.props(), "master");
		//5 actorOf
	    for (int i=0; i < REDUCERS ; i++) {
	      system.actorOf(Reducer.props(), "reducer" + i);
	    }

	    try {
			//load file The_Bible.txt
	          master.tell(new File(loader.getResource("The_Bible.txt").getFile()), ActorRef.noSender());
	          master.tell("OCCURRENCES", ActorRef.noSender());

	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      system.terminate();
	    }
  }


}
