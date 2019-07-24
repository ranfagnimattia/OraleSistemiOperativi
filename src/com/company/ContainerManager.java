package com.company;

import java.util.ArrayList;

public class ContainerManager {
    private Container[] containers;

    public ContainerManager(int M) {
        containers = new Container[M];
        for(int i=0; i<containers.length;i++) {
            containers[i] = new Container(i+1);
        }
    }

    public synchronized ArrayList<Integer> getTokens(int[] ncontainers)throws InterruptedException  {
        //aspetta finchè la condizione è falsa
        while(!verifyFetchable(ncontainers))
            wait();
        //prende il primo token di ogni container appartenente al sottoinsieme di container (implementato come un insieme di indici corrispondenti ai container designati)
        ArrayList r = new ArrayList<Integer> ();
        for(int i=0; i<ncontainers.length;i++) {
            r.add(containers[ncontainers[i]].getToken());
        }
        notifyAll();
        return r;
    }

    public synchronized void putTokens(int[] ncontainers,int tok) {
        for (int ncontainer : ncontainers) {
            containers[ncontainer].putToken(tok);
        }
        //sblocco tutti i thread bloccati
        notifyAll();
    }

    /**
     * metodo che verifica che sia presente almeno un token in un sottoinsieme di container
     * @param ncontainers insieme di indici corrispondente al sottoinsieme di container
     * @return valore booleano che indica se si è verificata la condizione descritta sopra
     */
    public synchronized boolean verifyFetchable(int[] ncontainers) {
        for(int i=0; i<ncontainers.length;i++) {
            if(containers[ncontainers[i]].emptyToken())
                return false;
        }
        return true;
    }


}
