package ws.miaw.friendfilter;

public class ServerUtil {

    private static boolean onHypixel = false;

    public static void setOnHypixel(boolean newValue) {
        onHypixel = newValue;
    }

    public static boolean isOnHypixel() {
        return  onHypixel;
    }

}
