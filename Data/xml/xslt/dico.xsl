<?xml version="1.0" encoding="UTF-8"?>

<!--
     Document   : dico.xsl
     Created on : 9 novembre 2021, 15:36
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

                <title>Dictionnaire</title>
            </head>
            <body>
             <table border="1">  <xsl:apply-templates select="tux:dictionnaire/tux:mot">
                    <xsl:sort select=".">
                        
                    </xsl:sort>
                        </xsl:apply-templates>
              </table>
               
            </body>
        </html>
    </xsl:template>


<xsl:template match="tux:mot">
<tr>
<td> Mot:  <xsl:value-of select="."/> </td>
<td> Niveau: <xsl:value-of select="@niveau"/> </td>
</tr>
</xsl:template>

    <xsl:template match="tux:profil">
       <tr>
        <th> Partie </th>
        <td> Date:  <xsl:value-of select="@date"/> </td>        
        <td> Temps:  <xsl:value-of select="@date"/> </td>
        <td> Niveau:  <xsl:value-of select="@date"/> </td>
        <td> Mot: <xsl:value-of select="./mot"/> </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
