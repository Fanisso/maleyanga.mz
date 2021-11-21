package mz.maleyanga

class Gestor {
    String nome
    int ativos
    int inativos
    int total

    int getTotal() {
        return ativos + inativos
    }
}
