package hr.from.bkoruznjak.rida.root.database;

/**
 * Created by bkoruznjak on 29/01/2018.
 */

public interface OnDbOperationListener {

    int CREATE = 1;
    int READ = 2;
    int WRITE = 3;
    int DELETE = 4;

    void operationDone(int option);
}