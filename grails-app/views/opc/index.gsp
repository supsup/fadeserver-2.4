<%@ page import="com.fadeserver.fadecandy.hardware.Opc" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'opc.label', default: 'Opc')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-opc" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                          default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-opc" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="dateCreated"
                              title="${message(code: 'opc.dateCreated.label', default: 'Date Created')}"/>

            <g:sortableColumn property="hashArray"
                              title="${message(code: 'opc.hashArray.label', default: 'Hash Array')}"/>

            <g:sortableColumn property="height" title="${message(code: 'opc.height.label', default: 'Height')}"/>

            <g:sortableColumn property="hexValue" title="${message(code: 'opc.hexValue.label', default: 'Hex Value')}"/>

            <g:sortableColumn property="width" title="${message(code: 'opc.width.label', default: 'Width')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${opcInstanceList}" status="i" var="opcInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${opcInstance.id}">${fieldValue(bean: opcInstance, field: "dateCreated")}</g:link></td>

                <td>${fieldValue(bean: opcInstance, field: "hashArray")}</td>

                <td>${fieldValue(bean: opcInstance, field: "height")}</td>

                <td>${fieldValue(bean: opcInstance, field: "hexValue")}</td>

                <td>${fieldValue(bean: opcInstance, field: "width")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>


     <a id="randomColor" href="#">Reload</a>
     <a id="stopColor" href="#">Stop Random</a>
    <div class="pagination">
        <g:paginate total="${opcInstanceCount ?: 0}"/>
    </div>
</div>



<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

<script>
    $("#randomColor").click(function(){
        $.ajax({
            url: "/fadeserver/opc/index?width=8&height=8"
        }).done(function() {
            $( this ).addClass( "done" );

        });

        return false;
    });


    $("#stopColor").click(function(){
        $.ajax({
            url: "/fadeserver/opc/endTask"
        }).done(function() {
            $( this ).addClass( "done" );

        });

        return false;
    })

</script>

</body>
</html>
