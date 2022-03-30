package com.progjar;

import java.io.*;
import java.util.zip.ZipOutputStream;

public class Download {

    public void unduh(String ext, File file, OutputStream outS, String urn) throws IOException {
        String contype = "";
        FileInputStream fis = new FileInputStream(file);
        System.out.println("sampek sini");
        if(ext.equalsIgnoreCase("png")){
            contype = "image/png";
        }else if(ext.equalsIgnoreCase("pdf")){
            contype = "application/pdf";
        }
        else if(ext.equalsIgnoreCase("zip")){
            contype = "application/zip";
        }
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        DataOutputStream binaryOut = new DataOutputStream(outS);
        binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
        binaryOut.writeBytes("Content-Type: " + contype + "\r\n");
        binaryOut.writeBytes("Content-Length: " + data.length + "\r\n");
        binaryOut.writeBytes("Content-Disposition: attachment; filename=\"" + urn + "\"");
        binaryOut.writeBytes("\r\n\r\n");
        binaryOut.write(data);
        binaryOut.flush();
        binaryOut.close();
    }
}
