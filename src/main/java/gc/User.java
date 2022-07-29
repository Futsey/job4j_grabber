package gc;

public class User {

    /** ��������� ������ = 8 ����
     * + ���� id (��������) = 4 �����
     * + ������ �� ��������� ������ Department = 4 �����. � ��� ��������� ������ Department = 16 ����,
     * (��� ��������� 8 ����, ��������� ���������� 4 ����� � �������� 4 ����� ��� ��������� ������ ��� 4 �����)
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
