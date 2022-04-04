package mz.maleyanga.security

import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import mz.maleyanga.documento.Nota

class Utilizador implements Serializable {
    private static final long serialVersionUID = 1

    transient springSecurityService

    String username
    String password
    String email
    String telefone1
    String telefone2
    String endereco
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired


    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: true
        password blank: false
        email nullable: true
        telefone1 nullable: true
        telefone2 nullable: true
        endereco nullable: true
        contas nullable: true


    }

    static mapping = {
        password column: '`password`'
        contas lazy: false
        batchSize(10)

    }
    static hasMany = [clientes: Cliente, contas: Conta]
    Set<Role> getAuthorities() {
        UtilizadorRole.findAllByUtilizador(this).collect { it.role }
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

     String toString() {
         return "${username}"
    }
}
