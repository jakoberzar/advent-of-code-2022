... Reads the file and dumps it to stdout
aoc		START	0
		CLEAR	A
readLoop	ADD	#0
stdInLoop	TD	stInPtr
		JEQ	stdInLoop
		RD	stInPtr
stdOutLoop	TD	stOutPtr
		JEQ	stdOutLoop
		WD	stOutPtr
		J	readLoop


. constants
stInPtr		BYTE	X'00'
stOutPtr	BYTE	X'01'
valA		BYTE	C'A'
valX		BYTE	C'X'
newline		WORD	10
chara		WORD	65
count		WORD	58

. variables
ofs		WORD	0
one		WORD	1
		END		aoc
