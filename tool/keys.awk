#!/bin/awk -f
BEGIN{
	for( i =32; i < 128; i++){ code[sprintf("%c",i)] = i }
	code["SPC"  ] =   32; face["SPC"  ] = "@string/Space";
	code["BS"   ] =   -5; face["BS"   ] = "@string/BackSpace";
	code["ESC"  ] = -111; face["ESC"  ] = "@string/Escape";
	code["TAB"  ] =    9; face["TAB"  ] = "@string/Tab";
	code["ctrl" ] =  -10; face["ctrl" ] = "@string/Control";
	code["RET"  ] =   -4; face["RET"  ] = "@string/Return";
	code["shift"] =   -1; face["shift"] = "@string/CapsLock";
	code["zj" ] = 20; face["zj" ] = "@string/DownArrow";
	code["zh" ] = 21; face["zh" ] = "@string/LeftArrow";
	code["zl" ] = 22; face["zl" ] = "@string/RightArrow";
	code["zk" ] = 19; face["zk" ] = "@string/UpArrow";
	face["@"]  = "\\@";
	face["\\"] = "\\\\";
	face["'"]  = "&apos;";
	face["&"]  = "&amp;";
	face["<"]  = "&lt;";
	face[">"]  = "&gt;";
}
{
	print "<Row>";
	printTag( $1, "android:keyEdgeFlags=\"left\"");
	for ( i = 2; i < NF; i++ ){
		printTag( $i, "");
	}
	printTag( $NF, "android:keyEdgeFlags=\"right\"");
	print "</Row>";
}
function printTag( label, option ){
	option = option ? " " option : "";
	if ( label ~ /shift|ctrl/ ){
		option = "\n android:isModifier=\"true\" android:isSticky=\"true\"\n" option
	}
	printf("<Key android:codes=\"%s\" android:keyLabel=\"%s\"%s/>\n",
		code[label], face[label] ? face[label] : label, option);
}
