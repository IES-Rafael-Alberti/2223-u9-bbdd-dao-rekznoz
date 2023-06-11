interface Acceso<T> {
    fun insert(entity: T)
    fun selectAll(): List<T>
    fun getElement(id: Int): T?
    fun update(entity: T)
    fun delete(id: Int)
}