
import java.sql.SQLException

fun main(args: Array<String>) {

    val conex = Database()

    //val participaciones = CTFs(conex).selectAll()
    //val mejoresCtfByGroupId = mejoresResultados(participaciones)
    //println(mejoresCtfByGroupId)

    //val args = mutableListOf("-l")

    if (args.isEmpty()) {
        println("Debe proporcionar al menos un argumento.")
        return
    }

    when (args[0]) {
        "-a" -> {
            if (args.size != 4) {
                println("El número de argumentos es incorrecto.")
                return
            }
            val ctfid = args[1].toInt()
            val grupoId = args[2].toInt()
            val puntuacion = args[3].toInt()
            // Añadir una participación
            CTFs(conex).insert(Ctf(ctfid,grupoId,puntuacion))
            Grupos(conex).updatePuntos()
        }
        "-d" -> {
            if (args.size != 3) {
                println("El número de argumentos es incorrecto.")
                return
            }
            val ctfid = args[1].toInt()
            val grupoId = args[2].toInt()
            // Eliminar una participación
            CTFs(conex).delete(ctfid)
            Grupos(conex).updatePuntos()
        }
        "-l" -> {
            if (args.size > 2) {
                println("El número de argumentos es incorrecto.")
                return
            }
            if (args.size == 2) {
                println(Grupos(conex).getElement(args[1].toInt()))
            } else {
                for (grps in Grupos(conex).selectAll()) {
                    println(grps)
                }
            }

        }
        else -> {
            println("La opción proporcionada no es válida.")
            return
        }
    }
}

