package mz.maleyanga

import org.codehaus.groovy.grails.commons.ApplicationHolder

import javax.servlet.http.HttpSession

class UtilService {

    boolean transactional = true
    def SQLHelperService
    
    void removeMessages(HttpSession session) {
    	
    	if (session.dontRemoveMessages == true){
            session.errorMessage            = null
        	session.statusMessage			= null
   		}
   		
   		session.dontRemoveMessages = true
   }
   

   void updateCurrentAction (HttpSession session, String currentAction){
    		session.currentAction = currentAction 
    }
   


    ArrayList<Integer> generateListInteger(int qtdIntegers){
          ArrayList<Integer> integers = new ArrayList<Integer>()

          for (int i=1; i <= qtdIntegers; i++) integers.add(i)

          return integers
    }

    ApplicationHolder applicationTagLib(){
        def ctx = ApplicationHolder.getApplication().getMainContext()
        return  ctx.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }

    String message(String i18n){
      def ctx = ApplicationHolder.getApplication().getMainContext()
      org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib  applicationTagLib = ctx.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')

        return applicationTagLib.message(code :i18n)
    }

}
