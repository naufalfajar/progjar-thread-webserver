package com.progjar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

public class Dirlist {
    private boolean flag = false;

    public StringBuilder getHTML(){
        return new StringBuilder("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="icon" type="image/x-icon" href="https://i.pinimg.com/736x/96/0d/f4/960df438f758d552e7300b7b601ac3c7.jpg">
                <title>FILE LIST</title>

                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
            </head>
            <body>
            <div class="container">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>File/Folder Name</th>
                    <th>Type</th>
                    <th>Last Modified</th>
                    <th>Size (Byte)</th>
                </tr>
                </thead>
                <tbody>
        """);
    }

    public StringBuilder listing(StringBuilder response, String[] fileList, File files) throws IOException {
//        assert fileList != null;
//        boolean flag = false;
        for(String fileName : fileList) {
            System.out.println("filename : "+fileName);
            if(fileName.equalsIgnoreCase("index.html")){
                flag = true;
            }
            response.append("    <tr>\n");
            File file = new File(files+"/"+fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastModified = sdf.format(file.lastModified());
            response.append("        <td><a href=\"/").append(fileName).append(file.isFile() ? "" : "/").append("\">").append(fileName).append("</a></td>\n");
            response.append(file.isFile() ? "        <td>File</td>\n" : "        <td>Folder</td>\n");
            response.append("        <td>").append(lastModified).append("</td>\n");
            response.append("        <td>").append(Files.size(file.toPath())).append("</td>\n");

            response.append("    </tr>\n");
        }
        response.append("""
                                </tbody>
                                </table>
                                </div>
                                </body>
                            </html>""");
        return response;
    }

    public boolean getFlag(){
        return flag;
    }
}
