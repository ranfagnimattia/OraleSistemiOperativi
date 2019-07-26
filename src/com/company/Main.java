package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        //leggo file contenente matrice di incidenza, utilizzo fileReader perchè in questo caso
        // preferisco leggere uno stream di caratteri piuttosto che uno stream di bytes, in tal caso avrei utilizzato InputStream e InputStreamReader
        FileReader filereader = new FileReader("./matrix.txt");
        BufferedReader bf = new BufferedReader(filereader);
        ArrayList<String> strings = new ArrayList<>();
        int height=0;
        String line;
        //leggo ogni stringa del file per ottenere il numero di righe, inserisco ogni stringa in un arrayList di stringhe per poterle processare successivamente e evitare di fare 2 letture
        while((line=bf.readLine()) != null) {
            height++;
            strings.add(line);
        }
        //faccio split sulla prima stringa in modo da ottenere il numero di colonne della matrice per poterla allocare.
        String[] r = strings.get(0).split("\\s");
        int width = r.length;
        int[] [] matrix = new int[height][width];
        int h=0;
        for(String s : strings) {
            //tok è un array di String che divide ogni stringa in token, utilizzando come delimitatore lo spazio: " "
            String[] tok = s.split("\\s");
            //converto ogni token in un numero intero da mettere nella matrice di incidenza
            for(int w=0; w <tok.length;w++) {
                matrix[h][w] = Integer.parseInt(tok[w]);
            }
            h++;
        }

        //inizializzo il container manager
        ContainerManager cm = new ContainerManager(width);
        for(int i=0; i<width-1;i++) {
            //per dare più generalizzazione al programma, inserisco in ogni container tranne l'ultimo un token
            int[] id = new int[] {i};
            int time = (int) (Math.random() * 100);
            cm.putTokens(id,time);
        }

        //alloco un array di thread Mover
        Mover[] movers = new Mover[height];
        int[][] wait_time = new  int[] [] {{200,0},{200,100},{600,500},{300,200}};
        //inizializzo i mover e li faccio partire
        for(int i=0; i<movers.length; i++) {
            movers[i] = new Mover(i,cm,matrix[i],wait_time[i%wait_time.length][0],wait_time[i%wait_time.length][1]);
            movers[i].start();
        }
        Thread.sleep(30000);
        //come da consegna, interrompo i thread dopo 30 secondi
        for (Mover mover : movers) {
            mover.interrupt();
            mover.join();
        }
        System.out.println("PROGRAM FINISHED.");
    }
}
