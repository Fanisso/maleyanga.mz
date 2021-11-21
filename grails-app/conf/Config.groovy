// locations to search for config files that get merged into the main config;
grails.config.locations = ["file:grails-app/conf/NexmoConfig.groovy"]
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}
grails.attachmentable.poster.evaluator = { request.user }
// grails.attachmentable.poster.evaluator = { getPrincipal() } // enable this when use spring security core
// grails.attachmentable.searchableFileConverter="attachmentFileConverter"  // for searchable plugin

grails.databinding.convertEmptyStringsToNull = false

grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']


// E-mail config
grails.mail.host="smtp.gmail.com"
grails.mail.port=465
grails.mail.username="fmmicrocredito@gmail.com"
grails.mail.password="fm2010**"
grails.mail.from="fmmicrocredito@gmail.com"
props = ["mail.smtp.auth":"true",
		 "mail.smtp.socketFactory.port":"465",
		 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
		 "mail.smtp.socketFactory.fallback":"false"]
grails.mail.props = ['mail.smtp.auth': "true",
                     "mail.smtp.starttls.enable": "false",
                     "mail.from":"fmmicrocredito@gmail.com"]
grails.mail.javaMailProperties = ["mail.smtp.auth":"true",
                                  "mail.smtp.socketFactory.port":"465",
                                  "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                                  "mail.smtp.socketFactory.fallback":"false"]

// ADD BY DEVELOPER
grails.assets.less.compile = 'less4j'
grails.assets.plugin."twitter-bootstrap".excludes = ["**/*.less"]
grails.assets.plugin."twitter-bootstrap".includes = ["bootstrap.less"]
/*// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'mz.maleyanga.security.SecUtilizador'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'mz.maleyanga.security.SecUtilizadorSecRole'
grails.plugin.springsecurity.authority.className = 'mz.maleyanga.security.SecRole'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'*//**//*js*//**':                      ['permitAll'],
	'*//**//*css*//**':                     ['permitAll'],
	'*//**//*images*//**':                  ['permitAll'],
	'*//**//*favicon.ico':                ['permitAll'],
    '/login*//**':                      ['permitAll'],
    '/logout*//**':                     ['permitAll'],
    '/j_spring_security_switch_user': ['ROLE_ADMIN'],
    '/j_spring_security_exit_user':   ['permitAll'],
    '/js/admin*//**':                  ['ROLE_ADMIN'],
    '/maleyanga*//**':                   ['permitAll'],
    '/h2-console*//**':                   ['permitAll'],
    '/maleyanga/h2-console*//**':          ['permitAll'],
    '/maleyanga*//**':                   ['permitAll'],
    '/searchable*//**':                   ['permitAll']
]*/

grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/home/index'
//grails.serverURL = "http://localhost:8080/pdp"
// ADD FOR  ":export:1.6"
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text-plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      pdf: 'application/pdf',
                      rtf: 'application/rtf',
                      excel: 'application/vnd.ms-excel',
                      ods: 'application/vnd.oasis.opendocument.spreadsheet',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
]

// 2. turn off silent GORM errors
//grails.gorm.failOnError = true

grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']



// Added by the Spring Security Core plugin:

grails.plugin.springsecurity.userLookup.userDomainClassName = 'mz.maleyanga.security.Utilizador'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'mz.maleyanga.security.UtilizadorRole'
grails.plugin.springsecurity.authority.className = 'mz.maleyanga.security.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/':                              ['permitAll'],
	'/index':                         ['permitAll'],
	'/index.gsp':                     ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/zkau/**':                 	 ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll'],
    '/login/**':                      ['permitAll'],
    '/logout/**':                     ['permitAll'],
    '/j_spring_security_switch_user': ['ROLE_ADMIN'],
    '/j_spring_security_exit_user':   ['permitAll'],
    '/js/admin/**':                  ['ROLE_ADMIN'],
    '/pdp/**':                   ['permitAll'],
    '/h2-console/**':                   ['permitAll'],
    '/searchable/**':                   ['permitAll']
]

grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/home/index'

/*ADD By Developer*/
grails.views.default.codec = "html"
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// Added by the JQuery Validation UI plugin:
jqueryValidationUi {
	errorClass = 'error'
	validClass = 'valid'
	onsubmit = true
	renderErrorsOnTop = false
	
	qTip {
		packed = true
	  classes = 'ui-tooltip-red ui-tooltip-shadow ui-tooltip-rounded'  
	}
	
	/*
	  Grails constraints to JQuery Validation rules mapping for client side validation.
	  Constraint not found in the ConstraintsMap will trigger remote AJAX validation.
	*/
	StringConstraintsMap = [
		blank:'required', // inverse: blank=false, required=true
		creditCard:'creditcard',
		email:'email',
		inList:'inList',
		minSize:'minlength',
		maxSize:'maxlength',
		size:'rangelength',
		matches:'matches',
		notEqual:'notEqual',
		url:'url',
		nullable:'required',
		unique:'unique',
		validator:'validator'
	]
	
	// Long, Integer, Short, Float, Double, BigInteger, BigDecimal
	NumberConstraintsMap = [
		min:'min',
		max:'max',
		range:'range',
		notEqual:'notEqual',
		nullable:'required',
		inList:'inList',
		unique:'unique',
		validator:'validator'
	]
	
	CollectionConstraintsMap = [
		minSize:'minlength',
		maxSize:'maxlength',
		size:'rangelength',
		nullable:'required',
		validator:'validator'
	]
	
	DateConstraintsMap = [
		min:'minDate',
		max:'maxDate',
		range:'rangeDate',
		notEqual:'notEqual',
		nullable:'required',
		inList:'inList',
		unique:'unique',
		validator:'validator'
	]
	
	ObjectConstraintsMap = [
		nullable:'required',
		validator:'validator'
	]
	
	CustomConstraintsMap = [
		phone:'true', // International phone number validation
		phoneUS:'true',
		alphanumeric:'true',
		letterswithbasicpunc:'true',
    lettersonly:'true'
	]


}

birt {
	reportHome = "Reports" // will be in the webapp directory during development
	//imageUrl = "images/report" // this is a subdirectory of the images dir in the webapp directory, where BIRT stores the chart-images.
}






