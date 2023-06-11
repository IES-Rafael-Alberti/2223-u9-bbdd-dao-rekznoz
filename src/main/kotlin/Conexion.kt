import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

interface Conexion {
    fun connection() : Connection
}

class Database : Conexion {

    private val DB = "./database"
    private val DB_URL = "jdbc:h2:${DB}"
    private val USER = "root"
    private val PASS = "toor"

    private val sql = """
                CREATE TABLE IF NOT EXISTS GRUPOS (
                    grupoid INT NOT NULL AUTO_INCREMENT,
                    grupodesc VARCHAR(100) NOT NULL,
                    mejorposCTFid INT,
                    PRIMARY KEY (grupoid)
                );
                
                CREATE TABLE IF NOT EXISTS CTFS (
                    CTFid INT NOT NULL,
                    grupoid INT NOT NULL,
                    puntuacion INT NOT NULL,
                    PRIMARY KEY (CTFid,grupoid)
                );
        
                insert into grupos(grupoid, grupodesc) values(1, '1DAM-G1');
                insert into grupos(grupoid, grupodesc) values(2, '1DAM-G2');
                insert into grupos(grupoid, grupodesc) values(3, '1DAM-G3');
                insert into grupos(grupoid, grupodesc) values(4, '1DAW-G1');
                insert into grupos(grupoid, grupodesc) values(5, '1DAW-G2');
                insert into grupos(grupoid, grupodesc) values(6, '1DAW-G3');
        """.trimIndent()

    init {
        loadTable()
    }

    private fun loadTable() {
        val dbFile = File("${DB}.mv.db")
        if (dbFile.exists()) {
            println("[DB] Cargada correctamente.")
        } else {
            val conn = connection()
            try {
                val stmt = conn.prepareStatement(sql)
                stmt?.executeUpdate()
                stmt?.close()
                println("[DB] Creada correctamente.")
            } catch (ex: SQLException) {
                ex.run { printStackTrace() }
            } finally {
                conn.close()
            }
        }
    }

    override fun connection() : Connection {
        return DriverManager.getConnection( DB_URL, USER, PASS)
    }

}