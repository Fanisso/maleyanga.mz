<div class="nav" role="navigation">
    <ul>
        <li><g:link class="list" action="list">Person List</g:link></li>
    </ul>
</div>

<div id="upload-data" class="content scaffold-create" role="main">
    <div class="content scaffold-create" role="main">
        <h3>Upload Data</h3>

        <g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>

        <g:uploadForm action="doUpload">
            <fieldset class="form">
                <input class="btn-warning" type="file" name="file" />
            </fieldset>
            <fieldset class="buttons">
                <g:submitButton class="btn btn-danger" name="doUpload" value="Upload" />
            </fieldset>
        </g:uploadForm>
    </div>


</div>
<br/>

