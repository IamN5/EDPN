package iamn5.edpn.app.monitors;

import iamn5.edpn.app.utils.Processes;
import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.core.ListenableQueue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class JournalMonitor {
    private boolean running;
    private String lastFilename;
    private final String journalPath = System.getProperty("user.home") + "/Saved Games/Frontier Developments/Elite Dangerous/";
    private final Pattern journalPattern = Pattern.compile("^Journal.*\\.[0-9.]+\\.log$");
    private final int pollingRate = 100;
    private final int relaxedPollingRate = 5000;
    private final ListenableQueue<JSONObject> eventQueue;

    public JournalMonitor(ListenableQueue<JSONObject> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void start() throws InterruptedException {
        long lastSize = 0;
        int rate = relaxedPollingRate;

        Logger.info("JournalMonitor started!");

        running = true;
        while (running) {

            if (lastFilename != null && !lastFilename.isEmpty() && Processes.isEDRunning()) {
                rate = pollingRate;
            }


            try {
                Optional<Path> lastJournal = findLatestJournal();

                if (!lastJournal.isPresent()) {
                    int tries = 0;

                    Logger.error("Could not find a journal. Have you ever executed Elite Dangerous?");
                    do {
                        //TODO Instead of sleeping, use ScheduledExecutorService
                        Logger.info("Fetching journal... (" + ++tries + ")");
                        Thread.sleep(1000);
                        lastJournal = findLatestJournal();

                    } while (!lastJournal.isPresent());
                }


                String newFilename = lastJournal.get().getFileName().toString();
                long currentSize = Files.size(lastJournal.get());

                // Reads essential stuff for our application to work, ignoring all rest.
                if (lastFilename == null || !lastFilename.equals(newFilename)) {
                    lastFilename = newFilename;
                    lastSize = currentSize;

                    ReadCommanderEvent(lastJournal.get(), lastSize);
                } else {

                    if (currentSize != lastSize) {
                        long startPos = 0;

                        // File has been appended
                        if (currentSize > lastSize) {
                            startPos = lastSize;
                        }

                        ReadEvents(lastJournal.get(), startPos, currentSize - startPos);
                    }
                }


            } catch (IOException e) {
                Logger.error("Could not get latest journal: " + e.getMessage());
            }

            Thread.sleep(rate);
        }
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private void ReadCommanderEvent(Path filePath, long readSize) {
        Logger.info("Reading commander");
        try {
            RandomAccessFile file = new RandomAccessFile(filePath.toString(), "r");
            JSONArray events = new JSONArray();

            while (file.getFilePointer() < readSize) {
                JSONObject lineObject = new JSONObject(file.readLine());

                events.put(lineObject);
            }

            Optional<Object> cmdrEvent = events.toList().stream().filter(item -> {
                if (item instanceof HashMap) {
                    return ((HashMap) item).get("event").equals("Commander");
                }
                return false;
            }).findFirst();


            if (cmdrEvent.isPresent()) {
                JSONObject obj = new JSONObject((HashMap) cmdrEvent.get());

                eventQueue.offer(obj);
            }

        } catch (IOException e) {
            Logger.error("Error while reading Commander: " + e.getMessage());
        }
    }

    private void ReadEvents(Path filePath, long readFrom, long readSize) {
        Logger.info("Reading events");

        try {
            RandomAccessFile file = new RandomAccessFile(filePath.toString(), "r");
            JSONArray events = new JSONArray();

            while (file.getFilePointer() < readFrom + readSize) {
                JSONObject lineObject = new JSONObject(file.readLine());
                events.put(lineObject);
            }

            events.forEach(item -> eventQueue.offer((JSONObject) item));

        } catch (IOException e) {
            Logger.error("Error while reading events: " + e.getMessage());
        }
    }

    private Optional<Path> findLatestJournal() throws IOException {

        return Files.list(Paths.get(journalPath))
                .filter(f -> !Files.isDirectory(f) && journalPattern.matcher(f.getFileName().toString()).find())
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()));
    }
}
