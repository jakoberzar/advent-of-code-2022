... For part 2, change the use of part1Table to part2Table (just Ctrl+F it)
aoc		START	0
		JSUB	stInit
		CLEAR	A
		TD	stInPtr
		JEQ	halt
		TD	stOutPtr
		JEQ	halt

... -------------------------------
... |     PARSE INSTRUCTIONS      |
... -------------------------------
... Read line
readLoop	TD	stInPtr
		JEQ	part1
		. Read first letter
		RD	stInPtr
		COMP	#0
		JEQ	part1
		. A contains first letter
		RMO	A, B
		LDCH	charN
		COMPR	B, A
		JEQ	parseNoop
. Parse AddX instruction
parseAddX	RD	stInPtr		. Read "d"
		RD	stInPtr		. Read "d"
		RD	stInPtr		. Read "x"
		RD	stInPtr		. Read " "
		JSUB	readNumber	. Read number, untill the end of line
		RMO	A, S		. Store this aside
		LDA	noopInstr
		JSUB	storeInstr	. Add a noop before addx to have every instr be 1 cycle
		RMO	S, A
		JSUB	storeInstr	. Store instruction
		J	readLoop

parseNoop	RD	stInPtr 	. Read "o"
		RD	stInPtr 	. Read "o"
		RD	stInPtr 	. Read "p"
		LDA	noopInstr	. Store 0 to instruction list
		JSUB	storeInstr
		J	readNewLine

... Skip new line or start processing input if input ended
readNewLine	TD	stInPtr
		JEQ	part1
		RD	stInPtr
		COMP	#0
		JEQ	part1
		J	readLoop

... Store to instructions and increase counters
... Parameter in A, X is used for indexing
storeInstr	STA	instructions, X
		LDA	#3		. Increase x for indexing
		ADDR	A, X
		LDA	instrCount 	. Increase instruction counter
		ADD	#1
		STA	instrCount
		CLEAR	A
		RSUB

... Read number
... Reads the (possibly negative) number from stdin
... Modifies A, B, S
... Number being parsed is stored in S, returned in A
readNumber	RD	stInPtr
		. Check minus
		RMO	A, B
		LDCH	charMinus
		COMPR	A, B
		JEQ	rdNum_inMinus
		LDS	#1
		STS	parsingNegative
		J 	rdNum_parseFrst
rdNum_inMinus	LDS	=-1
		STS	parsingNegative
		RD	stInPtr
		RMO	A, B
rdNum_parseFrst	LDCH	char0
		SUBR	A, B
		RMO	B, S
rdNum_parseNext	TD	stInPtr
		JEQ	rdNum_finish
		RD	stInPtr
		COMP	#0
		JEQ	rdNum_finish
		RMO	A, B
		LDCH	newLine
		COMPR	A, B
		JEQ	rdNum_finish
		... Parse new digit
		LDCH	char0
		SUBR	A, B
		LDA	#10
		MULR	A, S
		ADDR	B, S
		J	rdNum_parseNext

... Compute the number and return
rdNum_finish	RMO	S, A
		MUL	parsingNegative
		RSUB

... -------------------------------
... |           PART 1            |
... -------------------------------
... We use register X for indexing instructions
... We store CRT X in register S, current cycle in register T, instrIndex in B
part1		LDS	#1
		CLEAR	T
		CLEAR	X
		CLEAR	B
part1_cycleLoop	LDA	#1
		ADDR	A, T
		LDA	nextSignalSum
		COMPR	T, A
		JLT	part1_instr
		. Calculate signal sum
		MULR	S, A
		ADD	signalSum
		STA	signalSum
		LDA	nextSignalSum
		ADD	signalSumIncr
		STA	nextSignalSum

part1_instr	LDA	instructions, X
		COMP	noopInstr
		JEQ	part1_incrInstr
		ADDR	A, S		. Perform addx op
part1_incrInstr	RMO	B, A		. Increase instr index
		ADD	#1
		COMP	instrCount
		JEQ	part1_printRes
		RMO	A, B
		LDA	#3
		ADDR	A, X
		J	part1_cycleLoop


part1_printRes	LDA	signalSum
		JSUB	printNumber
		J	part2

... -------------------------------
... |           PART 2            |
... -------------------------------
... We use register X for indexing instructions
... We store CRT X in register S, current position in register T, instrIndex in B
part2		LDS	#1
		CLEAR	T
		CLEAR	X
		CLEAR	B
		LDA	textScreenAddr
		STA	textScreenPtr
		CLEAR	A
part2_cycleLoop	LDA	#1
		ADDR	A, T
		LDA	#41
		COMPR	T, A
		JLT	part2_render
		. Reset position to new line
		LDT	#1
part2_render	RMO	S, A
		COMPR	T, A . Draw dot if T is smaller than A, or bigger than A + 2
		JLT	part2_dot
		ADD	#2
		COMPR	T, A
		JGT	part2_dot
part2_hash	LDCH	charHash
		J	part2_writeScrn
part2_dot	LDCH	charDot
part2_writeScrn	STCH	@textScreenPtr
		LDA	textScreenPtr
		ADD	#1
		STA	textScreenPtr
part2_instr	LDA	instructions, X
		COMP	noopInstr
		JEQ	part2_incrInstr
		ADDR	A, S		. Perform addx op
part2_incrInstr	RMO	B, A		. Increase instr index
		ADD	#1
		COMP	instrCount
		JEQ	part2_end
		RMO	A, B
		LDA	#3
		ADDR	A, X
		J	part2_cycleLoop

part2_end	J	halt

... -------------------------------
... |      PRINT NUMBER           |
... -------------------------------
... Expects parameter to print in A, prints it to stdout.
... TODO: Move to stdlib and make it link
... Algorithm:
... 1. Get last digit
... 2. Calculate its character,
... 3. put it on stack, increase counter.
... 4. Afterwards, pop the stack of all digits and write them out
printNumber	STL	@stPtr
		JSUB	stPush
		COMP	#0
		JEQ	endPrintDigits . TODO: Print 0 if input is 0
getDigits	JSUB	getLastDigit
		. Change it to a character
		RMO	A, T
		CLEAR	A . TODO: This is not required
		LDCH	char0
		ADDR	T, A
		. Store result
		STA	@stPtr
		JSUB	stPush
		. Increase digit counter
		LDA	digitCounter
		ADD	#1
		STA	digitCounter
		RMO	B, A
		COMP	#0
		JGT	getDigits
. All digits read, only 0 remains
. Print the digits
printDigits	LDA	digitCounter
		COMP	#0
		JEQ	endPrintDigits
		SUB	#1
		STA	digitCounter
		JSUB	stPop
		LDA	@stPtr
		WD	stOutPtr
		J	printDigits
endPrintDigits	CLEAR	A
		LDCH	newLine
		WD	stOutPtr
		JSUB	stPop
		LDL	@stPtr
		RSUB


... Gets the last digit in A.
... Returns it in A. The divided parameter is available in B.
getLastDigit	RMO	A, B
		DIV	#10
		COMP	#0
		JEQ	getLastDigit0
		MUL	#10
		SUBR	A, B
		DIV	#10
		STA	tmp
		RMO	B, A
		LDB	tmp
		RSUB
getLastDigit0	RMO	B, A
		CLEAR	B
		RSUB


halt		J	halt

... Constants
stInPtr		WORD	X'00'
stOutPtr	WORD	X'01'
char0		BYTE	C'0'
charA		BYTE	C'a'
charN		BYTE	C'n'
charMinus	BYTE	C'-'
charDot		BYTE	C'.'
charHash	BYTE	C'#'
newLine		BYTE	X'0A'
signalSumIncr	WORD	40
textScreenAddr	WORD	X'00B800'

... Variables
tmp		WORD	0
parsingNegative	WORD	1
digitCounter	WORD	0
signalSum	WORD	0
nextSignalSum	WORD 	20
textScreenPtr	WORD	0

... List of parsed instructions.
... We just store how much to add to CRT X.
... We insert additional Noops before AddX so we can treat every instruction as 1 cycle.
instructions	RESW	300
instrCount	WORD	0
noopInstr	WORD	0



... -------------------------------
... |         S T A C K           |
... -------------------------------

... Subroutine Stack Initialize
stInit		LDA	#stStart
		STA	stPtr
		RSUB
... Subroutine Stack Push
... Store your value to @stPtr before call! (e.g. STL @stPtr)
stPush		STA	stStA
		LDA	stPtr
		ADD	#3
		STA	stPtr
		LDA	stStA
		RSUB
... Subroutine Stack Pop
... Get your value via @stPtr after the call! (e.g. LDL @stPtr)
stPop		LDA	stPtr
		SUB	#3
		STA	stPtr
		RSUB
... Subroutine Push All (except L)
stAllPu		STL	stStL	. Store L in memory
		STA	@stPtr
		JSUB	stPush	. Push A
		STB	@stPtr
		JSUB	stPush	. Push B
		STS	@stPtr
		JSUB	stPush	. Push S
		STT	@stPtr
		JSUB	stPush	. Push T
		STX	@stPtr
		JSUB	stPush	. Push X
		LDL	stStL	. Load L from memory
		RSUB
... Subroutine Pop All (except L)
stAllPo		STL	stStL	. Store L in memory
		JSUB	stPop
		LDX	@stPtr	. Pop X
		JSUB	stPop
		LDT	@stPtr	. Pop T
		JSUB	stPop
		LDS	@stPtr	. Pop S
		JSUB	stPop
		LDB	@stPtr	. Pop B
		JSUB	stPop
		LDA	@stPtr	. Pop A
		LDL	stStL	. Load L from memory
		RSUB

... Stack variables
stPtr		RESW	1
stStart		RESW	200
stStL		RESW	1
stStA		RESW	1

		. End
		END		aoc
