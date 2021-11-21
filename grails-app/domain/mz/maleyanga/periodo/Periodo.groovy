package mz.maleyanga.periodo

import mz.maleyanga.entidade.Entidade

/**
 * Periodo
 * A domain class describes the data object and it's mapping to the database
 */
class Periodo implements Serializable {
    private static final long serialVersionUID = 1
    String descricao
    Date inicio
    Date fim
    static belongsTo = [entidade: Entidade]


    static constraints = {
    }
    static mapping = {
        id generator: 'increment'
    }

    String toString() {
        return "${id + "-" + descricao}"

    }


}
