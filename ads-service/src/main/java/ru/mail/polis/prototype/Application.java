package ru.mail.polis.prototype;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
public class Application {
    public static void main(String[] args) {
        SimpleServer server = new SimpleServer(1117);

        server.startUp();
    }
}
