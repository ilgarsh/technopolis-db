package ru.mail.polis.prototype;

import com.sun.corba.se.impl.orb.DataCollectorBase;
import ru.mail.polis.prototype.database.Database;
import ru.mail.polis.prototype.database.DatabaseImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
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
                if ((char) data == '~') {
                    break;
                }
            }

            System.out.println(sb.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void handeAddition(String query) {
        String[] tokens = query.split("/");
        try {
            database.addAdvertiser(Long.valueOf(tokens[0]), Float.valueOf(tokens[1]));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
