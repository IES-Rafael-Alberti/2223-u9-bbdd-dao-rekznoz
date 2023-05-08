import java.sql.SQLException

class Grupos(private val dataSource: Conexion) : Acceso<Grupo> {

    override fun getElement(id: Int): Grupo? {
        val sql = "SELECT * FROM GRUPOS WHERE grupoid = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, id.toString())
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Grupo(
                        grupoid = rs.getInt("grupoid"),
                        mejorCtfId = rs.getInt("mejorposCTFid")
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun insert(entity: Grupo){
        val conn = dataSource.connection()
        val sql = "INSERT INTO GRUPOS (grupoid, mejorposCTFid) VALUES (?, ?)"
        try {
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, entity.grupoid)
            stmt.setInt(2, entity.mejorCtfId)
            stmt.executeUpdate()
            stmt.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            conn.close()
        }
    }

    override fun selectAll(): MutableList<Grupo> {
        val conn = dataSource.connection()
        val sql = "SELECT * FROM GRUPOS"
        val productsList = mutableListOf<Grupo>()
        try {
            val stmt = conn.createStatement()
            val rs = stmt?.executeQuery(sql)
            while (rs?.next() == true) {
                productsList.add(
                    Grupo(
                        grupoid = rs.getInt("grupoid"),
                        mejorCtfId = rs.getInt("mejorposCTFid")
                    )
                )
            }
            rs?.close()
            stmt?.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            conn.close()
        }
        return productsList
    }

    override fun update(entity: Grupo) {
        val conn = dataSource.connection()
        val sql = "UPDATE GRUPOS SET mejorposCTFid = ? WHERE grupoid = ?"
        try {
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(2, entity.grupoid)
            stmt.setInt(1, entity.mejorCtfId)
            stmt?.executeUpdate()
            stmt?.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            conn.close()
        }
    }

    override fun delete(id: Int) {
        val conn = dataSource.connection()
        val sql = "DELETE FROM GRUPOS WHERE id = ?"
        try {
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, id)
            stmt.executeUpdate()
            stmt.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            conn.close()
        }
    }

    fun updatePuntos() {
        val ctfs = CTFs(dataSource).selectAll()
        val mejoresResultados = mejoresResultados(ctfs)
        mejoresResultados.values.forEach { mejorResultado ->
            var grupo = Grupos(dataSource).getElement(mejorResultado.second.grupoId)
            grupo?.let { elGrupo ->
                elGrupo.mejorCtfId = mejorResultado.second.id
                Grupos(dataSource).update(elGrupo)
            }
        }
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

}
