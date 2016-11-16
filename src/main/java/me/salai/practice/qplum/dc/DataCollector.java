package me.salai.practice.qplum.dc;

import java.io.IOException;
import java.nio.file.*;

/**
 * Data Collector
 * Roles
 * 1. Watches for a file in the directory. A Blocking infinite loop thread it is. It kills itself based on the timings of stock market
 * 2. If the file is modified, calls the CSV parsing class
 * 3.The CSV parsing class, just reads record by record and updates the database. As of now, we will stick to a MySQL data store
 */
public class DataCollector {
    private final String dirPath;
    private final String fileName;

    // Once initialized, the file path cannot be modified.
    public DataCollector(String dirPath,String fileName){
        this.dirPath = dirPath;
        this.fileName = fileName;
    }

    // The Night's Watch for CSV file.
    // When the CSV get changed, this guy notifies the CSV parser
    // Like Night's watch notify it's Lord Commander
    public void watch() throws IOException {
            // Make a Path object from the dir path to watch
            final Path toWatchDir = FileSystems.getDefault().getPath(dirPath);
            // Just to debug
            System.out.println(toWatchDir);

            // Create a new Watch Service instance
            final WatchService ws = FileSystems.getDefault().newWatchService();

            // Register the watch service to watch the given dir for Create and Modify Events
            final WatchKey wk = toWatchDir.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);

            // The infinite loop begins here
            // Kills itself only if the Watch is invalid
            while(true){
                try {
                    System.out.println("-----------------------------");
                    System.out.println("Night's Watch on duty");
                    System.out.println("-----------------------------");
                    // Not sure that take gives me over here. Need to read the API docs one more time
                    final WatchKey watchKey = ws.take();
                    if (!watchKey.isValid()){
                        System.out.println(" Watcher Key is invalid");
                        break;
                    }
                    // Get all the events happened for it
                    for ( WatchEvent event : watchKey.pollEvents() ){
                        System.out.println("I'm the event occured " +event.toString());
                        // get the context 
                        final Path changedPath = (Path) event.context();
                        System.out.println("I'm the file path "+ changedPath    );
                        // Check if the file modified is our file
                        if ( changedPath.endsWith(fileName)){
                            System.out.println(" Hey Lord Commander, I have got data. Gonna call CSV Processor once he is available");
                        }
                    }
                    System.out.println("Exited the first iteration");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Should I exit when this exception occurs? May be, I don't know as of now
                    System.out.println("Exception occurred");
                }
            }
    }
}
