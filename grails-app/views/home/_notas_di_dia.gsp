<%@ page import="mz.maleyanga.documento.Nota" %>

<g:set var="today" value="${new Date().format("MM/dd")}" />

<div class="prop"  >

    <g:each  in="${Nota.list().sort{it.dateCreated}}" status="i" var="n">

        <g:if test="${n.dateCreated.format("MM/dd").equals(today)}">
    <div class="form-group">
       <h4> <div class="col-lg-1"><span  style="background:#${n.colorG}ffe"> ${n.autor}:</span></div></h4>
     <div > <span  style="background:#ccc${n.colorR}">${n.messagem}</span></div>
    </div>
        </g:if>
    </g:each>
</div>
