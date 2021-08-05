package de.melody.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import de.melody.ConsoleLogger;
public class QueueFunktion {
	private static List<String> testlist = new ArrayList<>();
	private static List<String> othertestlist = new ArrayList<>();
	public static void startTestThread(){
		new Thread(() -> {
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ((line = reader.readLine()) != null) {
					String[] commargs = line.split(" ");
					if (commargs[0].equalsIgnoreCase("a")) {
						testlist.add(commargs[1]);
					} else if (commargs[0].equalsIgnoreCase("n")) {
						next(Integer.valueOf(commargs[1]));
					} else if (commargs[0].equalsIgnoreCase("bi")) {
						for (String test : othertestlist) {
							ConsoleLogger.debug("Played Info", test);
						}
					} else if (commargs[0].equalsIgnoreCase("ni")) {
						for (String test : testlist) {
							ConsoleLogger.debug("WillPlay Info", test);
						}
					} else if (commargs[0].equalsIgnoreCase("ci")) {
						ConsoleLogger.debug("Play Info", testlist.get(0));
					} else if (commargs[0].equalsIgnoreCase("b")) {
						back(Integer.valueOf(commargs[1]));
					}
				}
			} catch (IOException e) {
			}
		}).start();
	}

	private static int next(int amount) {
		ConsoleLogger.debug("Queue Next Funktion", 1);
		if(amount < 0) amount *= -1;
		if(amount == 0) amount++;
		ConsoleLogger.debug("Next Amount", amount);
		ConsoleLogger.debug("Queuelist Next amount", testlist.size());
		if (amount > testlist.size()) {
			ConsoleLogger.debug("Queue Next Funktion", 2);
			amount = testlist.size();
			ConsoleLogger.debug("Next Amount", amount);
		}
		if (!testlist.isEmpty()) {
			ConsoleLogger.debug("Queue Next Funktion", 3);
			for (int i = 0; i < amount;) {
				++i;
				ConsoleLogger.debug("Next For each", i);
				ConsoleLogger.debug("Queue Next Funktion", 4);
				String qt = testlist.remove(0);
				othertestlist.add(qt);
			}
			ConsoleLogger.debug("Queue Next Funktion", 5);
			if (testlist.get(0) != null) {
				ConsoleLogger.debug("Queue Next Funktion Succes", 6);
				// play song

				return amount;
			}
			ConsoleLogger.debug("Queue Next Funktion", 7);
		}
		return 0;
	}
	private static boolean back(int amount) {
		ConsoleLogger.debug("Queue Back Funktion", 1);
		if (amount < 0) {
			amount *= -1;
		} 
		if(amount == 0) {
			amount++;
		}
		ConsoleLogger.debug("Back Amount", amount);
		ConsoleLogger.debug("Queuelist Back amount", othertestlist.size());
		if(amount > othertestlist.size()) {
			ConsoleLogger.debug("Queue Back Funktion", 2);
			amount = othertestlist.size();
			ConsoleLogger.debug("Back Amount", amount);
		}
		if (!othertestlist.isEmpty()) {
			ConsoleLogger.debug("Queue Back Funktion", 3);
			for (int i = 0; i < amount;) {
				++i;
				ConsoleLogger.debug("Back For each", i);
				ConsoleLogger.debug("Queue Back Funktion", 4);
				String qt = othertestlist.remove(othertestlist.size()-1);
				testlist.add(qt);
			}
			ConsoleLogger.debug("Queue Back Funktion", 5);
			if (!othertestlist.isEmpty() && othertestlist.get(othertestlist.size()-1) != null) {
				ConsoleLogger.debug("Queue Back Funktion Succes", 6);
				// play song

				return true;
			}
			ConsoleLogger.debug("Queue Back Funktion", 7);
		}
		return false;
	}
	
	
}
