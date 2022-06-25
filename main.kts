class offBoundsError(message:String): Exception(message)

fun main() {
    println("Connect Four")

    var players: List<String> = emptyList<String>()
    for (n in listOf("First", "Second")) {
        println("$n player's name:")
        players += readln()
    }

    val (playerOne, playerTwo) =  players

    var gameBoardSize: List<Int> = emptyList()
    var validInput: Boolean = false

    do {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        val entry: String = readln()
        if (!entry.isEmpty()) {
            try {
                gameBoardSize = entry.split("x", ignoreCase = true).map { it.trim().toInt() }
                var checks: List<Boolean> = emptyList()
                for (rc in gameBoardSize) {
                    checks += rc !in 5..9
                }
                val wrongEntry: String = when {
                    checks[0] && checks[1] -> "both rows and columns"
                    checks[0] -> "rows"
                    checks[1] -> "columns"
                    else -> "none"
                }
                if (wrongEntry != "none") {
                    throw offBoundsError("Board $wrongEntry should be from 5 to 9")
                }
                validInput = true
            } catch (e: Exception) {
                when (e) {
                    is offBoundsError -> println(e.message)
                    else -> println("Invalid input")
                }
            }
        } else {
            gameBoardSize = listOf(6, 7)
            break
        }
    } while (!validInput)


    println("$playerOne VS $playerTwo")
    println("${ gameBoardSize.joinToString(" X ") } board")

    println((1..gameBoardSize[1]).joinToString(prefix = " ", separator = " "))
    repeat(gameBoardSize[0]) {
        println("|".repeat(gameBoardSize[1] + 1).chunked(1).joinToString(separator = " "))
    }
    println("=".repeat((gameBoardSize[1]) * 2 + 1))
}
