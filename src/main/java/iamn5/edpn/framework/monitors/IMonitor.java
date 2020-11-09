package iamn5.edpn.framework.monitors;

public interface IMonitor {
    void start();

    void stop();

    boolean isRunning();
}
