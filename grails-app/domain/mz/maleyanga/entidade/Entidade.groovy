package mz.maleyanga.entidade


import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import mz.maleyanga.periodo.Periodo
import org.zkoss.zk.grails.scaffolding.ScaffoldingTemplate

/**
 * Entidade
 * A domain class describes the data object and it's mapping to the database
 */
class Entidade implements Serializable {
    private static final long serialVersionUID = 1

    String nome
    String nuit
    String residencia
    String provincia
    String email
    String telefone
    String proprietario
    String formaDeCalculo
    String descricaoDaFormulaDeCalculo
    String chave
    String numeroDeRegisto
    String genero


    static hasMany = [clientes: Cliente, periodos: Periodo, contas: Conta]
    static mapping = {
        nome unique: true
        nuit unique: true
    }

    static constraints = {
        genero nullable: true, inList: ["masculino", "feminino", "transgênero", "não-binário", "agênero", "pangênero", "genderqueer", " two-spirit", "outro"]
        numeroDeRegisto nullable: true
        nome nullable: false
        residencia nullable: false
        email nullable: true
        telefone nullable: true
        provincia nullable: true
        proprietario nullable: true
        clientes nullable: true
        descricaoDaFormulaDeCalculo maxSize: 2000
        // a listagem na formula de calculo somente aceita uma caratera
        formaDeCalculo inList: ['m', 'v', 's', 'f'], unique: true
        contas nullable: true

    }

    String toString() {
        return "${nome}"
    }


}
