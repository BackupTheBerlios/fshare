<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:date="*** Processing dates ***">

    <date:month-names>
        <date:month short="jan">janvier</date:month>
        <date:month short="fév">février</date:month>
        <date:month short="mar">mars</date:month>
        <date:month short="avr">avril</date:month>
        <date:month short="mai">mai</date:month>
        <date:month short="jun">juin</date:month>
        <date:month short="jul">juillet</date:month>
        <date:month short="aou">août</date:month>
        <date:month short="sep">septembre</date:month>
        <date:month short="oct">octobre</date:month>
        <date:month short="nov">novembre</date:month>
        <date:month short="déc">décembre</date:month>
    </date:month-names>

    <xsl:template name="date:month-name">
        <!--returns the name of the month from its number-->
        <xsl:param name="month" select="0"/>

        <xsl:value-of select="document('')/*/date:month-names/date:month[$month]"/>
    </xsl:template>

    <xsl:template name="date:format">
        <!--formatting dates "j/m/aaaa" or "aaaa-m-j" -->
        <xsl:param name="date" select="''"/>
        <xsl:param name="day" select="substring-before($date, '/')"/>
        <xsl:param name="month" select="substring-before(substring-after($date, '/'), '/')"/>
        <xsl:param name="year" select="substring-after(substring-after($date, '/'), '/')"/>

        <xsl:choose>
            <xsl:when test="not(contains($date, '-'))">
                <xsl:variable name="month-name">
                    <xsl:call-template name="date:month-name">
                        <xsl:with-param name="month" select="number($month)"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:text/>
                <xsl:value-of select="$day" />
                <xsl:text>&#160;</xsl:text>
                <xsl:value-of select="$month-name" />
                <xsl:text>&#160;</xsl:text>
                <xsl:value-of select="$year" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="date:format">
                    <xsl:with-param name="year" select="substring-before($date, '-')"/>
                    <xsl:with-param name="month" select="substring-before(substring-after($date, '-'), '-')"/>
                    <xsl:with-param name="day" select="substring-after(substring-after($date, '-'), '-')"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="date:julian-day">
        <!--returns the julian day from the date-->
        <xsl:param name="date" select="''"/>
        <xsl:param name="year" select="substring-before($date, '-')"/>
        <xsl:param name="month" select="substring-before(substring-after($date, '-'), '-')"/>
        <xsl:param name="day" select="substring-after(substring-after($date, '-'), '-')"/>

        <xsl:variable name="a" select="floor( ( 14 - $month ) div 12 )"/>
        <xsl:variable name="y" select="$year + 4800 - $a"/>
        <xsl:variable name="m" select="$month + 12 * $a - 3"/>
        <xsl:value-of select="$day + floor( ( 153 * $m + 2 ) div 5 ) + $y * 365 + floor( $y div 4 ) - floor( $y div 100 ) + floor( $y div 400) - 32045"/>
    </xsl:template>

    <xsl:template name="date:absolute-day">
        <!--returns the absolute day from the date-->
        <xsl:param name="date" select="''"/>
        <xsl:param name="year" select="substring-before($date, '-')"/>
        <xsl:param name="month" select="substring-before(substring-after($date, '-'), '-')"/>
        <xsl:param name="day" select="substring-after(substring-after($date, '-'), '-')"/>

        <xsl:variable name="julian">
            <xsl:call-template name="date:julian-day">
                <xsl:with-param name="year" select="$year"/>
                <xsl:with-param name="month" select="$month"/>
                <xsl:with-param name="day" select="$day"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$julian - 1721425"/>
    </xsl:template>
</xsl:stylesheet>