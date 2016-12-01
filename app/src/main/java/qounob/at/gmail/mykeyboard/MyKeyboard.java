package qounob.at.gmail.mykeyboard;

/**
 * Created by user on 2016/11/18.
 */

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    /*
    http://aics-app.sakura.ne.jp/blog/2015/03/02/%E3%82%BD%E3%83%95%E3%83%88%E3%82%A6%E3%82%A7%E3%82%A2%E3%82%AD%E3%83%BC%E3%83%9C%E3%83%BC%E3%83%89%E3%81%AE%E4%BD%9C%E3%82%8A%E6%96%B9android/
    https://code.tutsplus.com/tutorials/create-a-custom-keyboard-on-android--cms-22615
     */
    static final int KEYCODE_CTRL = -10;
    static final int KEYCODE_ESC = -111;
    private KeyboardView kv;
    private Keyboard mainKB;
    private Keyboard symbolKB;
    private boolean shiftLock = false;
    private boolean ctrlLock = false;
    private boolean symLock = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        mainKB = new Keyboard(this, R.xml.main_keys);
        symbolKB = new Keyboard(this, R.xml.symbol_keys);
        kv.setKeyboard(mainKB);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();

        switch(primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_SHIFT:
                shiftLock = !shiftLock;
                kv.getKeyboard().setShifted(shiftLock);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                symLock = !symLock;
                kv.setKeyboard(symLock ? symbolKB : mainKB);
                Toast.makeText(this, ctrlLock ? "ctrl" : "not ctrl", Toast.LENGTH_SHORT).show();
                // kv.getKeyboard().getModifierKeys().forEach()
                // ctrlLock = false;
                // ctrlLock LED
                break;
            case KEYCODE_CTRL:
                ctrlLock = !ctrlLock;
                Toast.makeText(this, ctrlLock ? "ctrl" : "not ctrl", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                sendDownUpKeyEvents(primaryCode);
                break;
            case KEYCODE_ESC:
                ic.commitText(String.valueOf((char)0x1b),1);
                break;
            default:
                char code = (char) primaryCode;
                if (shiftLock && Character.isLetter(code)) {
                    code = Character.toUpperCase(code);
                }
                if (ctrlLock){
                    code = (char)((int)code % 32);
                }
                ic.commitText(String.valueOf(code), 1);
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
