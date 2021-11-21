package home

import org.zkoss.zk.ui.event.MouseEvent
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.*

class IndexComposer extends zk.grails.Composer implements Serializable{
    private static final long serialVersionUID = 1
    @Wire
    Listbox listbox
    Intbox periodo
     Doublebox capital
    Doublebox taxa
    Button btn
    Label notes
    Listfoot listfoot = new Listfoot()


    def afterCompose = { window ->
        // initialize components here
        renderListbox()
       // checkEntidade()
       /* Execution exec = Executions.getCurrent();
        HttpServletResponse response = (HttpServletResponse)exec.getNativeResponse();
        response.sendRedirect(response.encodeRedirectURL("/login")) //assume there is /login
        exec.setVoided(true)*/


    }

    @Listen("onFulfill = label#notes")
    def shownotes(){

    }

    def renderListbox(){


        try {
            listbox.getItems().clear()
            listfoot.children.clear()
            int p = periodo.getValue()
            def  divida = capital.getValue()
            def amortizacao = divida/p
            def juro = divida*(taxa.getValue()/100)
            def prestacao = (juro+amortizacao)

            1.upto(p){
                Listitem listitem = new Listitem()

                Listfooter lf1 = new Listfooter()
                Listfooter lf2 = new Listfooter()
                Listfooter lf3 = new Listfooter()
                Listfooter lf4 = new Listfooter()
                Listfooter lf5 = new Listfooter()
                Listcell lc1 = new Listcell()
                Listcell lc2 = new Listcell()
                Listcell lc3 = new Listcell()
                Listcell lc4 = new Listcell()
                Listcell lc5 = new Listcell()
                lc1.label="${it}"
                lc2.label=divida.floatValue()
                lc3.label=prestacao.floatValue()
               lc4.label=juro.floatValue()
                lc5.label=amortizacao.floatValue()
                lf1.label=""
                lf2.label="Totais"
                lf3.label=prestacao*p
                lf4.label=juro*p
                lf5.label=amortizacao*p

                listitem.appendChild(lc1)
                listitem.appendChild(lc2)
                listitem.appendChild(lc3)
               listitem.appendChild(lc4)
                listitem.appendChild(lc5)

                listfoot.appendChild(lf1)
                listfoot.appendChild(lf2)
                listfoot.appendChild(lf3)
                listfoot.appendChild(lf4)
                listfoot.appendChild(lf5)
                listbox.appendChild(listitem)
                listbox.appendChild(listfoot)
            divida = divida-amortizacao
            }



        }
        catch (NullPointerException e){
            Messagebox.show("Preeencha os todos os campos")
        }


    }

    @Listen ("onClick = button#btn")
    void click(MouseEvent event) {
    Messagebox.show("button clicked")
       renderListbox()
        capital.text=""
        periodo.text=""
        taxa.text=""

    }

    def checkEntidade (){

        if (session.getAttribute('entidade').equals(null)){
            Messagebox.show("Escolha a empresa ")
        }
    }


}
