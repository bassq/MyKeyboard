#!/bin/awk -f
BEGIN{
	for( i =32; i < 128; i++){ code[sprintf("%c",i)] = i }
	code["ESC"  ]	= -111;
	code["BS"   ]	= -5;
	code["TAB"  ]	= 9;
	code["ctrl" ]	= -10;
	code["RET"  ]	= -4;
	code["SPC"  ]	= 32;
	code["shift"]	= -1;
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
		code[label], label, option);
}
