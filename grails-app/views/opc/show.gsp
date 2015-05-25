<%@ page import="com.fadeserver.fadecandy.hardware.Opc" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'opc.label', default: 'Opc')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-opc" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                          default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-opc" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list opc">

        <g:if test="${opcInstance?.dateCreated}">
            <li class="fieldcontain">
                <span id="dateCreated-label" class="property-label"><g:message code="opc.dateCreated.label"
                                                                               default="Date Created"/></span>

                <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate
                        date="${opcInstance?.dateCreated}"/></span>

            </li>
        </g:if>

        <g:if test="${opcInstance?.hashArray}">
            <li class="fieldcontain">
                <span id="hashArray-label" class="property-label"><g:message code="opc.hashArray.label"
                                                                             default="Hash Array"/></span>

            </li>
        </g:if>

        <g:if test="${opcInstance?.height}">
            <li class="fieldcontain">
                <span id="height-label" class="property-label"><g:message code="opc.height.label"
                                                                          default="Height"/></span>

                <span class="property-value" aria-labelledby="height-label"><g:fieldValue bean="${opcInstance}"
                                                                                          field="height"/></span>

            </li>
        </g:if>

        <g:if test="${opcInstance?.hexValue}">
            <li class="fieldcontain">
                <span id="hexValue-label" class="property-label"><g:message code="opc.hexValue.label"
                                                                            default="Hex Value"/></span>

                <span class="property-value" aria-labelledby="hexValue-label"><g:fieldValue bean="${opcInstance}"
                                                                                            field="hexValue"/></span>

            </li>
        </g:if>

        <g:if test="${opcInstance?.width}">
            <li class="fieldcontain">
                <span id="width-label" class="property-label"><g:message code="opc.width.label" default="Width"/></span>

                <span class="property-value" aria-labelledby="width-label"><g:fieldValue bean="${opcInstance}"
                                                                                         field="width"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: opcInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${opcInstance}"><g:message code="default.button.edit.label"
                                                                                    default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
