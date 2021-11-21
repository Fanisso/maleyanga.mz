<%
    def myService = grailsApplication.mainContext.getBean("springSecurityService");
%>
<li class="">
    <a data-toggle="" href="#">
        <span class="label label-default">${myService.currentUser.username}</span>
    </a>

</li>
