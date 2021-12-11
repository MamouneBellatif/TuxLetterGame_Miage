<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 9 novembre 2021, 16:45
    Author     : bella
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet  version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:tux="http://myGame/tux">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
                <title>scores</title>
            </head>
            <body>
                
                <!-- <h1>Profil</h1>
                <h2> Nom:  <xsl:value-of select="tux:profil/tux:nom"/> </h2>
                <p> Né le: <xsl:value-of select="tux:profil/tux:anniversaire"/></p>
                -->
                <h2>Parties:</h2> 
                <p>
                    <table border="1">
                        <tr bgcolor="#9acd32">
                            <th>Position</th>
                            <th>nom</th>
                            <th>mot</th>
                            <th>score</th>
                        </tr>
                        <xsl:apply-templates select="tux:classement/tux:partie"/>
                    </table>

                    </p>

            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:partie">
        
                <tr>
                    <td> <xsl:value-of select="@position"/> </td>
                    <td> <xsl:value-of select="tux:nom"/> </td>
                    <td> <xsl:value-of select="tux:mot"/> </td>
                    <td> <xsl:value-of select="tux:score"/> </td>
                </tr>
    </xsl:template>

</xsl:stylesheet>
