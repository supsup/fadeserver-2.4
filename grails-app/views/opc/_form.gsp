<%@ page import="com.fadeserver.fadecandy.hardware.Opc" %>



<div class="fieldcontain ${hasErrors(bean: opcInstance, field: 'hashArray', 'error')} required">
    <label for="hashArray">
        <g:message code="opc.hashArray.label" default="Hash Array"/>
        <span class="required-indicator">*</span>
    </label>

</div>

<div class="fieldcontain ${hasErrors(bean: opcInstance, field: 'height', 'error')} required">
    <label for="height">
        <g:message code="opc.height.label" default="Height"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field name="height" type="number" value="${opcInstance.height}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: opcInstance, field: 'hexValue', 'error')} required">
    <label for="hexValue">
        <g:message code="opc.hexValue.label" default="Hex Value"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="hexValue" required="" value="${opcInstance?.hexValue}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: opcInstance, field: 'width', 'error')} required">
    <label for="width">
        <g:message code="opc.width.label" default="Width"/>
        <span class="required-indicator">*</span>
    </label>
    <g:field name="width" type="number" value="${opcInstance.width}" required=""/>

</div>

