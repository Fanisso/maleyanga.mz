package mz.maleyanga

import grails.plugin.springsecurity.annotation.Secured
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging

import javax.servlet.http.HttpSession

//import grails.plugin.springsecurity.annotation.Secured

//import javax.servlet.http.HttpSession
@ActionLogging
@Secured(['ROLE_ADMIN','ROLE_GESTOR','ROLE_TESO','ROLE_USER'])
class BasicController implements Serializable {
    private static final long serialVersionUID = 1
    boolean transactional = true

    def grailsApplication
    def utilService = new UtilService()
    def birtReportService

    def terminate = {
        if (session.controllerRedirect && session.controllerRedirect.hasActionToGo()) {
            dontRemoveMessages()
            CurrentAction currectAction = session.controllerRedirect.getCurrentAction()
            //println ' currectAction.controllerName ' + currectAction.controllerName
            //println ' currectAction.actionName     ' + currectAction.actionName

            redirect(controller: currectAction.controllerName, action: currectAction.actionName)
        } else {
            redirect(action: 'fechar')
        }
    }

    def back = {
        redirect(action: 'terminate')
    }

    def fechar = {
        redirect(controller: 'login')
    }

    def cancel = {
        fechar()
    }

    void setErrorMessage(String message) {
        session.errorMessage = message
        session.statusMessage = null
        session.dontRemoveMessages = true
    }

    void setStatusMessage(String message) {
        session.statusMessage = message
        session.errorMessage = null
        session.dontRemoveMessages = true
    }

    void dontRemoveMessages() {
        session.dontRemoveMessages = true
    }

    void removeMessages() {
        utilService.removeMessages(session)
    }

    void updateCurrentAction(String currentAction) {
        utilService.updateCurrentAction(session, currentAction)
    }

    void updateCurrentAction() {
        utilService.updateCurrentAction(session, actionName)
    }

    void printInfoCurrentRequest() {
        println 'controllerName ' + controllerName
        println 'actionName ' + actionName
        println 'params.id ' + params.id
    }

    void setParam(String param, String value) {
        session."${param}" = value
    }

    void setParamsId() {
        session.paramsId = params.id
    }

    void clearParamsId() {
        session.paramsId = null
    }

    void clearParam(String param) {
        session."${param}" = null
    }

    boolean paramsIdExists() {
        return (session.paramsId == null ? false : !session.paramsId.equals(""))
    }

    boolean paramExists(String param) {
        return session.paramsId != null
    }

    long getParamsId() {
        if (paramsIdExists()) {
            long id = Long.parseLong('' + session.paramsId)

            //O id so pode ser invocado uma vez, logo que se invoca deixa de ser disponivel pra evitar ambiguidades
            clearParamsId()

            return id
        }

        return 0
    }


    String getParam(String param) {
        if (paramExists(param)) {
            String paramValue = session."${param}"

            //O 'param' so pode ser invocado uma vez, logo que se invoca deixa de ser disponivel pra evitar ambiguidades
            clearParam(param)

            return paramValue
        }

        return null
    }

    void clearControllerRedirect() {
        println 'clearControllerRedirect'
        session.controllerRedirect = null
    }

    void denyAccess(String previousAction) {
        denyAccess(controllerName, previousAction)
    }

    void denyAccess() {
        denyAccess(controllerName, 'fechar')
    }

    void denyAccess(String previousController, String previousAction) {

        CurrentAction action = new CurrentAction(controllerName: previousController, actionName: previousAction)

        if (!session.controllerRedirect) session.controllerRedirect = new ControllerRedirect()

        session.controllerRedirect.addAction(action)

        session.errorMessage = utilService.message('default.not.access.permition.message')
        render(view: '../errorPage')
    }

    void renderErrorPage(String i18n) {
        denyAccess('index')
        session.errorMessage = utilService.message(i18n)
        render(view: '../errorPage')
    }



    void updateCurrentAction(HttpSession session, String currentAction) {
        session.currentAction = currentAction
        println 'session.currentAction ' + session.currentAction
    }

    def sair = {
        session.furoInstance = null
        session.casotaInstance = null
        session.redeInstance = null
        session.cavaleteBomba = null

        render(view: "/index")
    }

    def printReport = {
      String reportExt = params.reportExt
      String reportName = params.reportName


      params.remove('action')
      params.remove('controller')
      params.remove('reportName')
      def options = birtReportService.getRenderOption(request,params.reportExt)
     // def repParams = birtReportService.getReportParams()

        print birtReportService.reportHome

      def result = birtReportService.runAndRender(reportName, params, options)
        Date date = new Date()
        if (params.id.equals(null)) {
            System.println("  if Params.id" + params.id)
            // response.setHeader("Content-disposition", "attachment; filename=" + reportName + date.format("dd-MM-yy") + "." + reportExt)
            response.setHeader("Content-disposition", "inline; filename=" + reportName + date.format("dd-MM-yy") + "." + reportExt)
        } else {
            System.println("Else Params.id" + params.id)
            response.setHeader("Content-disposition", "inline; filename=" + reportName + params?.id + "." + reportExt)
        }

        response.contentType = 'application/pdf'
      response.outputStream << result.toByteArray()
      return false
    }



}
