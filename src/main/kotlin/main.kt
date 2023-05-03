import Database as db

fun main(args: Array<String>) {

    val participaciones = listOf(Ctf(1, 1, 3), Ctf(1, 2, 101)
        , Ctf(2, 2, 3), Ctf(2, 1, 50), Ctf(2, 3, 1)
        , Ctf(3, 1, 50), Ctf(3, 3, 5))
    val mejoresCtfByGroupId = mejoresResultados(participaciones)
    println(mejoresCtfByGroupId)

    db.loadTable()
}

/**
 * TODO
 *
 * @param participaciones
 * @return devuelve un mutableMapOf<Int, Pair<Int, Ctf>> donde
 *      Key: el grupoId del grupo
 *      Pair:
 *          first: Mejor posición
 *          second: Objeto CTF el que mejor ha quedado
 */
private fun mejoresResultados(participaciones: List<Ctf>): MutableMap<Int, Pair<Int, Ctf>> {
    val participacionesByCTFId = participaciones.groupBy { it.id }
    val participacionesByGrupoId = participaciones.groupBy { it.grupoId }
    val mejoresCtfByGroupId = mutableMapOf<Int, Pair<Int, Ctf>>()
    participacionesByCTFId.values.forEach { ctfs ->
        val ctfsOrderByPuntuacion = ctfs.sortedBy { it.puntuacion }.reversed()
        participacionesByGrupoId.keys.forEach { grupoId ->
            val posicionNueva = ctfsOrderByPuntuacion.indexOfFirst { it.grupoId == grupoId }
            if (posicionNueva >= 0) {
                val posicionMejor = mejoresCtfByGroupId.getOrDefault(grupoId, null)
                if (posicionMejor != null) {
                    if (posicionNueva < posicionMejor.first)
                        mejoresCtfByGroupId[grupoId] = Pair(posicionNueva, ctfsOrderByPuntuacion[posicionNueva])
                } else
                    mejoresCtfByGroupId[grupoId] = Pair(posicionNueva, ctfsOrderByPuntuacion[posicionNueva])

            }
        }
    }
    return mejoresCtfByGroupId
}