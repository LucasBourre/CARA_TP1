package java.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import java.akka.actors.Mapper;
import java.akka.actors.Master;
import java.akka.actors.Reducer;

import java.io.File;
import java.util.Scanner;


public class Main {

  public static final int NB_MAPPERS = 10;
  public static final int NB_REDUCERS = 5;
  public static final String MASTER_REDUCERS_SYSTEM_PATH = "akka.tcp://MasterReducersSystem@127.0.0.1:3001";

  private static final ClassLoader loader = Main.class.getClassLoader();


  public static void main(String[] args) {
	  ActorSystem system = ActorSystem.create("MasterReducersSystem", ConfigFactory.load("master-reducers"));

	    ActorRef master = system.actorOf(Master.props(), "master");

	    for (int i=0; i < NB_REDUCERS ; i++) {
	      system.actorOf(Reducer.props(), "reducer" + i);
	    }

	    try {

	          master.tell(new File(loader.getResource("The_Bible.txt").getFile()), ActorRef.noSender());
	          master.tell("PRINT_OCCURRENCES_BY_WORD", ActorRef.noSender());

	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      system.terminate();
	    }
  }


}