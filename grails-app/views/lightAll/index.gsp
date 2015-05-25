<%--
  Created by IntelliJ IDEA.
  User: hiroix
  Date: 5/10/15
  Time: 12:06 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>

<body>

<h1>
    Light All Demo
</h1>


<form id="myData">
    Color: <input name="hexColor" id="myColor" value="" /> random color if empty <br />
    Width of Array:   <input name="width" id="width" value="" /> 8 if empty<br />
    Height of Array:  <input name="height" id="height" value="" /> 8 if empty<br />
</form>

<a href="/fadeserver/lightAll/turnOn" id="turnOn">
    Turn On
</a>
<br />
<a href="/fadeserver/lightAll/turnOff" id="turnOff">
    Turn Off
</a>

<div id="running">0</div>
<script>
    $("#turnOn").click(function(){
        var myUrl = $(this).attr("href");
        var myParams = $("#myData").serialize();
        console.log(myUrl);

        $.ajax({
            url: myUrl,
            method: "POST",
            data: myParams
        }).done(function() {
            var $myTargetDiv = $("#running");
            var num = $myTargetDiv.text();
            num++;
            console.log(num)
            $myTargetDiv.html(num);
        });

        return false;
    });

    $("#turnOff").click(function(){
        var myUrl = $(this).attr("href");
        var myParams = $("#myData").serialize();

        $.ajax({
            url: myUrl,
            data: myParams
        }).done(function() {
            var $myTargetDiv = $("#running");
            $myTargetDiv.html("0");
        });
        return false;
    });


</script>



</body>
</html>