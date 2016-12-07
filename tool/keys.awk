#!/usr/bin/awk -f
BEGIN{
	if (ARGC == 1){
		print "usage: keys.awk source.txt [ -v part=main ]";
		exit;
	}
	for( i = 32; i < 128; i++){ code[sprintf("%c",i)] = i }
	code["SP"] =   32; face["SP"] = "@string/Space";
	code["BS"] =   -5; face["BS"] = "@string/BackSpace";
	code["ES"] = -111; face["ES"] = "@string/Escape";
	code["TB"] =    9; face["TB"] = "@string/Tab";
	code["CT"] =  -10; face["CT"] = "@string/Control";
	code["CR"] =   -4; face["CR"] = "@string/Return";
	code["SH"] =   -1; face["SH"] = "@string/CapsLock";
	code["DL"] -  127; face["DL"] = "Del";
	code["MT"] =   -6; face["MT"] = "meta";
	code["MD"] =   -2; face["MD"] = "mode";
	code["MN"] =   82; face["MN"] = "menu";
	code["zk"] =   19; face["zk"] = "@string/UpArrow";
	code["zj"] =   20; face["zj"] = "@string/DownArrow";
	code["zh"] =   21; face["zh"] = "@string/LeftArrow";
	code["zl"] =   22; face["zl"] = "@string/RightArrow";
	face["@"]  = "\\@";
	face["\\"] = "\\\\";
	face["'"]  = "&apos;";
	face["\""] = "&quot;";
	face["&"]  = "&amp;";
	face["?"]  = "\\?";
	face["<"]  = "&lt;";
	face[">"]  = "&gt;";
}

$0 ~ part , NF == 0 {
	if (NF <= 1) next;
	print "<Row>";
	printTag( $1, "android:keyEdgeFlags=\"left\"" );
	for ( i = 2; i < NF; i++ ){ printTag( $i ); }
	printTag( $NF, "android:keyEdgeFlags=\"right\"" );
	print "</Row>";
}

function printTag( label, option ){
	option = option ? " " option : "";
	if ( label ~ /BS|SP|DL/ ){
		option = "\n android:isRepeatable=\"true\"\n" option ;
	}
	if ( label ~ /SH|CT|MT/ ){
		option = "\n android:isModifier=\"true\" android:isSticky=\"true\"\n" option ;
	}

	# another option
	# android:keyWidth="30%p" 

	printf("<Key android:codes=\"%s\" android:keyLabel=\"%s\"%s/>\n",
		code[label], face[label] ? face[label] : label, option );
}
