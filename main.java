import java.io.*;
import java.nio.file.*;
import java.util.*;
// import nodo.*;
public class main{
    public static void main(String[] input) throws IOException{
        List<Nodo> nodos = new ArrayList<Nodo>();
        for(String l : Files.readAllLines(FileSystems.getDefault().getPath(input[0]))){
            if(!l.startsWith("Id	"))
                nodos.add(new Nodo(l));
        }
        for(Nodo a : nodos)
            a.getCampos();
    }
}
