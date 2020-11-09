package iamn5.edpn.app.monitors;

public interface IMonitor {
    void start();

    void stop();

    boolean isRunning();
}
