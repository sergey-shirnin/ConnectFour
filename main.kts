class OffBoundsError(message:String): Exception(message)

fun getPlayers(): List<String> {
    val players = mutableListOf<String>()
    for (n in listOf("First", "Second")) {
        println("$n player's name:")
        players.add(readln())
    }
    return players
}

fun getBoard(default: Pair<Int, Int>, limits: Pair<Int, Int>): List<MutableList<String>> {
    var gameBoardSize: List<Int> = emptyList()
    var validInput = false

    do {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default " +
                "(${default.first} x ${default.second})")
        val entry: String = readln()
        if (entry.isNotEmpty()) {
            try {
                gameBoardSize = entry.split("x", ignoreCase = true).map {
                    it.trim().toInt()
                }
                val checks = mutableListOf<Boolean>()
                for (rc in gameBoardSize) {
                    checks.add(rc !in limits.first..limits.second)
                }
                val wrongEntry: String = when {
                    checks[0] && checks[1] -> "both rows and columns"
                    checks[0] -> "rows"
                    checks[1] -> "columns"
                    else -> "none"
                }
                if (wrongEntry != "none") {
                    throw OffBoundsError("Board $wrongEntry should be from " +
                            "${limits.first} to ${limits.second}")
                }
                validInput = true
            } catch (e: Exception) {
                when (e) {
                    is OffBoundsError -> println(e.message)
                    else -> println("Invalid input")
                }
            }
        } else {
            gameBoardSize = default.toList()
            break
        }
    } while (!validInput)

    return List(gameBoardSize[0]) { MutableList(gameBoardSize[1]) { " " } }
}

fun printBoard(content: List<MutableList<String>>, structure: Pair<String, String>) {
    println((1..content[0].size).joinToString(prefix = " ", separator = " "))
    for (row in content) {
        println(row.joinToString(
            prefix = structure.first,
            postfix = structure.first,
            separator = structure.first))
    }
    println(structure.second.repeat(content[0].size * 2 + 1))
}

fun main() {
    // game options editable
    val defaultGameSize: Pair<Int, Int> = Pair(6, 7)
    val gameSizeRestrictions: Pair<Int, Int> = Pair(5, 9)
    val gameBoardChars: Pair<String, String> = Pair("|", "=")

    // pre-game
    println("Connect Four")

    val (playerOne, playerTwo) = getPlayers()
    val gameBoard: List<MutableList<String>> = getBoard(
        default = defaultGameSize,
        limits = gameSizeRestrictions)

    println("$playerOne VS $playerTwo")
    println("${gameBoard.size} X ${gameBoard[0].size} board")

    // game
    var lastAvailableRow = 0
    var player = playerOne

    game@ while (true) {
        printBoard(content = gameBoard, structure = gameBoardChars)

        var entryColumn = 0
        var validInput = false

        do {
            println("$player's turn:")
            val entry: String = readln()
            if (entry == "end") break@game
            else if (!entry.all { char -> char.isDigit() }) {
                println("Incorrect column number")
            }
            else if (entry.toInt() !in 1..gameBoard[0].size) {
                println("The column number is out of range (1 - ${gameBoard[0].size})")
            } else {
                entryColumn = entry.toInt() - 1
                lastAvailableRow  = (entryColumn..entryColumn)
                    .map { ind -> gameBoard.map { row -> row[ind] } }.flatten().lastIndexOf(" ")
                if (lastAvailableRow >= 0) {
                    validInput = true
                } else { println("Column ${entryColumn + 1} is full") }
            }
        } while (!validInput)

        gameBoard[lastAvailableRow][entryColumn] = if (player == playerOne) "o" else "*"
        player = if (player == playerOne) playerTwo else playerOne
    }

    println("Game over!")
}
