<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:zoo="urn:unice:minfo-2004"
    xmlns:doc="http://www.unice.fr/minfo/2004"
    xmlns:date="*** Processing dates ***"
    exclude-result-prefixes="zoo doc date">

    <xsl:import href="dates.xsl"/>
    <xsl:output method="html"/>
    <xsl:param name="liste-par-nom" select="false()" />

    <xsl:template match="/">
        <html>
            <head>
                <title>Zoo</title>
                <link href="zoo.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <h1>Bienvenue au Zoo</h1>
                <xsl:choose>
                    <xsl:when test="$liste-par-nom">
                        <ul>
                            <xsl:apply-templates mode="toc" select="//zoo:nom">
                                <xsl:sort select="."/>
                            </xsl:apply-templates>
                        </ul>
                    </xsl:when>
                    <xsl:otherwise>
                        <ul>
                            <xsl:apply-templates mode="toc" select="//zoo:nom">
                                <xsl:sort select="../@date-naissance"/>
                            </xsl:apply-templates>
                        </ul>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:apply-templates/>
            </body>
        </html>

    </xsl:template>

    <xsl:template match="zoo:nom" mode="toc">
        <li><a href="#{generate-id()}"><xsl:value-of select="."/></a></li>
    </xsl:template>

    <xsl:template match="doc:info|doc:attention|doc:danger">
        <xsl:choose>
            <xsl:when test="parent::zoo:dauphins or parent::zoo:sélaciens">
                <tr>
                    <td colspan="6"><xsl:call-template name="doc:avertissement"/></td>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="doc:avertissement"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="doc:avertissement">
	<table class="{local-name()}" align="center">
            <tr>
                <td><img src="img/{local-name()}.gif" /></td>
                <td><xsl:apply-templates /></td>
            </tr>
	</table>
    </xsl:template>

    <xsl:template match="doc:b"><b><xsl:apply-templates /></b></xsl:template>
    <xsl:template match="doc:i"><i><xsl:apply-templates /></i></xsl:template>
    <xsl:template match="doc:em"><em><xsl:apply-templates /></em></xsl:template>
    <xsl:template match="doc:strong"><strong><xsl:apply-templates /></strong></xsl:template>

    <xsl:template match="zoo:dauphins|zoo:sélaciens">
        <h2><xsl:value-of select="local-name()" /></h2>
        <table border="1">
            <tr>
                <th>Nom</th>
                <th>Date naissance</th>
                <th>Type</th>
                <th>Taille</th>
                <th>Poids</th>
                <th>Sexe</th>
            </tr>
            <xsl:apply-templates/>
        </table>
    </xsl:template>

    <xsl:template match="zoo:dauphin|zoo:requin">
	<tr>
            <td><a name="{generate-id(zoo:nom)}"><xsl:value-of select="zoo:nom" /></a></td>
            <td>
                <xsl:call-template name="date:format">
                    <xsl:with-param name="date" select="@date-naissance"/>
                </xsl:call-template>
            </td>
            <td><xsl:value-of select="@espèce" /><br /><i><xsl:value-of select="@nom-savant" /></i></td>
            <td><xsl:value-of select="zoo:taille" />&#160;<xsl:value-of select="zoo:taille/@unité" /></td>
            <td><xsl:value-of select="zoo:poids" />&#160;<xsl:value-of select="zoo:poids/@unité" /></td>
            <td><xsl:value-of select="zoo:sexe" /></td>
            <xsl:if test="@photo">
                <td><img src="img/{@photo}" alt="{zoo:nom}" /></td>
            </xsl:if>
	</tr>
    </xsl:template>

</xsl:stylesheet>
