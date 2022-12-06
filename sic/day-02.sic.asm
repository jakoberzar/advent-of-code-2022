... For part 2, change the use of part1Table to part2Table (just Ctrl+F it)
aoc		START	0
		JSUB	stInit
		TD	stInPtr
		JEQ	halt
		TD	stOutPtr
		JEQ	halt
readLoop	ADD	#0
		. Read first letter
		TD	stInPtr
		JEQ	halt
		RD	stInPtr
		COMP	#0
		JEQ	halt
		. A contains first letter
		RMO	A, B
		LDCH	charA
		SUBR	A, B
		RMO	B, A
		MUL	letterCombCount
		RMO	A, X

		. Skip space
		RD	stInPtr

		. Read second letter
		RD	stInPtr
		. A contains second letter
		RMO	A, B
		LDCH	charX
		SUBR	A, B
		ADDR	B, X
		LDCH	part1Table, X ... CHANGE THIS FOR PART 2!
		. A contains result of this round, add and store it
		ADD	result
		STA	result
		CLEAR	A

		. Skip new line or finish if end
		TD	stInPtr
		JEQ	printResult
		RD	stInPtr
		COMP	#0
		LDA	result
		JEQ	printResult

		J	readLoop

... -------------------------------
... |      PRINT DIGITS          |
... -------------------------------
... Expects parameter to print in A, prints it to stdout.
... TODO: Move to stdlib and make it link
... Algorithm:
... 1. Get last digit
... 2. Calculate its character,
... 3. put it on stack, increase counter.
... 4. Afterwards, pop the stack of all digits and write them out
printResult	COMP	#0
		JEQ	halt . TODO: Print 0 if input is 0
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
		J halt


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
charA		BYTE	C'A'
charX		BYTE	C'X'
newLine		BYTE	X'0A'
letterCombCount	WORD	3
part1Table	BYTE	X'04' 	. AX 3 + 1 = 4
		BYTE	X'08'	. AY 6 + 2 = 8
		BYTE	X'03'	. AZ 0 + 3 = 3
		BYTE	X'01'	. BX 0 + 1 = 1
		BYTE	X'05'	. BY 3 + 2 = 5
		BYTE	X'09'	. BZ 6 + 3 = 9
		BYTE	X'07'	. CX 6 + 1 = 7
		BYTE	X'02'	. CY 0 + 2 = 2
		BYTE	X'06'	. CZ 3 + 3 = 6

part2Table	BYTE	X'03' 	. AX 3 + 0 = 3
		BYTE	X'04'	. AY 1 + 3 = 4
		BYTE	X'08'	. AZ 2 + 6 = 8
		BYTE	X'01'	. BX 1 + 0 = 1
		BYTE	X'05'	. BY 2 + 3 = 5
		BYTE	X'09'	. BZ 3 + 6 = 9
		BYTE	X'02'	. CX 2 + 0 = 2
		BYTE	X'06'	. CY 3 + 3 = 6
		BYTE	X'07'	. CZ 1 + 6 = 7


... Variables
tmp		WORD	0
result		WORD	0
digitCounter	WORD	0



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
