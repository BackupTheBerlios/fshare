<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:etat="http://deptinfo.unice.fr/minfo/ns/p2p"
    xmlns:doc="http://www.unice.fr/minfo/2004"
    xmlns:date="*** Processing dates ***"
    exclude-result-prefixes="etat doc date">


    <xsl:output method="html"/>
    <xsl:param name="typeRequete" select="clients"/>
		<xsl:param name="typeTri" select="." />

    <xsl:template match="/">
      <link href="zoo.css" rel="stylesheet" type="text/css"/>
      <xsl:call-template name="fichiers"/>

    </xsl:template>


    <xsl:template name="fichiers">
                <xsl:choose>
                    <xsl:when test="$typeRequete = 'clients'">
                				 <h1>Liste des Clients</h1>
                       <p/> <a href="index.html">Retour</a>
                          <p/> <a href="fsservlet?requete=&#38;typeRequete=clients&#38;typeTri=name">Tri par nom</a>
                        <p/>  <a href="fsservlet?requete=&#38;typeRequete=clients&#38;typeTri=id">Tri par ID</a>
                        <p/>
                          <table align="left" border="1">
                      		<tr><th><b>NOM</b></th><th><b>ID</b></th></tr>
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
                    <xsl:when test="$typeRequete = 'fichiers'">
                <h1>Liste des Fichiers</h1>

      <p/> <a href="index.html">Retour</a>
      <p/> <a href="fsservlet?requete=&#38;typeRequete=fichiers&#38;typeTri=name">Tri par nom</a>
      <p/>  <a href="fsservlet?requete=&#38;typeRequete=fichiers&#38;typeTri=taille">Tri par taille décroissante</a>
      <p/>  <a href="fsservlet?requete=&#38;typeRequete=fichiers&#38;typeTri=type">Tri par type</a>
      <p/>

			<table align="left" border="1" width="600">
      <tr> <th><b>Nom</b></th> <th><b>Taille (en octets)</b></th> <th><b>Date</b></th> <th><b>Type</b></th>
      </tr>
			<xsl:choose>
        <xsl:when test="$typeTri='name'">
          <xsl:apply-templates  select="//etat:fichier">
             <xsl:sort select="etat:nom"/>
          </xsl:apply-templates>
        </xsl:when>
        <xsl:when test="$typeTri='taille'">
        <xsl:apply-templates  select="//etat:fichier">
             <xsl:sort select="etat:taille" order="descending" data-type="number"/>
          </xsl:apply-templates>
        </xsl:when>
        <xsl:when test="$typeTri='type'">
        <xsl:apply-templates  select="//etat:fichier">
             <xsl:sort select="etat:type"/>
        </xsl:apply-templates>
        </xsl:when>
        </xsl:choose>
        </table>
      	</xsl:when>
      <xsl:when test="$typeRequete = 'clientsparfichiers'">
        <h1> Liste des clients par fichiers</h1>
        <p/> <a href="index.html">Retour</a>
        <xsl:apply-templates select="//etat:fichier" mode="clients"/>


      </xsl:when>
       <xsl:when test="$typeRequete = 'fichiersparclients'">
        <h1> Liste des fichiers par clients</h1>
				<p/> <a href="index.html">Retour</a>
        <xsl:for-each select="//etat:client[not(@id = preceding::etat:client/@id)]">
						<xsl:variable name="idcli"><xsl:value-of select="@id"/>
						</xsl:variable>
            <h2><xsl:value-of select="@name"/> -- <xsl:value-of select="$idcli"/></h2>
            <ul>
            <xsl:for-each select="//etat:fichier">
                <xsl:for-each select="etat:client">
              		<xsl:if test="@id=$idcli">
                        <li> <xsl:value-of select="preceding-sibling::etat:nom"/> --
                            <xsl:value-of select="etat:date/@jour"/>/<xsl:value-of select="etat:date/@mois"/>/<xsl:value-of select="etat:date/@année"/> (<xsl:value-of select="etat:date/@heure"/>:<xsl:value-of select="etat:date/@minute"/>)
             						</li>
                            </xsl:if>
              </xsl:for-each>
		           </xsl:for-each>

              </ul>
          </xsl:for-each>


      </xsl:when>


      </xsl:choose>
    </xsl:template>

    <xsl:template match="etat:fichier" >
      <tr> <th><xsl:value-of select="etat:nom"/></th>
      			 <th><xsl:value-of select="etat:taille"/></th>
             <th><xsl:value-of select="etat:date/@jour"/>/<xsl:value-of select="etat:date/@mois"/>/<xsl:value-of select="etat:date/@année"/> (<xsl:value-of select="etat:date/@heure"/>:<xsl:value-of select="etat:date/@minute"/>)</th>
             <th><xsl:value-of select="etat:type"/></th>

        </tr>
    </xsl:template>

    <xsl:template match="etat:fichier" mode="clients">

      <h2><xsl:value-of select="etat:nom"/></h2>
			<ul>
        <xsl:for-each select="etat:client">
        <li> <xsl:value-of select="@id"/>  --  <xsl:value-of select="@name"/>  -- <th><xsl:value-of select="etat:date/@jour"/>/<xsl:value-of select="etat:date/@mois"/>/<xsl:value-of select="etat:date/@année"/> (<xsl:value-of select="etat:date/@heure"/>:<xsl:value-of select="etat:date/@minute"/>)</th></li>
     </xsl:for-each>
     </ul>
    </xsl:template>


    <xsl:template match="etat:client" >


    </xsl:template>


</xsl:stylesheet>
