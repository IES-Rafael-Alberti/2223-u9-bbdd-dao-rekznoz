data class Ctf(
    val id: Int,
    val grupoId: Int,
    val puntuacion: Int
)

data class Grupo(
    val grupoid: Int,
    var mejorCtfId: Int = 0
)