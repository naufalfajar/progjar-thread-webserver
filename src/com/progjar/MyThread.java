package com.progjar;

import java.io.*;
import java.net.Socket;

public class MyThread extends Thread{
    private Socket client;
    private Config config;

    public MyThread(Socket client, Config config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public void run() {
        InputStream inS;
        OutputStream outS;
        String websiteroot = config.getRoot1();
        try {
            inS = client.getInputStream();
            outS = client.getOutputStream();
            System.out.println(3);

            System.out.println(4 + ". Obtain BufferedReader and BufferedWriter");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inS));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outS));

            String message = bufferedReader.readLine();

            String urn = "";
            String domain = "";
            String connection = "";

            if(message != null){
                urn = message.split(" ")[1].substring(1);
            }
            System.out.println("domain: "+message);

            while (message != null && !message.isEmpty()) {
                if(message.startsWith("Host:")){
                    System.out.println(message.substring(message.indexOf(" ") + 1));
                    domain = message.substring(message.indexOf(" ") + 1);
                }
                if(message.startsWith("Connection:")){
                    int conIndex = ("Connection: ").length();
                    connection = message.substring(conIndex);
                    System.out.println("CONNECTION = "+connection);
                }
                message = bufferedReader.readLine();
            }

            String fileContent;
            String statusCode;

            if(domain.equalsIgnoreCase("progjar.com")){
                System.out.println("Masuk server 2");
                websiteroot = config.getRoot2();
            }
            try{
                String extension = "";
                if(!urn.isEmpty() && urn.contains(".")){
                    System.out.println("URN: "+urn);
                    extension = urn.split("[.]")[1];
                }

                if(urn.isEmpty()){
                    System.out.println("null URN");
                }
                if(extension.equalsIgnoreCase("pdf")||extension.equalsIgnoreCase("png")||extension.equalsIgnoreCase("zip")){
                    File files = new File(websiteroot + urn);
//                    Download download = new Download();
//                    download.unduh(extension, files, outS, urn);
                    ThreadDownload td = new ThreadDownload(extension,files,outS,urn);
                    td.start();
//                    try {
//                        sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }else{
                    if(urn.isEmpty() || urn.charAt(urn.length()-1)=='/'){
                        File files = new File(websiteroot + urn);
                        String[] fileList = files.list();

                        Dirlist dirlist = new Dirlist();
                        StringBuilder response;
                        response = dirlist.getHTML();

                        assert fileList != null;
                        response = dirlist.listing(response, fileList, files);
                        boolean flag = dirlist.getFlag();
                        statusCode = "200 OK";
                        bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + response.toString().length() + "\r\n");
                        bufferedWriter.write("\r\n");
                        if(!flag){
                            bufferedWriter.write(response.toString());
                        }else{
                            FileInputStream fileInputStream = new FileInputStream(websiteroot + "index.html");
                            fileContent = new String(fileInputStream.readAllBytes());
                            bufferedWriter.write(fileContent);
                        }
                        bufferedWriter.flush();
                    }
                    else{
                        FileInputStream fileInputStream = new FileInputStream(websiteroot + urn);
                        fileContent = new String(fileInputStream.readAllBytes());
                        statusCode = "200 OK";
                        bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + fileContent.length() + "\r\n");
                        bufferedWriter.write("\r\n");
                        bufferedWriter.write(fileContent);
                        bufferedWriter.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                FileInputStream fileInputStream = new FileInputStream(websiteroot + "NotFound.html");
                fileContent = new String(fileInputStream.readAllBytes());
                statusCode = "404 File Not Found";

                bufferedWriter.write("HTTP/1.0 " + statusCode + "\r\nContent-Type: html\r\nContent-Length: " + fileContent.length() + "\r\n");
                bufferedWriter.write("\r\n");
                bufferedWriter.write(fileContent);
                bufferedWriter.flush();
            }
            if(connection.equals("close"))
                client.close();
            System.out.println(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
