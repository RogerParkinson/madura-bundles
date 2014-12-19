<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
	<xsl:param name="WatermarkPortrait">none</xsl:param>
	<xsl:param name="WatermarkLandscape">none</xsl:param>
	<xsl:param name="Year">2007</xsl:param>
	<xsl:param name="ProductVersion">Madura 3</xsl:param>
	<xsl:param name="Company">Prometheus Consulting</xsl:param>
	<xsl:param name="BaseDir">./</xsl:param>

	<xsl:attribute-set name="normal">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="normalkeep">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
		<xsl:attribute name="page-break-after">avoid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="bold">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="cell-padding">
		<xsl:attribute name="padding-start">2pt</xsl:attribute>
		<xsl:attribute name="padding-end">2pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="th">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
		<xsl:attribute name="background-color">gray</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="italic">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">italic</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="figure">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="font-style">italic</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="graphic">
		<!--<xsl:attribute name="content-width">1in</xsl:attribute>-->
	</xsl:attribute-set>

	<xsl:attribute-set name="code">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Courier</xsl:attribute>
		<xsl:attribute name="white-space-treatment">preserve</xsl:attribute>
		<xsl:attribute name="linefeed-treatment">preserve</xsl:attribute>
		<xsl:attribute name="white-space-collapse">false</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="courier">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Courier</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="note">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.0cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
		<xsl:attribute name="space-after">0.1cm</xsl:attribute>
		<xsl:attribute name="background-color">White</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="warning">
		<xsl:attribute name="text-align">left</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.0cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
		<xsl:attribute name="space-after">0.1cm</xsl:attribute>
		<xsl:attribute name="background-color">White</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="foot">
		<xsl:attribute name="font-size">8pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H1">
		<xsl:attribute name="font-size">20pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.3cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.0cm</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
		<!--<xsl:attribute name="page-break-before">always</xsl:attribute>-->
	</xsl:attribute-set>

	<xsl:attribute-set name="H2">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.3cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.3cm</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H3">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-style">italic</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.2cm</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H4">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<!--<xsl:attribute name="font-style">italic</xsl:attribute>-->
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.1cm</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H5">
		<xsl:attribute name="font-size">10pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<!--<xsl:attribute name="font-style">italic</xsl:attribute>-->
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
		<xsl:attribute name="padding-bottom">0.1cm</xsl:attribute>
		<xsl:attribute name="padding-top">0.1cm</xsl:attribute>
		<xsl:attribute name="text-align">left</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H1TOC">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="H1TOC">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H2TOC">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H3TOC">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-style">italic</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="cell-padding">
		<!-- <xsl:attribute name="padding-start">2pt</xsl:attribute> -->
		<xsl:attribute name="padding-end">2pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">4pt</xsl:attribute>
	</xsl:attribute-set>


	<xsl:attribute-set name="H2TOC">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="H3TOC">
		<xsl:attribute name="font-size">12pt</xsl:attribute>
		<xsl:attribute name="font-weight">normal</xsl:attribute>
		<xsl:attribute name="font-style">italic</xsl:attribute>
		<xsl:attribute name="font-family">Helvetica</xsl:attribute>
	</xsl:attribute-set>

	<xsl:template name="PageMasterPortrait">
		<fo:simple-page-master
			master-name="oddLayout"
			page-height="29.5cm"
			page-width="21cm" 
			margin-top="0.1cm"
			margin-bottom="0.1cm"
			margin-left="1cm"
			margin-right="1cm">
			<fo:region-body 
				margin-top="0.75in" 
				margin-bottom="0.8in" 
				margin-left="0.75in" 
				margin-right="0.5in" 
				extent="0.5in" 
				region-name="text"/>
			<fo:region-before region-name="oddHead" extent="0.5in"/>
			<fo:region-after region-name="folio" extent="0.6in"/>
		</fo:simple-page-master>
		<fo:simple-page-master
			master-name="evenLayout"
			page-height="29.5cm"
			page-width="21cm" 
			margin-top="0.1cm"
			margin-bottom="0.1cm"
			margin-left="1cm"
			margin-right="1cm">
			<fo:region-body 
				margin-top="0.75in" 
				margin-bottom="0.8in" 
				margin-left="0.5in" 
				margin-right="0.75in" 
				extent="0.5in" 
				region-name="text"/>
			<fo:region-before region-name="evenHead" extent="0.5in"/>
			<fo:region-after region-name="folio" extent="0.6in"/>
		</fo:simple-page-master>
		<fo:simple-page-master
			master-name="landscapeLayout"
			page-height="21cm"
			page-width="30cm" 
			margin-top="0.1cm"
			margin-bottom="0.1cm"
			margin-left="1cm"
			margin-right="1cm">
			<fo:region-body 
				margin-top="0.75in" 
				margin-bottom="0.8in" 
				margin-left="0.5in" 
				margin-right="0.75in" 
				extent="0.5in" 
				region-name="text"/>
			<fo:region-before region-name="evenHead" extent="0.5in"/>
			<fo:region-after region-name="folio" extent="0.6in"/>
		</fo:simple-page-master>
		<fo:page-sequence-master master-name="landscape">
			<fo:single-page-master-reference master-reference="landscapeLayout"/>
		</fo:page-sequence-master>
		<fo:page-sequence-master master-name="oddEven">
			<fo:repeatable-page-master-alternatives>
				<fo:conditional-page-master-reference odd-or-even="odd" master-reference="oddLayout"/>
				<fo:conditional-page-master-reference odd-or-even="even" master-reference="evenLayout"/>
			</fo:repeatable-page-master-alternatives>
		</fo:page-sequence-master>
 	</xsl:template>
	
	<xsl:template name="FirstPageFooter">
		<fo:block xsl:use-attribute-sets="foot" space-before="0.1cm">
			<xsl:variable name="rev" select="Revision"/>
			<fo:block><xsl:text>&#xA9;</xsl:text><xsl:value-of select="$Year"/><xsl:text> </xsl:text><xsl:value-of select="$Company"/></fo:block>
			<fo:block><xsl:text>Document Revision: </xsl:text><xsl:value-of select="translate($rev,'$',' ')"/></fo:block>
		</fo:block>
 	</xsl:template>
	<xsl:template name="PageFooter">
		<fo:block xsl:use-attribute-sets="foot" space-before="0.1cm">
			<fo:block><xsl:text>&#xA9;</xsl:text><xsl:value-of select="$Year"/><xsl:text> </xsl:text><xsl:value-of select="$Company"/></fo:block>
			<fo:block><xsl:value-of select="/doc/title/SubTitle"/><xsl:text> </xsl:text><xsl:value-of select="$ProductVersion"/><xsl:text> </xsl:text></fo:block>
			<fo:block><xsl:text>- </xsl:text><fo:page-number/><xsl:text> -</xsl:text></fo:block>
		</fo:block>
 	</xsl:template>
		    
	<xsl:template name="FirstPage">
        <fo:block font-size="36pt" text-align="center" space-before="20pt">
        	<xsl:value-of select="MainTitle"/>
        </fo:block>
        <fo:block font-size="28pt" text-align="center" space-before="40pt">
        	<xsl:value-of select="SubTitle"/>
        </fo:block>
    	<xsl:if test="string-length(image) &gt; 0">
	    	<fo:block text-align="center" space-before="4cm">
         	<fo:external-graphic 
         		xsl:use-attribute-sets="graphic"
         		height="100%" 
         		content-width="6cm" 
         		border-style="solid" 
				border-color="black" 
				border-width="0.25mm"
				scaling="uniform" 
				scaling-method="resample-any-method"
				text-align="center" >
         	<xsl:attribute name="src">
         		<xsl:text>url(</xsl:text>
         		<xsl:value-of select="$BaseDir"/>
         		<xsl:text>images/</xsl:text>
         		<xsl:value-of select="image"/>
         		<xsl:text>)</xsl:text>
         	</xsl:attribute>
         	</fo:external-graphic>
         	</fo:block>
        	</xsl:if>
    	<xsl:if test="string-length(Logo) &gt; 0">
	    	<fo:block text-align="center" space-before="4cm">
         	<fo:external-graphic 
         		xsl:use-attribute-sets="graphic"
         		height="100%" 
         		content-width="6cm" 
         		border-style="solid" 
				border-color="black" 
				border-width="0.25mm"
				scaling="uniform" 
				scaling-method="resample-any-method"
				text-align="center" >
         	<xsl:attribute name="src">
         		<xsl:text>url(</xsl:text>
         		<xsl:value-of select="$BaseDir"/>
         		<xsl:text>images/</xsl:text>
         		<xsl:value-of select="Logo"/>
         		<xsl:text>)</xsl:text>
         	</xsl:attribute>
         	</fo:external-graphic>
         	</fo:block>
        	</xsl:if>
		<fo:block break-before='page'/>
           <!--<fo:block xsl:use-attribute-sets="H2" space-before="1cm">
            <xsl:value-of select="Revision"/>
           </fo:block>-->
 	</xsl:template>

</xsl:stylesheet>
 	