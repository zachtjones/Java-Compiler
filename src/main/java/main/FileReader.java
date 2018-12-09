package main;

import java.io.*;
import java.util.stream.Collectors;

class FileReader {

    static String readResourcesFile(String name) {
        InputStream is = FileReader.class.getClassLoader().getResourceAsStream(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines().collect(Collectors.joining("\n"));
    }
}
