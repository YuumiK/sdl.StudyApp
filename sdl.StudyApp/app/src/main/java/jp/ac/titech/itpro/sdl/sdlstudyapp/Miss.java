package jp.ac.titech.itpro.sdl.sdlstudyapp;

public class Miss {
    private String str;
    private int count;

    Miss(String str){
        this.str = str;
        count = 0;
    }

    public void addCount()
    {
        count += 1;
    }

    public String getStr(){
        return str;
    }

    public int getCount(){
        return count;
    }
}
