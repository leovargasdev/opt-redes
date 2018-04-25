import java.io.*;
import java.nio.file.*;
import java.util.*;
public class main{
    public static void main(String[] input) throws IOException{
        // Lê os nodos do arquivo nodos.input
        List<String> nodos = new ArrayList<String>();
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[0]))){
            if(!l.startsWith("Id	"))
                nodos.add(l.replaceAll("\t", " "));
        }
        for(String a : nodos)
            System.out.println(a);
        // Lê enlaces do arquivo enlaces.input
        List<String> enlaces = new ArrayList<String>();
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[0])))
            enlaces.add(l.replaceAll("\t", " "));
    }
}
