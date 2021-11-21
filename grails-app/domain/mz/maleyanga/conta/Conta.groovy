package mz.maleyanga.conta

import mz.maleyanga.banco.Banco
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.security.Utilizador
import mz.maleyanga.transacao.Transacao
import mz.maleyanga.transferencia.Transferencia

/**
 * Conta
 * A domain class describes the data object and it's mapping to the database
 */
class Conta implements Serializable {
    private static final long serialVersionUID = 1
    String codigo
    String designacaoDaConta
    String numeroDaConta
    String nib
    String iban
    String swiftCode
    BigDecimal credito = 0.0
    BigDecimal debito = 0.0
    BigDecimal saldo = 0.0
    Banco banco
    String finalidade
    Utilizador utilizador
    Cliente cliente
    String natureza
    Conta conta
    Boolean ativo
    PlanoDeContas planoDeContas



    Boolean getAtivo() {
        if (conta) {
            return conta.ativo
        }
        return ativo
    }

    BigDecimal getCredito() {
        credito = 0.0
        if (this.finalidade == "conta_movimento" || this.finalidade == "conta_caixa" || this.finalidade == "conta_capital" || this.finalidade == "conta_cliente" || this.finalidade == "fundo_de_maneio") {
            if (transacoes != null) {
                for (Transacao c in transacoes) {
                    if (c.credito) {
                        if (this.ativo) {
                            credito -= c.valor
                        } else {
                            credito += c.valor
                        }
                    }


                }
            }

        } else if (this.finalidade == "conta_classe" || this.finalidade == "conta_integradora") {
            for (Conta c in contas) {
                if (contas != null) {
                    credito += c.credito
                }

            }
        }
        return credito
    }
    /*
    'conta_cliente', 'conta_capital', 'conta_juros', 'conta_amortizacao', 'conta_poupanca',
                                            'conta_integradora', 'conta_movimento', 'conta_classe', 'conta_xitique', 'conta_juros_de_mora'
    * */

    BigDecimal getDebito() {
        debito = 0.0
        if (this.finalidade == "conta_movimento" || this.finalidade == "conta_caixa" || this.finalidade == "conta_capital" || this.finalidade == "conta_cliente" || this.finalidade == "fundo_de_maneio") {
            if (transacoes != null) {
                for (Transacao d in transacoes) {
                    if (d.credito.equals(false)) {
                        if (this.ativo) {
                            debito += d.valor
                        } else {
                            debito -= d.valor
                        }

                    }

                }
            }

        }
        if (this.finalidade == "conta_classe" || this.finalidade == "conta_integradora") {
            for (Conta c in contas) {
                if (contas != null) {
                    debito += c.debito
                }

            }
        }
        return debito
    }


    BigDecimal getSaldo() {
        return getCredito() + getDebito()
    }


    String getNatureza() {
        if(saldo<0){
            return 'devedora'
        }else
        return 'credora'
    }
/* Default (injected) attributes of GORM */
//	Long	id
//	Long	version

    /* Automatic timestamping of GORM */
//	Date	dateCreated
//	Date	lastUpdated

//	static	belongsTo	= []	// tells GORM to cascade commands: e.g., delete this object if the "parent" is deleted.
//	static	hasOne		= []	// tells GORM to associate another domain object as an owner in a 1-1 mapping
    static hasMany = [transacoes: Transacao, contas: Conta]
    // tells GORM to associate other domain objects for a 1-n or n-m mapping
    //  static mappedBy = [transferencias: 'origem']    // specifies which property should be used in a mapping

    static mapping = {
        id generator: 'increment'
        contas lazy: false
        transacoes lazy: false
        iban unique: true
        numeroDaConta unique: true
    }

    static constraints = {
        codigo nullable: true
        planoDeContas nullable: true
        designacaoDaConta unique: true
        banco nullable: true
        nib nullable: true, unique: true
        natureza nullable: true
        contas nullable: true
        swiftCode nullable: true, unique: true
        iban nullable: true
        numeroDaConta nullable: true
        utilizador nullable: true
        cliente nullable: true


        // conta_cliente tem a finalidade de guardar valores do cliente
        // conta_capital representa o capital que pode ser usado para gerar créditos
        // conta_juros ficam todos os valores de juros pagos
        // conta_jutos_de_mora ficam o valores de juros de mora pagos pelos clientes
        // conta_amortizacao é a conta onde será depositado todos os valores de rembolso do valor empestado portanto é o valor capital devolvido
        finalidade nullable: true, inList: ['conta_integradora', 'conta_cliente', 'conta_movimento', 'conta_classe', 'conta_capital', 'conta_caixa', 'fundo_de_maneio']
    }

    /*
     * Methods of the Domain Class
     */

    @Override
    // Override toString for a nicer / more descriptive UI
    String toString() {
        return "${designacaoDaConta}"
    }

    def calcularSaldo() {
        if (transacoes != null) {
            saldo = 0.0
            transacoes.sort { it.id }
            for (Transacao transacao in transacoes) {
                saldo += transacao.valor
                transacao.saldo = saldo
                transacao.merge()
            }
        }

    }


}
