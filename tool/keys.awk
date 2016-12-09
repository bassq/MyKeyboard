#!/usr/bin/awk -f
BEGIN{
	if (ARGC == 1){
		print "usage: keys.awk keyboard_layout.txt [ -v part=main ]" >> "/dev/stderr";
		exit 1;
	}
	
	# make char code hash. AWK does not have char to integer function.
	for( i = 32; i < 128; i++){ codeOf[sprintf("%c",i)] = i }

	# extend char code hash.
	codeOf["SP"] =   32; faceOf["SP"] = "@string/Space";
	codeOf["BS"] =   -5; faceOf["BS"] = "@string/BackSpace";
	codeOf["DL"] =  127; faceOf["DL"] = "@string/Delete";
	codeOf["ES"] = -111; faceOf["ES"] = "@string/Escape";
	codeOf["TB"] =    9; faceOf["TB"] = "@string/Tab";
	codeOf["CT"] =  -10; faceOf["CT"] = "@string/Control";
	codeOf["CR"] =   -4; faceOf["CR"] = "@string/Return";
	codeOf["SH"] =   -1; faceOf["SH"] = "@string/CapsLock";
	codeOf["MT"] =   -6; faceOf["MT"] = "@string/Meta";
	codeOf["MD"] =   -2; faceOf["MD"] = "@string/Mode";
	codeOf["MN"] =   82; faceOf["MN"] = "@string/Menu";
	# keycode 82? this means "R".
	codeOf["zk"] =   19; faceOf["zk"] = "@string/UpArrow";
	codeOf["zj"] =   20; faceOf["zj"] = "@string/DownArrow";
	codeOf["zh"] =   21; faceOf["zh"] = "@string/LeftArrow";
	codeOf["zl"] =   22; faceOf["zl"] = "@string/RightArrow";
	faceOf["@"]  = "\\@";
	faceOf["\\"] = "\\\\";
	faceOf["'"]  = "&apos;";
	faceOf["\""] = "&quot;";
	faceOf["&"]  = "&amp;";
	faceOf["?"]  = "\\?";
	faceOf["<"]  = "&lt;";
	faceOf[">"]  = "&gt;";
}

$0 ~ part , NF == 0 {
	if (NF <= 1) next;

	print "<Row android:keyWidth=\"" 100/NF "%p\">";
	printTag( $1, "android:keyEdgeFlags=\"left\"" );
	for ( i = 2; i < NF; i++ ){
		printTag( $i );
	}
	printTag($NF, "android:keyEdgeFlags=\"right\"" );
	print "</Row>";
}

function printTag( label, option ){
	option = option ? " " option : "";
	if ( label ~ /BS|SP|DL/ ){
		option = "\n android:isRepeatable=\"true\"" option;
	}
	if ( label ~ /SH|CT|MT/ ){
		option = "\n android:isModifier=\"true\" android:isSticky=\"true\"" option;
	}
	printf("<Key android:codes=\"%s\" android:keyLabel=\"%s\"%s/>\n",
		codeOf[label], faceOf[label] ? faceOf[label] : label, option );

	if ( !codeOf[label] ) {
		print "error: unknown key label :", label >> "/dev/stderr";
		exit 1;
	}
}
