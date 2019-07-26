package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Mover extends Thread{
    private int idM;
    private ContainerManager cm;
    private int[] containertoget;
    private int[] containertoput;
    private int max;
    private int min;

    /**
     * Costruttore della classe Mover.
     * Per inizializzare gli array che corrispondono agli indici dei container da cui Mover deve prelevare e inviare token, si converte la lista di incidenza
     * presente come parametro e si inizializza gli attributi containertoget e containertoput
     * @param idM id del mover
     * @param cm containermanager da dove verranno presi e dove verranno inseriti i token
     * @param inc lista di incidenza del thread Mover attuale
     * @param max tempo di attesa massimo tra ricezione e invio
     * @param min tempo di attesa minimo tra ricezione e invio
     */
    public Mover(int idM, ContainerManager cm, int[] inc, int max,int min) {
        this.idM = idM;
        this.cm = cm;
        this.max = max;
        this.min=min;
        //inizializzo due variabili locali c1 e c2 in modo da
        int c1=0,c2=0;
        //ciclo che conta il numero di container da cui verrà effettuata la ricezione di token e il numero di container su cui verrà fatta la trasmissione di token
        for (int value : inc) {
            if (value == -1)
                c1++;
            else if (value == 1)
                c2++;
        }
        //utilizzo i risultati ottenuti nel ciclo precedente per allocare i due array containertoget e containertoput
        this.containertoget = new int[c1];
        this.containertoput = new int[c2];
        c1=0;c2=0;
        //Elaboro la lista di incidenza
        for(int i=0; i< inc.length;i++) {
            // Nella lista di incidenza -1 corrisponde all'arco Container->Thread, quindi il mover attuale estrarrà token dal container i-esimo
            if(inc[i]==-1) {
                this.containertoget[c1] = i;
                c1++;
            }
            // Nella lista di incidenza 1 corrisponde all'arco Thread->Container, quindi il mover attuale invierà token al container i-esimo
            else if(inc[i]==1) {
                this.containertoput[c2] = i;
                c2++;
            }
        }

    }

    @Override
    public void run() {
        try {
            do {
                //prelevo token dai container designati
                ArrayList tokens = cm.getTokens(containertoget);
                System.out.println("Mover #"+idM+" tokens: "+tokens.toString());
                //attendo un tempo random compreso nell'intervallo [min,max)
                Thread.sleep((int)(Math.random()*(max-min))+min);
                //finchè la coda non è vuota
                while(!tokens.isEmpty()) {
                    int t =(int) tokens.remove(0);
                    //trasmetto i token e li inserisco nei container designati
                    cm.putTokens(containertoput,t);
                    System.out.println("Mover #"+idM+" put token "+t+ " in "+ Arrays.toString(containertoput));
                }

            } while(true);
        }
        catch(InterruptedException ex) {
            System.out.println("Mover #"+idM+" is interrupted");
        }
    }
}
