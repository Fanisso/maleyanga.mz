package conta

import mz.maleyanga.ExtratoDeConta
import mz.maleyanga.SessionStorageService
import mz.maleyanga.TransferenciaService
import mz.maleyanga.ContaService
import grails.transaction.Transactional
import mz.maleyanga.UtilizadorService
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import mz.maleyanga.banco.Banco
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import mz.maleyanga.conta.PlanoDeContas
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.security.Utilizador
import mz.maleyanga.transacao.Transacao
import mz.maleyanga.transferencia.Transferencia
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zul.Button
import org.zkoss.zul.ListModelList
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.Label
import org.zkoss.zul.Tabbox
@Transactional
@Service
class ContasViewModel {
    String filterOrigens
    String filterDestinos
    UtilizadorService utilizadorService
    SessionStorageService sessionStorageService
    TransferenciaService transferenciaService
    @Wire Tabbox tb_classes
    @Wire Label info

    @Wire Button bt_add_pdc
    @Wire Button bt_salvar_banco
    @Wire Button bt_salvar_classe
    @Wire Button bt_delete_banco
    private  String filter
    private  Conta selectedConta
    private  Conta novaContaServico
    private  Conta selectedCI
    private  Conta selectedClasse
    private  Conta novaConta
    private  Conta selectedContaOrigem
    private  Conta selectedContaClasse
    private  Conta selectedContaDestino
    private  Conta selectedContaIntegradora
    private  Conta rconta
    private String valor
    private  Cliente selectedCliente
    private  Banco selectedBanco
    private ListModelList<Conta> contas


    private ListModelList<Conta> integradoras
    private ListModelList<Conta> cis
    private ListModelList<Conta> cintegradoras
    private ListModelList<Conta> classes
    private ListModelList<Conta> cservicos
    private ListModelList<Conta> classesDeContas
    private ListModelList<Conta> contasIntegradoras
    private ListModelList<Conta> contaorigens
    private ListModelList<Conta> contadestinos
    private ListModelList<Banco> bancos
    private ListModelList<String> finalidades
    private ListModelList<String> outrasFinalidades

    private ListModelList<Cliente> clientes
    private ListModelList<PlanoDeContas> pdcs
    private Utilizador selectedUtilizador
    private ListModelList<Utilizador> utilizadores
    private ListModelList<Item> items
    private ListModelList<Transferencia> transferencias
    private ListModelList<Conta> rcontas
    private ListModelList<Conta> ccontas
    ListModelList<Transacao> transacoes = new ListModelList<Transacao>()
    ListModelList<ExtratoDeConta> extratoDeContas = new ListModelList<ExtratoDeConta>()
    Date dataInicial
    Date dataFinal
    private String styleRed = "color:red;font-size:16px;font-weight"
    private String styleBlue = "color:blue;font-size:16px;font-weight"

    @Init init() {

      /*if(contaService.conta!=null){
          rconta = contaService.conta
          contaService.conta = null
      }*/
    }
    @Command
    @NotifyChange(["filterOrigens","contaorigens"])
    void doSearchOrigens() {
        info.value = ""
        contaorigens.clear()
        List<Conta> allItems = getOrigens()
        if (filterOrigens == null || "".equals(filterOrigens)) {
            contaorigens.addAll(allItems)
        } else {
            for (Conta item : allItems) {
                if (item.designacaoDaConta.toString().toLowerCase().indexOf(filterOrigens.toLowerCase()) >= 0 ||
                        item.numeroDaConta.toString().indexOf(filterOrigens) >= 0 ||
                        item.codigo.toLowerCase().indexOf(filterOrigens) >= 0) {
                    contaorigens.add(item)
                }

            }
        }
    }

    List<Conta> getOrigens(){
        def contas = new ListModelList<Conta>(Conta.all)
        List<Conta> origens = new ArrayList<Conta>()
        for(Conta conta in contas){
            if(conta.finalidade=="conta_movimento"||conta.finalidade=="conta_capital"||conta.finalidade=="conta_caixa"||conta.finalidade=="fundo_de_maneio"){
                origens.add(conta)
            }

        }
        return origens
    }
    @Command
    @NotifyChange(["filterDestinos","contadestinos"])
    void doSearchDestino() {
        info.value = ""
        contadestinos.clear()
        List<Conta> allItems = getOrigens()
        if (filterDestinos == null || "".equals(filterDestinos)) {
            contadestinos.addAll(allItems)
        } else {
            for (Conta item : allItems) {
                if (item.designacaoDaConta.toLowerCase().indexOf(filterDestinos.toLowerCase()) >= 0 ||
                        item?.numeroDaConta?.indexOf(filterDestinos) >= 0 ||
                        item?.codigo?.indexOf(filterDestinos) >= 0) {
                    contadestinos.add(item)
                }
            }
        }
    }

    String getFilterOrigens() {
        return filterOrigens
    }

    void setFilterOrigens(String filterOrigens) {
        this.filterOrigens = filterOrigens
    }

    String getFilterDestinos() {
        return filterDestinos
    }

    void setFilterDestinos(String filterDestinos) {
        this.filterDestinos = filterDestinos
    }

    Date getDataInicial() {
        return dataInicial
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial
        Calendar c = Calendar.getInstance()
        c.setTime(dataInicial)
        c.add(Calendar.MONTH, 1)
        c.add(Calendar.DAY_OF_MONTH,-1)
        dataFinal = c.getTime()
        sessionStorageService.dataInicial = dataInicial
        sessionStorageService.dataFinal = dataFinal
    }

    Date getDataFinal() {
        return dataFinal
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataFinal(Date dataFinal) {
        if(dataFinal<dataInicial){
            Calendar c = Calendar.getInstance()
            c.setTime(dataInicial)
            c.add(Calendar.DAY_OF_MONTH, 1)
            dataFinal = c.getTime()
            this.dataFinal = dataFinal

        }else {
            this.dataFinal = dataFinal
        }

        sessionStorageService.dataFinal= dataFinal
    }

    ListModelList<ExtratoDeConta> getExtratoDeContas() {
        extratoDeContas = new ListModelList<ExtratoDeConta>()
        getTransacoes()
        BigDecimal saldo=0.0
        for(Transacao transacao in transacoes){

            if(transacao.credito){
                ExtratoDeConta extratoDeConta = new ExtratoDeConta()
                saldo-=transacao.valor
                extratoDeConta.data = transacao.dateCreated
                extratoDeConta.credito=transacao.valor
                extratoDeConta.debito = 0.0
                extratoDeConta.descricao = transacao.descricao
                extratoDeConta.saldo=saldo
                extratoDeContas.add(extratoDeConta)
            }else {
                ExtratoDeConta extratoDeConta = new ExtratoDeConta()
                saldo+=transacao.valor
                extratoDeConta.data = transacao.dateCreated
                extratoDeConta.credito=0.0
                extratoDeConta.debito =transacao.valor
                extratoDeConta.descricao = transacao.descricao
                extratoDeConta.saldo =saldo
                extratoDeContas.add(extratoDeConta)
            }

        }
        return extratoDeContas
    }

    ListModelList<Transacao> getTransacoes() {
        transacoes= new ListModelList<Transacao>()
        getSelectedConta()
        for(Transacao transacao in rconta?.transacoes){
            transacoes.add(transacao)

        }
        transacoes.sort{it.id}
        return transacoes
    }

    ListModelList<Conta> getRcontas() {
        if(rcontas == null){
            rcontas = new ListModelList<Conta>(Conta.findAllByFinalidadeOrFinalidadeOrFinalidadeOrFinalidade("conta_movimento","conta_caixa","conta_capital","fundo_de_maneio"))
        }
        return rcontas
    }

    ListModelList<Conta> getCcontas() {
       if(ccontas == null){
            ccontas = new ListModelList<Conta>(Conta.findAllByClienteIsNotNull())
        }

        return ccontas
    }

    Conta getRconta() {
        return rconta
    }

    @NotifyChange(["dataInicial","dataFinal"])
    void setRconta(Conta rconta) {
        this.rconta = rconta
        printExtrato()
    }

    ListModelList<Conta> getCservicos() {
        if(cservicos==null){
            cservicos = new ListModelList<Conta>()
        }
        cservicos.clear()
        def contas = Conta.all
        for(Conta conta in contas){
            if(conta.finalidade=="conta_capital"||conta.finalidade=="conta_caixa"||conta.finalidade=="fundo_de_maneio"){
                cservicos.add(conta)
            }
        }
        return cservicos
    }

    ListModelList<Conta> getCintegradoras() {
        if(cintegradoras==null){
            cintegradoras = new ListModelList<Conta>(Conta.findAllByFinalidade("conta_integradora"))
        }
        return cintegradoras
    }

    Conta getNovaContaServico() {
        return novaContaServico
    }

    void setNovaContaServico(Conta novaContaServico) {
        this.novaContaServico = novaContaServico
    }

    ListModelList<Transferencia> getTransferencias() {
        if(transferencias==null){
            transferencias = new ListModelList<Transferencia>()
        }
        return transferencias
    }

    String getValor() {
        return valor
    }

    void setValor(String valor) {
        this.valor = valor
    }
    @Command
    @NotifyChange(['rconta','rcontas','transacoes','extratoDeContas'])
    def showExtrato(){
      //  System.println('showExtrato')
        getTransacoes()
        getExtratoDeContas()

    }
    @Command
    @NotifyChange(['items',"novaConta"])
    def printExtrato (){

        if(rconta==null){
            info.value = "Selecione uma conta!"
            info.style = styleRed
            return
        }
        if(dataFinal==null){
            info.value = "Selecione uma data!"
            info.style = styleRed
            return
        }
        if(dataInicial==null){
            info.value = "Selecione uma data!"
            info.style = styleRed
            return
        }
     /*   contaService.conta = rconta
        contaService.dataInicial = dataInicial
        contaService.dataFinal = dataFinal*/
        sessionStorageService.conta = rconta
        sessionStorageService.dataInicial = dataInicial
        sessionStorageService.dataFinal = dataFinal
      //  Executions.sendRedirect("/conta/printExtratoDeConta/")
    }
    @Command
    @NotifyChange(['items',"novaConta"])
    ListModelList<conta.ContasViewModel.Item> getItems() {

        if(items==null){
            items = new ListModelList<Item>()
        }
        BigDecimal saldo = 0.0

        int x = rconta.transacoes.size()
        if(x==0){
            return items
        }else {
            for (int i=0; i<x;i++) {
                if (rconta.transacoes[x].credito) {

                    if (rconta.ativo) {
                        saldo -= rconta.transacoes[x].valor
                    } else {
                        saldo += rconta.transacoes[x].valor
                    }
                    Item item = new Item("0.0",rconta.transacoes[x].valor+"",saldo+"")
                    items.add(item)
                }else {
                    if (rconta.ativo) {
                        saldo += rconta.transacoes[x].valor
                    } else {
                        saldo -= rconta.transacoes[x].valor
                    }
                    Item item = new Item(rconta.transacoes[x].valor+"","0.0",saldo+"")
                    items.add(item)
                }


            }
        }



        return items

    }



    @Command
    @NotifyChange([''])
    def limpar(){
        transferencias.clear()
    }
    @Command
    @NotifyChange(['transferencias'])
    def addDestino(){
        if(selectedContaOrigem==null){
            info.value = "Selecione a conta Origem"
            info.style = styleRed
        }else {
            Transferencia transferencia = new Transferencia()
            transferencia.origem = selectedContaOrigem
            transferencia.destino = selectedContaDestino
              transferencias.add(transferencia)
        }
    }
    //
    @Command
    @NotifyChange(['info'])
    def verificarCondicoes(){
        for(Transferencia tr in transferencias){
                if(selectedContaOrigem.saldo<tr.valor){
                    info.value = "A conta '"+selectedContaOrigem.codigo+"' não tem saldo sufuciente!"
                    info.style = styleRed
                }
        }
    }

    @Command
    @NotifyChange(['contaorigens','contadestinos',"transferencias",'contas','cis','integradoras','trans'])
    def salvarTrans(){
        info.value = ""
        try {
            if(transferencias.empty){
                info.value = "Selecione as contas e o valor!"
                info.style = styleBlue
                return
            }

            boolean ok=  transferenciaService.salvarTransferencias(transferencias)
            if (ok){
                info.value = "Os dados foram gravados com sucesso !"
                info.style = styleBlue

                transferencias.clear()
            }else {
                info.value = "Erro na gravação dos dados !"
                info.style = styleBlue

            }

            info.value = "Dados gravados com sucesso!"
            info.style = styleBlue
        }catch(Exception e) {
          //  System.println(e.toString())
            info.value = e.toString()
            info.style = styleRed
        }

    }

    ListModelList<Conta> getIntegradoras() {
        if (integradoras==null){
            integradoras = new ListModelList<Conta>()
        }
        integradoras.clear()
        integradoras = Conta.findAllByFinalidade("conta_integradora")
        return integradoras
    }


    private Entidade entidade
    private  PlanoDeContas selectedPlanoDeContas

    Conta getselectedContaIntegradora() {
        return selectedContaIntegradora
    }

    void setselectedContaIntegradora(Conta selectedContaIntegradora) {
        this.selectedContaIntegradora = selectedContaIntegradora
    }

    Conta getSelectedCI() {
        return selectedCI
    }

    void setSelectedCI(Conta selectedCI) {
        this.selectedCI = selectedCI
    }

    @Command
    @NotifyChange(['selectedCI'])
    def chooseSelectedCI(@BindingParam("index") Integer index){
        selectedCI = Conta.findById(index)
       // System.println(selectedCI.designacaoDaConta)
    }

    @Command
    @NotifyChange(['cis','classesDeContas'])
    ListModelList<Conta> getCis() {

        if (cis==null){
            cis = new ListModelList<Conta>()
        }
        cis.clear()

        if(selectedContaClasse){

            cis = Conta.findAllByConta(selectedContaClasse)
        }else {

            cis=Conta.findAllByFinalidade("conta_integradora")
        }
        return cis
    }

    ListModelList<Conta> getClassesDeContas() {
        if(classesDeContas==null){
            classesDeContas = new ListModelList<Conta>()
        }
        classesDeContas = Conta.findAllByFinalidade("conta_classe")
        return classesDeContas
    }

    Conta getSelectedContaClasse() {
        return selectedContaClasse
    }

    void setSelectedContaClasse(Conta selectedContaClasse) {
        this.selectedContaClasse = selectedContaClasse
    }

    Conta getSelectedContaIntegradora() {
        return selectedContaIntegradora
    }

    void setSelectedContaIntegradora(Conta selectedContaIntegradora) {
        this.selectedContaIntegradora = selectedContaIntegradora
    }

    ListModelList<Conta> getContasIntegradoras() {
        if(contasIntegradoras==null){
            contasIntegradoras = new ListModelList<Conta>()
        }
        contasIntegradoras.clear()
        contasIntegradoras = Conta.findAllByFinalidade("conta_integradora")
        return contasIntegradoras
    }

    ListModelList<PlanoDeContas> getPdcs() {
        if(pdcs==null){
            pdcs = new ListModelList<PlanoDeContas>(PlanoDeContas.all)
        }

        return pdcs
    }

    @Command
    @NotifyChange(['selectedPlanoDeContas','pdcs'])
    def addPDC(){
        bt_add_pdc.style="color:blue"
        selectedPlanoDeContas = new PlanoDeContas()
    }

    @Command
    @NotifyChange(['selectedPlanoDeContas','pdcs'])
    def salvarPDC(){
        try {

            if(selectedPlanoDeContas.hasErrors()){
                info.value = "Preencha os campos devidamente"
                info.style = styleRed
                return
            }
            def pdcs= PlanoDeContas.all
            if(pdcs!=null){
                if(selectedPlanoDeContas.ativo){
                    pdcs.each {
                        it.ativo=false
                        it.merge(flush: true)
                    }
                }
            }

            selectedPlanoDeContas.save(flush: true)
            info.value = "O Plano de contas "+selectedPlanoDeContas.descricao+" foi criado com sucesso!"
            info.style= styleBlue
        }catch(Exception e){
            info.value = e.toString()
            info.style=styleRed
            System.println(e.toString())
        }
    }

    @Command
    @NotifyChange(['pdcs','selectedPlanoDeContas'])
    def deletePDC(){
        try {

            selectedPlanoDeContas.delete flush: true
            info.value = "O Plano de contas " + selectedPlanoDeContas.descricao + " foi removido com sucesso!"
            info.style = styleBlue
        } catch (Exception e) {
           // System.println(e.toString())
            info.value = e.toString()
            info.style = styleRed
        }

        getPdcs()
    }

    PlanoDeContas getSelectedPlanoDeContas() {
        return selectedPlanoDeContas
    }

    void setSelectedPlanoDeContas(PlanoDeContas selectedPlanoDeContas) {
        this.selectedPlanoDeContas = selectedPlanoDeContas
    }

    ListModelList<String> getOutrasFinalidades() {
        if(outrasFinalidades==null){
            outrasFinalidades = new ListModelList<String>(['conta_movimento'])
        }
        return outrasFinalidades
    }

    Conta getNovaConta() {
        return novaConta
    }

    void setNovaConta(Conta novaConta) {
        this.novaConta = novaConta
    }

    Conta getSelectedClasse() {
        return selectedClasse
    }

    void setSelectedClasse(Conta selectedClasse) {
        this.selectedClasse = selectedClasse
    }

    @Command
    @NotifyChange(['selectedConta'])
    def setCodigoDaconta(){

    }




    Utilizador getSelectedUtilizador() {
        return selectedUtilizador
    }

    void setSelectedUtilizador(Utilizador selectedUtilizador) {
        this.selectedUtilizador = selectedUtilizador
    }

    Entidade getEntidade() {
        return entidade
    }

    void setEntidade(Entidade entidade) {
        this.entidade = entidade
    }

    ListModelList<Utilizador> getUtilizadores() {
        if (utilizadores==null){
            utilizadores = new   ListModelList<Utilizador>(utilizadorService.all)
        }
        return utilizadores
    }





    @Command
    @NotifyChange(['bancos','selectedBanco'])
    def salvarBanco(){
        try {
            selectedBanco.save(flush: true)
            if(Banco.findById(selectedBanco?.id)){
                bt_salvar_banco.label = 'Banco criado com sucesso!'
                getBancos()
            }
        }catch (Exception e){
            bt_salvar_banco.label = e.toString()
        }
    }

    @Command def deleteBanco(){
        try {
            selectedBanco.delete(flush: true)
            bt_delete_banco.label='Banco eliminado!'
        }catch (Exception e){
            bt_delete_banco.label = e.toString()
        }
    }


    @Command
    @NotifyChange(['selectedContaIntegradora'])
    def checkContent(){
        try {
            Integer.parseInt(selectedContaIntegradora.codigo)
        }catch(Exception e){
            info.value = "Somente númoros devem constar nesse campo!"
            info.style = styleRed
            selectedContaIntegradora.codigo = ""

        }

    }
    @Command
    @NotifyChange(['selectedContaIntegradora'])
    def checkMe(){
        try {
            Integer.parseInt(novaConta.codigo)
        }catch(Exception e){
            info.value = "Somente númoros devem constar nesse campo!"
            info.style=styleRed
            novaConta.codigo = ""

        }

    }

    @Command
    @NotifyChange(['selectedContaIntegradora','novaContaServico'])
    def checkM(){
        try {
            Integer.parseInt(novaContaServico.codigo)
        }catch(Exception e){
            info.value = "Somente númoros devem constar nesse campo!"
            info.style = styleRed
            novaContaServico.codigo = ""

        }

    }

    @Command
    @NotifyChange(['selectedClasse'])
    def changeMe(){
        if(selectedClasse.ativo){
            selectedClasse.ativo = false
        }else {
            selectedClasse.ativo=true
        }
    }

    @Command
    @NotifyChange(['contasIntegradoras','selectedContaIntegradora','cis','contasIntegradoras','contas','contadestinos','contaorigens'])
    def salvarConta(){
        if(Conta.findById(novaConta.id)){
            info.value="Esta conta já existe"
            info.style = styleRed
            return
        }

        if(novaConta?.codigo==""){
            info?.value = "Preencha os campos"
            info?.style = styleRed
            return
        }

        if(selectedCI==null){
            info.value = "Preencha os campos"
            info.style = styleRed
            return
        }
        try {
            Integer cod = novaConta.codigo.toInteger()
            novaConta.codigo = ""
            String str = String.format("%04d", cod)
           // System.println(str)
            novaConta.conta = selectedCI
            novaConta.ativo = selectedCI
            novaConta.codigo = novaConta.conta.codigo+"."+str
            if(novaConta.ativo==null){
                info.value = "acione activo ou passivo no campo tipo de conta"
                info.style = styleRed
                novaConta.codigo = ""
                return
            }

            if(selectedCI?.id!=null){

                novaConta.conta = selectedCI
              //  System.println(novaConta.codigo)
            }


            if(Conta.findByCodigo(novaConta.codigo)){
                info.value = "Já existe uma conta com este código!"
                info.style = styleRed
                return
            }
            novaConta.planoDeContas = PlanoDeContas.findByAtivo(true)
            novaConta.finalidade = "conta_movimento"
            System.println(novaConta.planoDeContas)


            novaConta.save(flush: true)

            def sccDB = Conta.findById(novaConta.id)

            def contaPai = Conta.findById(novaConta.conta.id)
            if(sccDB){

                if (contaPai.contas ==null){
                    contaPai.contas = new ArrayList<Conta>()
                }
                if(selectedUtilizador){
                    Utilizador utilizador = Utilizador.findById(selectedUtilizador.id)
                    if(utilizador.contas==null){
                        utilizador.contas = new LinkedHashSet<Conta>()
                    }
                    utilizador.contas.add(sccDB)
                    utilizador.merge(flush: true)
                    sccDB.utilizador = utilizador
                }
                contaPai.contas.add(sccDB)
                contaPai.merge(flush: true)
                sccDB.merge(flush: true)
                info.value = "A conta '" + novaConta.designacaoDaConta + "' foi criado com sucesso!"
                info.style = styleBlue
            }
        }catch(Exception e){
            System.println(e.toString())
            info.value = "Ocorreu um erro durante a gração dos dados!"
            info.style = styleRed
        }
        novaConta = new Conta()
    }

    @Command
    @NotifyChange(['contasIntegradoras','selectedContaIntegradora','cis','contas',"cservicos","novaContaServico","contaorigens","contadestinos","integradoras"])
    def salvarContaServico(){
        if(Conta.findById(novaContaServico.id)){
            info.value="Esta conta já existe"
            info.style = styleRed
            return
        }
           Conta contaCaixa = Conta.findByUtilizadorAndFinalidade(selectedUtilizador,"conta_caixa")
           Conta contaCapital = Conta.findByUtilizadorAndFinalidade(selectedUtilizador,"conta_capital")
           Conta contaManeio = Conta.findByUtilizadorAndFinalidade(selectedUtilizador,"fundo_de_maneio")
        if(novaContaServico.finalidade=="conta_caixa"){
            while (contaCaixa!=null){
                info?.value = "O Utilizador "+selectedUtilizador.username+" já tem uma conta caixa com a ref."+contaCaixa.codigo
                info?.style = styleRed
                return
            }
        }
        if(novaContaServico.finalidade=="conta_capital"){
            while (contaCapital!=null){
                info?.value = "O Utilizador "+selectedUtilizador.username+" já tem uma conta caixa com a ref."+contaCapital.codigo
                info?.style = styleRed
                return
            }
        }
        if(novaContaServico.finalidade=="fundo_de_maneio"){
            while (contaManeio!=null){
                info?.value = "O Utilizador "+selectedUtilizador.username+" já tem uma conta caixa com a ref."+contaManeio.codigo
                info?.style = styleRed
                return
            }
        }


        if(novaContaServico?.codigo==""){
            info?.value = "Preencha os campos"
            info?.style = styleRed
            return
        }

        if(selectedCI==null){
            info.value = "Preencha os campos"
            info.style = styleRed
            return
        }
        try {
            System.println(novaContaServico.codigo)
            Integer cod = novaContaServico.codigo.toInteger()
            novaContaServico.codigo = ""
            String str = String.format("%04d", cod)
            System.println(str)
            novaContaServico.conta = selectedCI
            novaContaServico.ativo = selectedCI
            novaContaServico.codigo = novaContaServico.conta.codigo+"."+str
            if(novaContaServico.ativo==null){
                info.value = "acione activo ou passivo no campo tipo de conta"
                info.style = styleRed
                novaContaServico.codigo = ""
                return
            }

            if(selectedCI?.id!=null){


                System.println(novaContaServico.codigo)
            }


            if(Conta.findByCodigo(novaContaServico.codigo)){
                info.value = "Já existe uma conta com este código!"
                info.style = styleRed
                return
            }
            novaContaServico?.planoDeContas = PlanoDeContas?.findByAtivo(true)
           // novaContaServico.finalidade = se
            System.println(novaContaServico.planoDeContas)

            novaContaServico.save(flush: true)

            def sccDB = Conta.findById(novaContaServico.id)

            def contaPai = Conta.findById(novaContaServico.conta.id)
            if(sccDB){

                if (contaPai.contas ==null){
                    contaPai.contas = new ArrayList<Conta>()
                }
                if(selectedUtilizador){
                    Utilizador utilizador = Utilizador.findById(selectedUtilizador.id)
                    if(utilizador.contas==null){
                        utilizador.contas = new LinkedHashSet<Conta>()
                    }
                    utilizador.contas.add(sccDB)
                    utilizador.merge(flush: true)
                    sccDB.utilizador = utilizador
                }
                contaPai.contas.add(sccDB)
                contaPai.merge(flush: true)
                sccDB.merge(flush: true)
                info.value = "A conta '"+novaContaServico.designacaoDaConta +"' foi criado com sucesso!"
                info.style = styleBlue
            }
        }catch(Exception e){
            System.println(e.toString())
            info.value = "Ocorreu um erro durante a gração dos dados!"
            info.style = styleRed
        }
        novaContaServico = new Conta()
    }
    @Command
    @NotifyChange(['contasIntegradoras','selectedContaIntegradora','cis','contasIntegradoras','integradoras',"cintegradoras",'integradoras','selectedContaClasse'])
    def salvarContaIntegradora(){

        if(selectedContaIntegradora?.codigo==""){
            info.value = "Preencha o campo codigo"
            info.style = styleRed
            return
        }
        if(!selectedContaClasse?.id&&!selectedCI?.id){
            info.value = "Selecione uma classe ou uma conta integradora"
            info.style = styleRed
            return
        }
        try {
            String cod = selectedContaIntegradora.codigo+""
            selectedContaIntegradora.codigo = ""
            if(selectedCI?.id){
                System.println("selectedCI"+selectedCI)
                selectedContaIntegradora.codigo = selectedCI.codigo+"."+cod
                selectedContaIntegradora.conta = selectedCI
                selectedContaIntegradora.ativo = selectedCI.ativo
            }else {
                System.println("selecionado conta classe")
                System.println("selectedContaClasse"+selectedContaClasse)
                selectedContaIntegradora.codigo = selectedContaClasse.codigo+"."+cod
                selectedContaIntegradora.conta = selectedContaClasse
                selectedContaIntegradora.ativo = selectedContaClasse.ativo
            }


            if(Conta.findByCodigo(selectedContaIntegradora.codigo)){
                info.value = "Já existe uma conta com este código!"
                info.style = styleRed
                return
            }
          //  selectedContaIntegradora.planoDeContas = PlanoDeContas.findByAtivo(true)
            selectedContaIntegradora.finalidade="conta_integradora"
            selectedContaIntegradora.save flush: true

            def sccDB = Conta.findById(selectedContaIntegradora.id)
            def contaPai = Conta.findById(selectedContaIntegradora.conta.id)
            if(sccDB){
                if (contaPai.contas ==null){
                    contaPai.contas = new ArrayList<Conta>()
                }
                contaPai.contas.add(sccDB)
                contaPai.merge(flush: true)
                info.value = "A " + selectedContaIntegradora.designacaoDaConta + " foi criado com sucesso!"
                info.style = styleBlue
            }
        }catch(Exception e){
            System.println(e.toString())
        }
        selectedContaIntegradora = new Conta()
    }

    @Command
    @NotifyChange(['contasIntegradoras','selectedContaIntegradora'])
    def updateContaIntegradora(){
        try {

            selectedContaIntegradora.merge(flush: true)
            info.value = "A "+selectedContaIntegradora.designacaoDaConta +" foi atualizado com sucesso!"
            info.style = styleBlue
        }catch(Exception e){
            System.println(e.toString())
        }
    }


    @Command
    @NotifyChange(['classes','cis','classesDeContas','cintegradoras'])
    def salvarClasse(){

        try {

            if(Conta.findByCodigo(selectedClasse.codigo)){
                info.value = 'Já existe uma conta com este codigo'
                info.style = styleRed
                return
            }
            selectedClasse.planoDeContas = PlanoDeContas.findByAtivo(true)
            selectedClasse.finalidade = "conta_classe"
            selectedClasse.save(flush: true)
            def contaDb = Conta.findById(selectedClasse.id)
            if(contaDb!=null){

                info.value = "A Classe " + selectedClasse.designacaoDaConta + " foi criado com sucesso!"
                info.style = styleBlue
            }

        }catch (Exception e){

            info.value = 'Erro na gravação!'
            info.style = styleRed
            System.println(e.toString())
        }
        selectedClasse = new Conta()
        getCis()
    }

    @Command
    @NotifyChange(['selectedPlanoDeContas'])
    def changeFechado(){
        if(selectedPlanoDeContas.fechado){
            selectedPlanoDeContas.fechado=false
        }else {
            selectedPlanoDeContas.fechado = true
        }
    }
    @Command
    @NotifyChange(['selectedPlanoDeContas'])
    changeAtivo(){
        if(selectedPlanoDeContas.ativo){
            selectedPlanoDeContas.ativo = false
        }else {
            selectedPlanoDeContas.ativo=true
        }
    }

    @Command
    def cleanInfo(){
        info.value=""
    }
    @Command
    def updateConta(){
        try {
            Conta code = Conta.findById(novaConta.id)
            novaConta.codigo = code.codigo
            novaConta.merge(flush: true)
            info.value = novaConta.designacaoDaConta + " foi atualizado com sucesso!"
            info.style = styleBlue
        } catch (Exception e) {
            info.value = e.toString()
            info.style= styleRed
        }

    }
    @Command
    @NotifyChange(['cis','integradoras','contasIntegradoras'])
    def updateClasse(){
        try {
            selectedClasse.merge(flush: true)
            info.value = selectedClasse.designacaoDaConta + " foi atualizado com sucesso!"
            info.style = styleBlue
        } catch (Exception e) {
            info.value = "Erro na atualização dos dados!"
            info.style = styleRed
            System.println(e.toString())
        }

    }
    @Command
    @NotifyChange(['pdcs','selectedPlanoDeContas'])
    def updatePDC(){
        def pdcs= PlanoDeContas.all



            if(selectedPlanoDeContas.ativo){
                pdcs.each {
                    it.ativo=false
                    it.merge(flush: true)
                }
                selectedPlanoDeContas.merge(flush: true)
                info.value ="O Plano de contas "+selectedPlanoDeContas.descricao+" fou atualizado com sucesso!"
                info.style = styleBlue
            }


    }

    @Command
    def updateBanco(){
        selectedBanco.merge(flush: true)
    }

    @Command
    @NotifyChange(['contas'])
    def deleteConta(){
        try {
            novaConta.contas = null

            novaConta.delete(flush: true)
            info.value = 'Conta eliminada com sucesso!'
            info.style = styleBlue
        }catch (Exception e ){
            info.value = 'Erro na eliminação!'
            info.style = styleRed

            System.println(e.toString())
        }
        getContas()
    }

    @Command
    @NotifyChange(['contasIntegradoras'])
    def deleteContaIntegradora(){
        try {
            selectedContaIntegradora.delete flush:  true
            info.value = "A conta integradora "+selectedContaIntegradora.designacaoDaConta+", foi eliminada com sucesso!"
            info.style = styleBlue
        }catch(Exception e){
            System.println(e.toString())
        }
    }
    @Command
    @NotifyChange(['classes'])
    def deleteClasse(){
        try {

            selectedClasse.delete(flush: true)
            info.value = 'Conta eliminada com sucesso!'
            info.style = styleBlue
        }catch (Exception e ){
            info.value = 'Erro na eliminação da classe!'
            info.style = styleRed
            System.println(e.toString())
        }

    }

    @Command
    def showAlerta(){
        info.value ="Se realmente pretende eliminar o item seleccionado, Faça Double Click!"
        info.style=styleRed
    }


    @NotifyChange(["contas","info",'selectedClasse'])
    @Command
    def addClasse(){
        bt_salvar_classe.label = ""
        selectedClasse = new Conta()
    }
    @NotifyChange(["contas","info",'novaConta',"selectedUtilizador"])
    @Command
    def addConta(){
        info.value = ""
        novaConta = new Conta()
        selectedUtilizador = null
    }

    @NotifyChange(["contas","info",'novaConta','novaContaServico','cservicos'])
    @Command
    def addContaServico(){
        info.value = ""
        novaContaServico = new Conta()
    }

    @Command
    @NotifyChange(["selectedContaIntegradora","contasIntegradoras",'contasIntegradoras','integradoras','cis','integradoras','cis',"selectedCI","selectedContaClasse"])
    def addContaIntegradora(){
        info.value = ""
        getCis()
        selectedCI = null
        selectedContaClasse = null
        selectedContaIntegradora = new Conta()

    }


    @Command
    @NotifyChange(["bancos","info","selectedBanco"])
    def addBanco(){
        info.value=""
        selectedBanco = new Banco()
    }
    @NotifyChange(["contas","info"])
    @Command
    void doSearch() {
        info.value = ""
        contas.clear()
        List<Conta> allItems = Conta.all
        if (filter == null || "".equals(filter)) {
            contas.addAll(allItems)
        } else {
            for (Conta item : allItems) {
                if (item.designacaoDaConta.toLowerCase().indexOf(filter.toLowerCase()) >= 0 ||
                        item?.numeroDaConta?.indexOf(filter) >= 0 ||
                        item?.nib?.indexOf(filter) >= 0 ||
                        item?.credito?.toString()?.indexOf(filter) >= 0 ||
                        item?.debito?.toString()?.indexOf(filter) >= 0 ||
                        item?.saldo?.toString()?.indexOf(filter) >= 0 ||
                        item?.banco?.nomeDoBanco?.indexOf(filter) >= 0 ||
                        item?.finalidade?.indexOf(filter) >= 0 ||
                        item?.iban?.indexOf(filter) >= 0 ||
                        item?.swiftCode?.indexOf(filter) >= 0) {
                    contas.add(item)
                }
            }
        }
    }
    String getFilter() {
        return filter
    }

    ListModelList<Cliente> getClientes() {
        if(clientes==null){
            clientes = new ListModelList<Cliente>(Cliente.all)
        }
        return clientes
    }

    ListModelList<String> getFinalidades() {
        if(finalidades ==null){
            finalidades = new ListModelList<String>(['conta_integradora','conta_classe'])
        }
        return finalidades
    }


    Cliente getSelectedCliente() {
        return selectedCliente
    }

    @NotifyChange(["contas",'rconta'])
    void setSelectedCliente(Cliente selectedCliente) {
        rconta = Conta.findByNumeroDaConta(selectedCliente.id.toString())
        this.selectedCliente = selectedCliente
        printExtrato()
    }

    ListModelList<Banco> getBancos() {
        if(bancos==null){
            bancos = new ListModelList<Banco>()
        }
        bancos = Banco.all
        return bancos
    }

    String getSelectedFinalidade() {
        return selectedFinalidade
    }

    void setSelectedFinalidade(String selectedFinalidade) {
        this.selectedFinalidade = selectedFinalidade
    }

    Banco getSelectedBanco() {
        return selectedBanco
    }

    void setSelectedBanco(Banco selectedBanco) {
        this.selectedBanco = selectedBanco
    }

    void setFilter(String filter) {
        this.filter = filter
    }

    Conta getSelectedConta() {
        return selectedConta
    }

    void setSelectedConta(Conta selectedConta) {
        this.selectedConta = selectedConta
        tb_classes.visible = true
    }

    ListModelList<Conta> getContas() {
        if(contas==null){
            contas = new ListModelList<>()
        }
        contas.clear()
        def contasDb = Conta.all
        for (Conta conta in contasDb){
            if(conta.finalidade=="conta_movimento"
            ){
                contas.add(conta)
            }
        }
        return contas
    }
    ListModelList<Conta> getClasses() {
        if(classes==null){
            classes = new ListModelList<>()
        }
        classes.clear()
        classes = Conta.findAllByFinalidade("conta_classe")

        return classes
    }
    Label getInfo() {
        return info
    }

    void setInfo(Label info) {
        this.info = info
    }

    Conta getSelectedContaOrigem() {
        return selectedContaOrigem
    }

    void setSelectedContaOrigem(Conta selectedContaOrigem) {
        this.selectedContaOrigem = selectedContaOrigem
    }

    Conta getSelectedContaDestino() {
        return selectedContaDestino
    }

    void setSelectedContaDestino(Conta selectedContaDestino) {
        this.selectedContaDestino = selectedContaDestino
    }



    ListModelList<Conta> getContaorigens() {
        if(contaorigens==null){
            contaorigens = new ArrayList<Conta>()
        }
        def contas = new ListModelList<Conta>(Conta.all)
        for(Conta conta in contas){
            if(conta.finalidade=="conta_movimento"||conta.finalidade=="conta_capital"||conta.finalidade=="conta_caixa"||conta.finalidade=="fundo_de_maneio"){
                contaorigens.add(conta)
            }

        }
        return contaorigens
    }

    ListModelList<Conta> getContadestinos() {
        if(contadestinos==null){
            contadestinos = new ListModelList<>()
        }
        def contas = new ListModelList<Conta>(Conta.all)
        for(Conta conta in contas){
            if(conta.finalidade=="conta_movimento"||conta.finalidade=="conta_capital"||conta.finalidade=="conta_caixa"||conta.finalidade=="fundo_de_maneio"){
                contadestinos.add(conta)
            }

        }

        return contadestinos
    }

    class Item {
        private String debito
        private String credito
        private String saldo


        Item(String debito, String credito, String saldo) {
            this.debito = debito
            this.credito = credito
            this.saldo = saldo
        }

        String getDebito() {
            return debito
        }

        String getCredito() {
            return credito
        }

        String getSaldo() {
            return saldo
        }
    }
}
