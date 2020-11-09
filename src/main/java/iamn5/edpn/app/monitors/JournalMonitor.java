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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class JournalMonitor {
    private String lastFilename;
    private final String journalPath = System.getProperty("user.home") + "/Saved Games/Frontier Developments/Elite Dangerous/";
    private final Pattern journalPattern = Pattern.compile("^Journal.*\\.[0-9.]+\\.log$");
    private final int pollingRate = 100;
    private final int relaxedPollingRate = 5000;

    private long currentRate = relaxedPollingRate;
    private Optional<Path> lastJournal = Optional.empty();
    private long lastSize = 0;
    private Runnable pollingTask;
    private final ScheduledExecutorService scheduledExecutor;

    private ScheduledFuture<?> scheduledTask;
    private final ListenableQueue<JSONObject> eventQueue;

    public JournalMonitor(ListenableQueue<JSONObject> eventQueue) {
        this.eventQueue = eventQueue;
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    }

    public void start() {

        Logger.info("JournalMonitor started!");

        pollingTask = new Runnable() {
            @Override
            public void run() {
                if (lastFilename != null && !lastFilename.isEmpty() && Processes.isEDRunning()) {
                    currentRate = pollingRate;
                }

                try {
                    lastJournal = findLatestJournal();

                    if (!lastJournal.isPresent()) {
                        int tries = 0;

                        do {
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

                            lastSize = currentSize;

                            ReadEvents(lastJournal.get(), startPos, currentSize - startPos);
                        }
                    }

                    scheduleMonitoring();
                } catch (IOException | InterruptedException e) {
                    Logger.error("Could not get latest journal: " + e.getMessage());
                }
            }
        };

        pollingTask.run();
    }

    public void scheduleMonitoring() {
        if (pollingTask != null) {
            scheduledTask = scheduledExecutor.schedule(pollingTask, currentRate, TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    public boolean isRunning() {
        return scheduledTask != null && !scheduledTask.isDone();
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
