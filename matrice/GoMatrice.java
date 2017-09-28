package matrice;

public class GoMatrice {
    public static void main (String[] args){
        Matrice A = new Matrice(2,1);
        Matrice B = new Matrice(1,2);

        A.setTab(0,0,3);
        A.setTab(1,0,2);

        B.setTab(0,0,5);
        B.setTab(0,1,4);

        Matrice C = A.multiplier(B);

        System.out.println("Matrices  A  = " + A.toString());
        System.out.println("Matrices  B  = " + B.toString());
        System.out.println("Matrices A*B = " + C.toString());
    }
}
