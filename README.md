# MyKeyboard

an android soft keyboard for Vim and Busybox sh.
menu key (button) does nothing now.

* QWERTY layout soft keyboard.
* requires android 4.1.2 or more.
* made in Android Studio 2.2.2.

do not use in ordinary text editor.
because this keyboard sends raw control characters to edit field.

i want to implement flick shift and flick ctrl,
but current implementation is shift lock and ctrl lock.

## references

### official sample code of soft keyboard
* https://android.googlesource.com/platform/development/+/master/samples/SoftKeyboard

### other soft keyboards
* https://github.com/klausw/hackerskeyboard
* https://play.google.com/store/apps/details?id=jp.co.pline.android.ctrlkeyboard

## to compile this code in Android Studio

1. open config dialog.
[menu] Run -> Edit Configurations...

2. set "Launch options" to "Nothing".
this project has no activities.

## tool
* keys.awk
  outputs tags for keyboard layout xml files (xml/*_keys.xml).
  edit layout.txt as you like.

  windows' version of git includes GAWK.
  this script can run in GitBash.

  but i had used AWK in [busybox-w32](https://frippery.org/busybox/)...

```sh
cd {path_to_this_project}/tool
awk -f keys.awk -v part=main  layout.txt # gawk
awk -f keys.awk -v part=shift layout.txt # busybox awk
awk -f keys.awk layout.txt -v part=symbol # busybox awk
awk -f keys.awk symKeys1.txt
```
