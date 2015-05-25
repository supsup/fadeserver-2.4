<%--
  Created by IntelliJ IDEA.
  User: hiroix
  Date: 5/16/15
  Time: 1:38 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Single Pixel</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>

<body>

<form id="myData">
   Color: <input name="hexColor" id="myColor" value="" /> random if empty <br />
   Index:  <input name="index" id="myIndex" value="" /> 0 if empty <br />
   Width of Array:   <input name="width" id="width" value="" /> 8 if empty <br />
   Height of Array:  <input name="height" id="height" value="" /> 8 if empty <br />
</form>

<a href="/fadeserver/singlePixel/turnOn" id="turnOn">
    Turn On
</a>
<br />
<a href="/fadeserver/singlePixel/turnOff" id="turnOff">
    Turn Off
</a>


<div id="running">0</div>
<script>
    $("#turnOn").click(function(){

        var myParams = $("#myData").serialize();
        var myUrl = $(this).attr("href");
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