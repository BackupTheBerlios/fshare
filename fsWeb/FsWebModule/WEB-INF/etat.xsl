<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:etat="http://deptinfo.unice.fr/minfo/ns/p2p"
    xmlns:doc="http://www.unice.fr/minfo/2004"
    xmlns:date="*** Processing dates ***"
    exclude-result-prefixes="etat doc date">

    <xsl:import href="dates.xsl"/>
    <xsl:output method="html"/>
    <xsl:param name="typeRequete" select="false()" />
		<xsl:param name="typeTri" select="." />

    <xsl:template match="/">
        <html>
            <head>
                <title>Zoo</title>
                <link href="style.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <xsl:choose>
                    <xsl:when test="$typeRequete">
                				 <h1>Liste des Clients</h1>
                        <table align="left" border="1">
         								<xsl:for-each select="//etat:client[not(@id = preceding::etat:client/@id)]">
													<xsl:sort select="@id"/>
                           <tr> <th><xsl:value-of select="@name"/></th>
                              		 <th><xsl:value-of select="@id"/></th>
                             </tr>
												</xsl:for-each>

                    </table>
                    </xsl:when>
                    <xsl:otherwise>
                <h1>Liste des Fichiers</h1>

										<table align="left" border="1" width="600">
         						<xsl:apply-templates mode="toc" select="//etat:fichier"/>

                    </table>
                    </xsl:otherwise>
                </xsl:choose>

            </body>
        </html>

    </xsl:template>

    <xsl:template match="etat:fichier" mode="toc">
        <tr> <th><xsl:value-of select="etat:nom"/></th>
      			 <th><xsl:value-of select="etat:taille"/></th>
             <th><xsl:value-of select="etat:date/@jour"/>/<xsl:value-of select="etat:date/@mois"/>/<xsl:value-of select="etat:date/@année"/> (<xsl:value-of select="etat:date/@heure"/>:<xsl:value-of select="etat:date/@minute"/>)</th>
             <th><xsl:value-of select="etat:type"/></th>

        </tr>
    </xsl:template>

    <xsl:template match="etat:client" mode="toc">


    </xsl:template>


</xsl:stylesheet>
