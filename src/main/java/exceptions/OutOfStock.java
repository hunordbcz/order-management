package exceptions;

public class OutOfStock extends Exception {
    public OutOfStock() {
        super("Not enough stock");
    }
}
