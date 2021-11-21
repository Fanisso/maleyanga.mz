package mz.maleyanga.cliente

import mz.maleyanga.security.Utilizador

class Assinante implements Serializable{
    private static final long serialVersionUID = 1
    String nomeDoAssinante
    String estadoCivilDoAssinante
    String nuitDoAssinante
    String tipoDeIndentificacaoDoAssinante
    String numeroDeIndentificaoDoAssinante
    Date dataDeExpiracaoDoAssinante
    String residenciaDoAssinante
    String emailDoAssinante
    Utilizador utilizador
    String localDeTrabalhoDoAssinante
    String telefone
    String telefone1
    String telefone2


    static mapping = {
        id generator: 'increment'
    }
    static belongsTo = [cliente: Cliente]
    static constraints = {

        utilizador nullable: true
        nomeDoAssinante nullable: false, unique: true
        nuitDoAssinante nullable: true, unique: true
        dataDeExpiracaoDoAssinante nullable: true
        numeroDeIndentificaoDoAssinante nullable: true
        residenciaDoAssinante nullable: true
        emailDoAssinante nullable: true

        tipoDeIndentificacaoDoAssinante inList: ["BI", "Passaporte", "Carta de conducao", "Outro"], nullable: true
        estadoCivilDoAssinante inList: ["Solteiro", "Solteira", "Casada", "Casado", "Separado Judicialmente", "Separada Judicialmente", "Outro"]

        telefone nullable: true, maxSize: 9
        telefone1 nullable: true, maxSize: 9
        telefone2 nullable: true, maxSize: 9
        localDeTrabalhoDoAssinante nullable: true


    }

    String toString() {
        return "${nomeDoAssinante}"
    }

}
