import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;
public class main{
    public static void main(String [] input) throws IOException{
        System.out.println("Arquivo 1:");
        for (String linha : Files.readAllLines(FileSystems.getDefault().getPath(input[0]))){
            linha = linha.replaceAll("\t", " ");
            System.out.println(linha);
        }
        System.out.println("Arquivo 2:");
        for (String linha : Files.readAllLines(FileSystems.getDefault().getPath(input[1]))){
            linha = linha.replaceAll("\t", " ");
            System.out.println(linha);
        }
    }
}
