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
						      <xsl:call-template name="fichiers"/>
            </body>
        </html>

    </xsl:template>


    <xsl:template name="fichiers">
                <xsl:choose>
                    <xsl:when test="$typeRequete">
                				 <h1>Liste des Clients</h1>
                              		 valeur de type tri : <xsl:value-of select="$typeTri"/>
                        <p/> <a href="index.html">Retour</a>
                          <p/> <a href="fsservlet?requete=&#38;typeRequete=clients&#38;typeTri=name">Tri par nom</a>
                          <p/> <a href="fsservlet?requete=&#38;typeRequete=clients&#38;typeTri=id">Tri par ID</a>
                        <table align="left" border="1">
                      <xsl:choose>
                          <xsl:when test="$typeTri = 'id'">
         								<xsl:for-each select="//etat:client[not(@id = preceding::etat:client/@id)]">
											      <xsl:sort select="@id"/>
                               <tr> <th><xsl:value-of select="@name"/></th>
                              		 <th><xsl:value-of select="@id"/></th>
                             </tr>
												</xsl:for-each>
									</xsl:when>
                  <xsl:when test="$typeTri = 'name'">
         								<xsl:for-each select="//etat:client[not(@id = preceding::etat:client/@id)]">
											      <xsl:sort select="@name"/>
                               <tr> <th><xsl:value-of select="@name"/></th>
                              		 <th><xsl:value-of select="@id"/></th>
                             </tr>
												</xsl:for-each>

                  </xsl:when>
                  </xsl:choose>
                  </table>
                    </xsl:when>
                    <xsl:otherwise>
                <h1>Liste des Fichiers</h1>

										<table align="left" border="1" width="600">
         						<xsl:apply-templates mode="toc" select="//etat:fichier"/>

                    </table>
                    </xsl:otherwise>
                </xsl:choose>
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
