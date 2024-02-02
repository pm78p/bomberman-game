import java.io.Serializable;

public class Chornometr implements Serializable {

    long stop_time;
    long zero;
    long gaps = 0;
    long savetime;
    static final long serialVersionUID = 1L;

    public Chornometr() {
        zero = System.currentTimeMillis();
    }

    public long getNow() {
        return System.currentTimeMillis() - zero - gaps;
    }

    public void stop() {
        stop_time = System.currentTimeMillis();
    }

    public void resume() {
        gaps += System.currentTimeMillis() - stop_time;
    }

    public void setsavetime() {
        savetime = System.currentTimeMillis();
    }

    public String saveString() {
        return stop_time + " " + zero + " " + gaps + " " + savetime + " ";
    }

    public void loadString(long stop_time, long z, long gps, long savetime) {
        this.stop_time = stop_time;
        this.zero = z;
        this.gaps = gps;
        this.savetime = savetime;
    }

    public void copy(Chornometr tmp) {
        this.gaps = tmp.gaps;
        long beet = tmp.savetime - tmp.zero;
        this.zero = System.currentTimeMillis() - beet;
    }

}
