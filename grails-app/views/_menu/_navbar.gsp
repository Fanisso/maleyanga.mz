<nav id="Navbar" class="navbar navbar-fixed-top navbar-inverse" role="navigation">
	<div class="container">
	
	    <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

            <a class="navbar-brand" href="${createLink(uri: '/home')}">
                <img style="color: #5EB544" class="logo" src="${resource(dir: 'images', file: 'maleyanga.png')}"
                     alt="${meta(name: 'app.name')}" width="37px" height="32px"/>
                ${meta(name: 'app.name')}
                <small>v${meta(name: 'app.version')}</small>
            </a>
        </div>

        <div class="collapse navbar-collapse navbar-ex1-collapse" role="navigation">
            <ul class="nav navbar-nav navbar-right">
                <g:render template="/_menu/settings"/>
                <g:render template="/_menu/entidade"/>
                <g:render template="/_menu/out"/>
            </ul>

        </div>
    </div>
</nav>
