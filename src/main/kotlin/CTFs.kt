import java.sql.SQLException

class CTFs(private val dataSource: Conexion) : Acceso<Ctf> {

    override fun getElement(id: Int): Ctf? {
        val sql = "SELECT * FROM CTFS WHERE CTFid = ?"
        return dataSource.connection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Ctf(
                        id = rs.getInt("CTFid"),
                        grupoId = rs.getInt("grupoid"),
                        puntuacion = rs.getInt("puntuacion"),
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun insert(entity: Ctf){
        val conn = dataSource.connection()
        val sql = "INSERT INTO CTFS (CTFid, grupoid, puntuacion) VALUES (?, ?, ?)"
        try {
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, entity.id)
            stmt.setInt(2, entity.grupoId)
            stmt.setInt(3, entity.puntuacion)
            stmt.executeUpdate()
            stmt.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            conn.close()
        }
    }

    override fun selectAll(): List<Ctf> {
        val conn = dataSource.connection()
        val sql = "SELECT * FROM CTFS"
        val ctfList = mutableListOf<Ctf>()
        try {
            val stmt = conn.createStatement()
            val rs = stmt.executeQuery(sql)
            while (rs.next()) {
                ctfList.add(
                    Ctf(
                        id = rs.getInt("CTFid"),
                        grupoId = rs.getInt("grupoid"),
                        puntuacion = rs.getInt("puntuacion"),
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
        return ctfList
    }

    override fun update(entity: Ctf) {
        val conn = dataSource.connection()
        val sql = "UPDATE CTFS SET grupoid = ?, puntuacion = ? WHERE CTFid = ?"
        try {
            val stmt = conn.prepareStatement(sql)
            stmt.setInt(1, entity.grupoId)
            stmt.setInt(2, entity.puntuacion)
            stmt.setInt(3, entity.id)
            stmt.executeUpdate()
            stmt.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        } finally {
            conn.close()
        }
    }

    override fun delete(id: Int) {
        val conn = dataSource.connection()
        val sql = "DELETE FROM CTFS WHERE id = ?"
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

}
