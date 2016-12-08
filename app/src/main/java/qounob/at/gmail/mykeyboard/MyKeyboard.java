package qounob.at.gmail.mykeyboard;

/**
 * Created by user on 2016/11/18.
 *
 * start with the pages...
 http://aics-app.sakura.ne.jp/blog/2015/03/02/%E3%82%BD%E3%83%95%E3%83%88%E3%82%A6%E3%82%A7%E3%82%A2%E3%82%AD%E3%83%BC%E3%83%9C%E3%83%BC%E3%83%89%E3%81%AE%E4%BD%9C%E3%82%8A%E6%96%B9android/
 https://code.tutsplus.com/tutorials/create-a-custom-keyboard-on-android--cms-22615
 */

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    // final static boolean DEBUG = true;
    final static int KEYCODE_CTRL = -10;
    final static int KEYCODE_ESC = -111;
    final static int KEYCODE_FORWARD_DELETE = 127;

    private KeyboardView kv;
    private Keyboard mainKeyboard;
    private Keyboard shiftKeyboard;
    private Keyboard symbolKeyboard;
    private boolean shiftMode = false;
    private boolean symbolMode = false;
    private boolean ctrlLock = false;
    // private boolean metaLock = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        mainKeyboard = new Keyboard(this, R.xml.main_keys);
        shiftKeyboard = new Keyboard(this, R.xml.shift_keys);
        symbolKeyboard = new Keyboard(this, R.xml.symbol_keys);
        kv.setKeyboard(mainKeyboard);
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
        // Keyboard.Key key = null; // for capture of null key

        switch(primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case KEYCODE_FORWARD_DELETE:
                ic.deleteSurroundingText(0, 1);
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_SHIFT:
                shiftMode = !shiftMode;
                /*
                kv.getKeyboard().setShifted(shiftMode);
                kv.invalidateAllKeys();
                */
                kv.setKeyboard(shiftMode ? shiftKeyboard : mainKeyboard);
                keyOf(Keyboard.KEYCODE_SHIFT).on = shiftMode;
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                /*
                if (DEBUG){
                    Toast.makeText(this, "to save shift",Toast.LENGTH_SHORT).show();
                }
                */
                symbolMode = !symbolMode;
                kv.setKeyboard(symbolMode ? symbolKeyboard :
                        shiftMode ? shiftKeyboard : mainKeyboard);
                keyOf(KEYCODE_CTRL).on = ctrlLock;
                /*
                key = keyOf(Keyboard.KEYCODE_ALT);
                if(key != null) key.on = metaLock;
                */
                break;
            case KEYCODE_CTRL :
                 ctrlLock = !ctrlLock;
                 keyOf(KEYCODE_CTRL).on = ctrlLock;
                break;
            /*
            case Keyboard.KEYCODE_ALT:
                metaLock = !metaLock;
                key = keyOf(Keyboard.KEYCODE_ALT);
                if(key != null) key.on = metaLock;
                break;
                */
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
                /*
                if (shiftMode && Character.isLetter(code)) {
                    code = Character.toUpperCase(code);
                }
                */
                if (ctrlLock){
                    code = (char)((int)code % 32);
                }
                /*
                if (metaLock){
                    code = (char)((int)code + 128);
                }
                */
                ic.commitText(String.valueOf(code), 1);
                break;
        }
    }

    private Keyboard.Key keyOf(int targetKeyCode){
        Keyboard.Key result = null;
        for(Keyboard.Key key : kv.getKeyboard().getKeys()){
            // kv.getKeyboard().getModifierKeys() // NG
            if (key.codes[0] == targetKeyCode){
                result = key;
                break;
            }
        }
        return result;
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
