<?php 
$swaggerInserts = $_MB['PP']['ppSwaggerInserts'];
$swaggerDescription = $_MB['PP']['ppSwaggerDescription'];
$swaggerVersion = $_MB['PP']['ppSwaggerVersion'];
$swaggerTitle = $_MB['PP']['ppSwaggerTitle'];
$swaggerHost = $_MB['PP']['ppSwaggerHost'];
$swaggerBasePath = $_MB['PP']['ppSwaggerBasePath'];
echo	"{\r\n";
echo	"\t\"swagger\" : \"2.0\",\r\n";
echo	"\t\t\"info\" : {\r\n";
echo	"\t\t\"description\" : \"$swaggerDescription\",\r\n";
echo	"\t\t\"version\" : \"$swaggerVersion\",\r\n";
echo	"\t\t\"title\" : \"$swaggerTitle\",\r\n";
echo	"\t\t\"license\" : {\r\n";
echo	"\t\t\t\"name\" : \"Apache 2.0\",\r\n";
echo	"\t\t\t\"url\" : \"http://www.apache.org/licenses/LICENSE-2.0.html\"\r\n";
echo	"\t\t}\r\n";
echo	"\t},\r\n";
echo	"\t\"host\" : \"$swaggerHost\",\r\n";
echo	"\t\"basePath\" : \"$swaggerBasePath\",\r\n";
echo	"\t\"schemes\" : [ \"http\" ],\r\n";
echo	"\t\"paths\" : {\r\n";
echo	"$swaggerInserts\r\n";
echo	"\t}\r\n";
echo	"}";
?>