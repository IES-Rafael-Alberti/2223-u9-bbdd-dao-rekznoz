import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {

    private const val DB = "./database"
    private const val DB_URL = "jdbc:h2:${DB}"
    private const val USER = "root"
    private const val PASS = "toor"

    private fun getConnection(): Connection? {
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS)
        } catch (ex: SQLException) {
            ex.run { printStackTrace() }
        }
        return conn
    }

    // Función para cerrar una conexión a la base de datos
    private fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
        } catch (ex: SQLException) {
            ex.run { printStackTrace() }
        }
    }

    fun loadTable() {
        val dbFile = File("${DB}.mv.db")
        if (dbFile.exists()) {
            println("[DB] Cargada correctamente.")
        } else {
            val conn = getConnection()
            val sql = """
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
        
                ALTER TABLE GRUPOS
                ADD FOREIGN KEY (mejorposCTFid, grupoid)
                REFERENCES CTFS(CTFid,grupoid);
        
                insert into grupos(grupoid, grupodesc) values(1, '1DAM-G1');
                insert into grupos(grupoid, grupodesc) values(2, '1DAM-G2');
                insert into grupos(grupoid, grupodesc) values(3, '1DAM-G3');
                insert into grupos(grupoid, grupodesc) values(4, '1DAW-G1');
                insert into grupos(grupoid, grupodesc) values(5, '1DAW-G2');
                insert into grupos(grupoid, grupodesc) values(6, '1DAW-G3');
        """.trimIndent()
            try {
                val stmt = conn?.prepareStatement(sql)
                stmt?.executeUpdate()
                stmt?.close()
                println("[DB] Creada correctamente.")
            } catch (ex: SQLException) {
                ex.run { printStackTrace() }
            } finally {
                closeConnection(conn)
            }
        }
    }


}