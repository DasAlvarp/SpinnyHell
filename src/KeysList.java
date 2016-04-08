/**
 * Created by alvarpq on 2/17/2016.
 */
public class KeysList {
    int[] boostUp = {87, 73};//W,I
    int[] rbLeft = {65, 74};//A, J
    int[] rbRight = {68, 76};//D, L
    int[] shieldRight = {69, 79};//E, O
    int[] shieldLeft = {81, 85}; //Q, U
    int[] boost = {83, 75 };//s, k
    public int getShieldRight(int cont) {
        return shieldRight[cont];
    }
    public int getShieldLeft(int cont){
        return shieldLeft[cont];
    }


    public int getBoostUp(int cont)
    {
        return boostUp[cont];
    }

    public int getRbLeft(int cont)
    {
        return rbLeft[cont];
    }

    public int getRbRight(int cont)
    {
        return rbRight[cont];
    }

    public int getBoost(int cont)
    {
        return boost[cont];
    }



}
