package android.text;

/**
 * Work around for following error:
 * java.lang.RuntimeException: Method isEmpty in android.text.TextUtils not mocked. See http://g.co/androidstudio/not-mocked for details.
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
