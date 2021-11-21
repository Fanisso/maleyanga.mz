package mz.maleyanga

import mz.maleyanga.pagamento.Pagamento


class SendReportsJob {
    def sendMailService
    static triggers = {
        cron name: 'myTrigger', cronExpression: "0 0 18 ? * *"
    }

    def execute() {
        def dataActual = new Date()

        def pagamentos = Pagamento.all
        for (Pagamento p in pagamentos) {
           // sendMailService.sendPagamneto(p)
            /*if (dataActual.equals(p.dataDePagamento)) {
                sendMailService.sendPagamneto(p)
            }*/

        }
    }
}


