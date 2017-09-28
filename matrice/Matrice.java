package matrice;

public class Matrice {
    private int nbLigne;
    private int nbCol;
    private float tab[][];

    public Matrice (int nbLigne, int nbCol){
        this.nbLigne =nbLigne;
        this.nbCol=nbCol;

        tab = new float[nbLigne][nbCol];
    }

    public int getNbCol() {
        return nbCol;
    }

    public int getNbLigne() {
        return nbLigne;
    }

    public void setTab(int i, int j, float val) {
        tab[i][j] = val;
    }

    public float getTab(int i, int j) {
        return tab[i][j];
    }

    @Override
    public String toString() {
        String chaine="[ ";

        for(int i=0; i<nbLigne; i++)
        {
            for(int j=0; j<nbCol; j++)
            {
                chaine = chaine + tab[i][j] + " ";
            }

            if (i != nbLigne-1)
                chaine = chaine + "| ";
        }

        chaine = chaine + "]";

        return chaine;
    }

    public Matrice multiplier(Matrice B) {
        if (this.nbCol != B.nbLigne) {
            System.out.println("Matrices incompatibles pour la multiplication");
            return new Matrice(1, 1);
        }

        Matrice C = new Matrice(this.nbLigne, B.nbCol);

        for (int i = 0; i < C.nbLigne; i++) {
            for (int j = 0; j < C.nbCol; j++) {
                C.tab[i][j] = 0;

                for (int r = 0; r < this.nbCol; r++) {
                    C.tab[i][j] = C.tab[i][j] + this.tab[i][r] * B.tab[r][j];
                }
            }
        }

        return C;
    }
}
