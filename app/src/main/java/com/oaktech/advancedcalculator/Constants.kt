package com.oaktech.advancedcalculator

/**
 * Constants used throughout the app
 */
object Constants {
    val bracketOpen = "("
    val bracketClose = ")"
    val bracketSymbol = "()"
    val deleteSymbol = "DEL"
    val clearSymbol = "C"
    val clearAllSymbol = "AC"
    val decimalSymbol = "."
    val plusSymbol = "+"
    val minusSymbol = "-"
    val negativeSign = minusSymbol
    val multiplySymbol = "*"
    val divideSymbol = "/"
    val signSymbol = "+/-"
    val solveSymbol = "="
    val imaginarySymbol = "i"
    val infinitySymbol = "INF"
    val independentVariable = "x"

    // it is important to keep arithmeticOperators in the
    // order of precedence i.e. BODMAS or PEMDAS
    val arithmeticOperators = listOf<String>(
        divideSymbol, multiplySymbol,
        plusSymbol, minusSymbol
    )
    val numbers = listOf<String>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")


    val sqrtSymbol = "sqrt"
    val invSymbol = "^-1"
    val squareSymbol = "^2"
    val powerSymbol = "^"
    val sinSymbol = "sin"
    val cosSymbol = "cos"
    val tanSymbol = "tan"
    val piSymbol = "pi"
    val invSinSymbol = "asin"
    val invCosSymbol = "acos"
    val invTanSymbol = "atan"
    val eulerSymbol = "e"
    val logSymbol = "log"
    val lnSymbol = "ln"
    val expSymbol = "e^"
    val absSymbol = "|x|"
    val facSymbol = "!"
    val percentSymbol = "%"
    val permSymbol = "P"
    val combSymbol = "C"
    val leftAbsBracket = "|("
    val rightAbsBracket = ")|"
    val constantSymbols =
        listOf<String>(piSymbol, eulerSymbol, independentVariable) // functions used like constants
    val middleOperations =
        listOf<String>(permSymbol, combSymbol, powerSymbol) // functions used like an operator
    val leftBracketOperations = listOf<String>(
        sqrtSymbol,
        invSymbol,
        sinSymbol,
        cosSymbol,
        tanSymbol,
        invSinSymbol,
        invCosSymbol,
        invTanSymbol,
        logSymbol,
        lnSymbol,
        expSymbol,
    ) // functions used like '(', the '(' is appended to them in the MainEditor
    val rightBracketOperations =
        listOf<String>(
            squareSymbol, facSymbol, percentSymbol
        ) // functions used like ')', the ')' is prepended to them in the MainEditor

    val errorSymbol = "ERROR" // error message displayed in MainEditor if something
    // goes wrong during calculation

    val calculationPrecision = 10 // the precision of the numbers used in calculations
    val outputPrecision = 5 // the max precision used in displaying outputs
}