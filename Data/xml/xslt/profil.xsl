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
                <title>profil</title>
            </head>
            <body>
                
                <h1>Profil</h1>
                <h2> Nom:  <xsl:value-of select="tux:profil/tux:nom"/> </h2>
                <p> Né le: <xsl:value-of select="tux:profil/tux:anniversaire"/></p>

                <h2>Parties:</h2>
                <xsl:apply-templates select="tux:profil/tux:parties/tux:partie"/>

            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:partie">
        <p>
            <table border="1">
                <tr bgcolor="#9acd32">
                    <th>Date</th>
                    <th>temps</th>
                    <th>niveau</th>
                    <th>mot</th>
                </tr>
                <tr>
                    <td> <xsl:value-of select="@date"/> </td>
                    <td> <xsl:value-of select="tux:temps"/> </td>
                    <td> <xsl:value-of select="tux:mot/@niveau"/> </td>
                    <td> <xsl:value-of select="tux:mot"/> </td>
                </tr>
            </table>
        </p>
    </xsl:template>

</xsl:stylesheet>
