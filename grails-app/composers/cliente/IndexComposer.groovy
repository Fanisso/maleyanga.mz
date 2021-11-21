package cliente

import grails.plugin.springsecurity.annotation.Secured
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Intbox
import org.zkoss.zul.Messagebox

@Secured(['ROLE_ADMIN','ROLE_USER'])
class IndexComposer extends zk.grails.Composer implements Serializable {
    private static final long serialVersionUID = 1
    Intbox intbox
    def afterCompose = { window ->
        // initialize components here
    }

    @Listen("onClick = intbox#intbox")
    void setEntidade (){
       Messagebox.show("you ")

    }
}
