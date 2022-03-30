package com.progjar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadDownload extends Thread{
    private String ext;
    private File files;
    private OutputStream outS;
    private String urn;

    public ThreadDownload(String ext, File files, OutputStream outS, String urn) {
        this.ext = ext;
        this.files = files;
        this.outS = outS;
        this.urn = urn;
    }

    @Override
    public void run() {
        Download download = new Download();
        try {
            download.unduh(ext, files, outS, urn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
