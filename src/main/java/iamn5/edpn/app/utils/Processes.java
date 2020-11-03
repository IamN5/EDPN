package iamn5.edpn.app.utils;

import iamn5.edpn.framework.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Processes {

    public static boolean isEDRunning() {
        try {
            return isProcessRunning("EliteDangerous");
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }

        return false;
    }

    public static boolean isProcessRunning(String processName) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
        Process process = processBuilder.start();
        String taskList = toString(process.getInputStream());

        return taskList.contains(processName);
    }

    private static String toString(InputStream inputStream)
    {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
}
