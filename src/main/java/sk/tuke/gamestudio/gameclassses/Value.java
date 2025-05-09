package sk.tuke.gamestudio.gameclassses;

public class Value {

    private char[] value;
    private int rank;

    Value(int size) {
        this.value = new char[size];
        this.rank = 0;
    }

    Value(char[] val) {
        this.value = val;
        this.rank = 0;
    }

    public void set(char[] val) {
        this.value = val;
    }

    public char[] get() {
        return this.value;
    }

    public void set_rank(int rank) {
        this.rank = rank;
    }

    public int get_rank() {
        return this.rank;
    }

    public void increase() {
        this.rank++;
    }

    public void decrease() {
        this.rank--;
    }
}
