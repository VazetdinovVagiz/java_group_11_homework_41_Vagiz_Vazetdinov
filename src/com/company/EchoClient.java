package com.company;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;
    private EchoClient(String host, int port){
        this.host = host;
        this.port = port;
    }
    public static EchoClient connectTo(int port){
        var localhost = "127.0.0.1";
        return new EchoClient(localhost,port);
    }
    public void run(){
        System.out.printf("Напиши 'bye' чтобы выйти%n%n%n");
        try(var socket = new Socket(host,port)){
            var scanner = new Scanner(System.in,"UTF-8");
            var output = socket.getOutputStream();
            var writer = new PrintWriter(output);
            var input = socket.getInputStream();
            var isr = new InputStreamReader(input,"UTF-8");
            Scanner scannerIn = new Scanner(isr);
            try(scanner; writer; scannerIn){
                while(true){
                    String message = scanner.nextLine();
                    StringBuilder sb = new StringBuilder(message);
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    var messageIn = scannerIn.nextLine().strip();
                    System.out.printf("Got: %s%n", messageIn);
                    if("bye".equals(message.toLowerCase())){
                        return;
                    }
                }
            }
        }catch (NoSuchElementException ex){
            System.out.println("connection dropped");
        }catch (IOException e){
            var msg = "Can't connect to %s:%s!%n";
            System.out.printf(msg,host,port);
            e.printStackTrace();
        }
    }
}
