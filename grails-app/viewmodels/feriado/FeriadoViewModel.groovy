package feriado

import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import grails.transaction.Transactional

@Transactional
@Service
class FeriadoViewModel {

    String message
    @Wire  btnHello

    @Init init() {
        // initialzation code here
    }

    @NotifyChange(['message'])
    @Command clickMe() {
        message = "Clicked"
    }

}
