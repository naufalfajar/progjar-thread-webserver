package com.progjar;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        String conf = "\\src\\com\\progjar\\file.conf";

        String confPath = new File("").getAbsolutePath().concat(conf);
        Config config = new Config(confPath);

        System.out.println(1 + ". Create server and client socket");
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(config.getPort()),5, InetAddress.getByName(config.getIp()));
        System.out.println(2);
        int i = 0;

        while (!serverSocket.isClosed()){
            Socket client = serverSocket.accept();

            MyThread ct = new MyThread(client, config);
            System.out.println("THREAD "+i);
            System.out.println(client +"dan"+ config);
            ct.start();
            i++;
        }
        serverSocket.close();
        System.out.println("Server Closed!");
    }

}
