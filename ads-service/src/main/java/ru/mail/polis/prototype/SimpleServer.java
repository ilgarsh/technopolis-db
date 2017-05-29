package ru.mail.polis.prototype;

import ru.mail.polis.prototype.database.Database;
import ru.mail.polis.prototype.database.DatabaseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
public class SimpleServer {
    private ServerSocket serverSocket;
    private Database database;

    //    dont forget to close
    public SimpleServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startUp() {
        database = new DatabaseImpl();
        try {
            database.initTables();
//            database.dropTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            while (true) {
                Socket accept = serverSocket.accept();
                executorService.submit(() -> handle(accept));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle(Socket accept) {
        try (InputStream inputStream = accept.getInputStream();
             OutputStream outputStream = accept.getOutputStream()) {
            int data;

            StringBuilder sb = new StringBuilder();
            while (-1 != (data = inputStream.read())) {
                sb.append((char) data);
//                for test
                if ((char) data == '\n') {
                    outputStream.write((dbWork(sb.toString()) + "\n").getBytes(Charset.forName("UTF-8")));
                    sb = new StringBuilder();
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private int dbWork(String query) {
        String[] tokens = query.split("\\?");
        try {
            if (!tokens[0].equals("getAdvertisement") && !tokens[0].equals("addAdvertisement")) {
                return 1;
            }
            switch (tokens[0]) {
                case "addAdvertisement":
                    String[] shit = tokens[1].split("&");
                    shit = shit[0].split("=");
                    Long advId = Long.valueOf(shit[1]);
                    database.addAdvertiser(advId, 13.5f);
//                    validation kek
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

