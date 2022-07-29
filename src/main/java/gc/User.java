package gc;

public class User {

    /** «аголовок класса = 8 байт
     * + поле id (примитив) = 4 байта
     * + ссылка на экземпл€р класса Department = 4 байта. а сам экземпл€р класса Department = 16 байт,
     * (где заголовок 8 байт, ссылочна€ переменна€ 4 байта и добиваем 4 байта дл€ кратности восьми еще 4 байта)
     * 32 = 8 + 4 + 4 + 16
     */
    private int id;
    private long bankAcc;
    private Department department;

    public User(int id, Department department) {
        this.id = id;
        this.department = department;
    }

    public User(int id, long bankAcc) {
        this.id = id;
        this.bankAcc = bankAcc;
    }

    public int userMemCalc() {
        int header = 8;
        int idInBytes = 4;
        int departmentInBytes = 20;
        return header + idInBytes + departmentInBytes;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.printf("Removed %d %s%n", id, department);
    }
}
