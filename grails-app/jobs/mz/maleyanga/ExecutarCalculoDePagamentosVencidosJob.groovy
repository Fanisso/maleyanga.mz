package mz.maleyanga

import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.settings.Settings
import org.hibernate.SessionFactory


class ExecutarCalculoDePagamentosVencidosJob {
    SessionFactory sessionFactory
    PagamentoService pagamentoService
    def contaService
    static triggers = {
        //3600000=1h
        simple startDelay: 60000, repeatInterval: 86400000
    }

    def execute() {

        def data = new Date()
        Calendar cal = Calendar.getInstance()

        Settings settings = Settings.findByNome("settings")
        // utilitariosController.sendSMS("envio de sms","843854654")
        def agora = new Date()
        System.println("calculo automatico de dividas " + agora)
        def pagamentos = Pagamento.findAllByPago(false)
        def creditos = Credito.findAllByEmDivida(true)
        def clientes = Cliente.all
        if (settings.calcularAutomatico) {
            System.println("inicio de calculo automatico de moras!")
            for (Credito credito in creditos) {

                def result
                // pagamentoService.calcularMoras(p)
                //  pagamentoService.calcularMoraCaPital(credito)
            }
            //  System.println("fim de calculo automatico de moras!")
        }
        for (Pagamento p in pagamentos) {
            p.merge(failOnError: true)
        }


        /*for (Cliente c in clientes) {
            c.getClassificacao()
            c.merge(failOnError: true)
        }*/


        // execute job
        //  pagamentoService.calcularPagamentosVencidos()4
        /*if (sessionFactory != null) {
            if (!sessionFactory.isClosed()) {
                sessionFactory.close()
            }
        }*/
        System.println(new Date().toString() + " : fim de calculo")
    }
}
