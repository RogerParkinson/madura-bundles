<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright 2010 Prometheus Consulting
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
    http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output
		method="html"
		omit-xml-declaration="yes"
		doctype-public="-//W3C//DTD HTML 4.01//EN"
		doctype-system="http://www.w3.org/TR/html4/strict.dtd"
		indent="yes"/>
	<xsl:param name="Year">2007</xsl:param>
	<xsl:param name="ProductVersion">1.1.1</xsl:param>
	<xsl:param name="Build">Build 1234</xsl:param>
	<xsl:param name="Company">xyz Ltd</xsl:param>
	<xsl:key name="references" match="reference" use="@t" />
	<xsl:key name="sections" match="a1|a2|a3|a4|a5|h1|h2|h3|h4|h5" use="@t" />
	<xsl:key name="figures" match="img" use="text()" />
	<xsl:key name="tables" match="table[string-length(@t) &gt; 0]" use="@t" />
	<xsl:template match="/">
		<xsl:processing-instruction name="xml-stylesheet">
			type="text/css" href="#css"
		</xsl:processing-instruction>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="doc">
		<html>
			<style id="css">
				body
				{ 
					margin:0; 
					padding:0 0 0 175px; 
					COLOR: #610B0B;
					FONT-FAMILY: Arial; 
					FONT-SIZE: 12pt; 
					FONT-WEIGHT:normal; 
					TEXT-ALIGN: left; 
					background-repeat: no-repeat;
					background-position: -220px 0px; 
					background-attachment: fixed; 
				} 
				.log 
				{
					FONT-FAMILY: Arial; 
					white-space: pre-wrap; 
					white-space: -moz-pre-wrap !important;
					white-space : pre;
					/*word-break: break-all;*/ 
					word-wrap: break-word; /* IE */ 
					width:70%;
					margin-top: 5px;
					margin-right: 5px;
					margin-bottom: 5px;
					margin-left: 5px;
					border-top-width: thin;
					border-top-style: solid;
					border-top-color: -moz-use-text-color;

					border-bottom-width: thin;
					border-bottom-style: solid;
					border-bottom-color: -moz-use-text-color;

					border-right-width: thin;
					border-right-style: solid;
					border-right-color: -moz-use-text-color;

					border-left-width: thin;
					border-left-style: solid;
					border-left-color: -moz-use-text-color;

					padding-top: 10px;
					padding-right: 10px;
					padding-bottom: 10px;
					padding-left: 10px;
					background-color:  	#ffffcc;
					background-image: none;
					background-repeat: repeat;
					background-attachment: scroll;
					background-position: 0% 0%;
					line-height: 1.1;
				} 
				.titleSection 
				{
					font-size: 80%;
					position:fixed; color:blue; top:30px;
					left:5px; width:170px; height:100%; 
				}
				img {display: block; margin-left:auto; margin-right:auto;text-align:center;}
				p
				{
				}
				a
				{
				}
				.figureCaption
				{
					font-style:italic;
				}
				.list
				{
				}
				.xtable
				{
					/*border: 1px #000 solid;
					margin-left:auto; margin-right:auto;
					text-align:center;
					display: block;*/
				}
				.center
				{
					margin-left:auto; margin-right:auto;
				}
				table.xtable.tr.td /* this doesn't work yet */
				{
					vertical-align:top;
					border: 1px #000 solid;
				}
				td
				{
					vertical-align:top;
				}
				th
				{
					vertical-align:top;
					background-color:gray;
				}
				emph
				{
					font-style:italic;
				}
				bold
				{
					font-weight:bold; 
				}
				.courier
				{
					font-family: courier,monospace;
					font-size: 90%;
				}
				.comment
				{
					font-family: courier,monospace;
					font-size: 90%;
					color:blue;
				}
				
				.code
				{
					font-family: courier,monospace;
					font-size: 95%;
					width:70%;
					margin-top: 5px;
					margin-right: 5px;
					margin-bottom: 5px;
					margin-left: 5px;

					border-top-width: thin;
					border-top-style: solid;
					border-top-color: -moz-use-text-color;

					border-bottom-width: thin;
					border-bottom-style: solid;
					border-bottom-color: -moz-use-text-color;

					border-right-width: thin;
					border-right-style: solid;
					border-right-color: -moz-use-text-color;

					border-left-width: thin;
					border-left-style: solid;
					border-left-color: -moz-use-text-color;

					padding-top: 10px;
					padding-right: 10px;
					padding-bottom: 10px;
					padding-left: 10px;
					background-color: #ffffcc;
					background-image: none;
					background-repeat: repeat;
					background-attachment: scroll;
					background-position: 0% 0%;
					line-height: 1.1;
				}
				.rantHeader
				{
					background-image: url(rant.gif); 
					background-repeat: no-repeat;
				}
				.rant
				{
					float:center;
					font-family: Arial;
					font-size: 90%;
					width:30%;
					margin-top: 5px;
					margin-right: 5px;
					margin-bottom: 5px;
					margin-left: 300px;

					border-top-width: thin;
					border-top-style: solid;
					border-top-color: -moz-use-text-color;

					border-bottom-width: thin;
					border-bottom-style: solid;
					border-bottom-color: -moz-use-text-color;

					border-right-width: thin;
					border-right-style: solid;
					border-right-color: -moz-use-text-color;

					border-left-width: thin;
					border-left-style: solid;
					border-left-color: -moz-use-text-color;

					padding-top: 10px;
					padding-right: 10px;
					padding-bottom: 15px;
					padding-left: 10px;
					background-color: pink;
					background-image: none;
					background-repeat: repeat;
					background-attachment: scroll;
					background-position: 0% 0%;
					line-height: 1.1;
				}
				.warningHeader
				{
					background-image: url(images/warning.gif); 
					background-repeat: no-repeat;
				}
				.warning
				{
					float:center;
					font-family: Arial;
					font-size: 90%;
					width:30%;
					margin-top: 5px;
					margin-right: 5px;
					margin-bottom: 5px;
					margin-left: 300px;

					border-top-width: thin;
					border-top-style: solid;
					border-top-color: -moz-use-text-color;

					border-bottom-width: thin;
					border-bottom-style: solid;
					border-bottom-color: -moz-use-text-color;

					border-right-width: thin;
					border-right-style: solid;
					border-right-color: -moz-use-text-color;

					border-left-width: thin;
					border-left-style: solid;
					border-left-color: -moz-use-text-color;

					padding-top: 10px;
					padding-right: 10px;
					padding-bottom: 15px;
					padding-left: 10px;
					background-color: yellow;
					background-image: none;
					background-repeat: repeat;
					background-attachment: scroll;
					background-position: 0% 0%;
					line-height: 1.1;
				}
				.noteHeader
				{
					background-image: url(images/note.gif); 
					background-repeat: no-repeat;
				}
				.note
				{
					float:center;
					font-family: Arial;
					font-size: 90%;
					width:30%;
					margin-top: 5px;
					margin-right: 5px;
					margin-bottom: 5px;
					margin-left: 300px;

					border-top-width: thin;
					border-top-style: solid;
					border-top-color: -moz-use-text-color;

					border-bottom-width: thin;
					border-bottom-style: solid;
					border-bottom-color: -moz-use-text-color;

					border-right-width: thin;
					border-right-style: solid;
					border-right-color: -moz-use-text-color;

					border-left-width: thin;
					border-left-style: solid;
					border-left-color: -moz-use-text-color;

					padding-top: 10px;
					padding-right: 10px;
					padding-bottom: 15px;
					padding-left: 10px;
					background-color: Gray;
					background-image: none;
					background-repeat: repeat;
					background-attachment: scroll;
					background-position: 0% 0%;
					line-height: 1.1;
				}
				Year
				{
				}
				.footnote
				{
					position: relative;
					/*background-color: white;*/
					text-decoration: none;
					color: blue;
					font-size:10px;
					vertical-align:top; 
				}
				.footnote:hover .footnotebody
				{
					display: block;
					position: absolute;
					width: 20em;
					background-color: white;
					padding: 1px 3px 2px 3px;
					border: 1px #000 solid;
					font-size:10px;
					left: 2em;
					top: 0.5em;
				}
				.footnotebody
				{
					display:none;
				}

			</style>
			<head>
				<title>
					<xsl:value-of select="/doc/title/MainTitle" />
					::
					<xsl:value-of select="/doc/title/SubTitle" />
				</title>
				<meta http-equiv="content-type"
					content="text/html; charset=ISO-8859-1" />
				<meta http-equiv="content-language" content="en" />
				<meta name="robots" content="index,follow" />
			</head>
			<body>
				<xsl:apply-templates select="title" />
				<xsl:apply-templates select="body" />
			</body>
		</html>
	</xsl:template>

	<xsl:template match="process-references">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:text>reference.References</xsl:text>
			</xsl:attribute>
		<h1>
			<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
			<xsl:text> </xsl:text>
			<xsl:text>References</xsl:text>
		</h1>
		</a>
		<xsl:apply-templates select="/doc/title/references" />
	</xsl:template>

	<xsl:template match="process-log">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:text>reference.ChangeLog</xsl:text>
			</xsl:attribute>
		<h1>
			<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
			<xsl:text> </xsl:text>
			<xsl:text>Change Log</xsl:text>
		</h1>
		</a>
           <xsl:apply-templates select="/doc/title/log"/>
	</xsl:template>

	<xsl:template match="title">
		<!-- Title Page -->
		<div class="titleSection">
			<div id="MainTitle">
				<xsl:value-of select="MainTitle" />
			</div>
			<div id="SubTitle">
				<xsl:value-of select="SubTitle" /><br/>
				<xsl:value-of select="Author" /><br/>
				<xsl:variable name="rev" select="Revision"/>
				<xsl:value-of select="translate($rev,'$',' ')"/><br/>
			</div>
			<br/>
			<xsl:apply-templates select="/doc/body/a1|/doc/body/h1|/doc/body/process-log|/doc/body/process-references" mode="toc"/>
			<xsl:apply-templates select="/doc/body/process-reviewers" mode="toc"/>
		</div>
	</xsl:template>

	<xsl:template match="log">
		<pre><div class="log">
			<xsl:value-of select="substring-after(text(),'v $')"
				disable-output-escaping="yes" />
		</div></pre>
	</xsl:template>
	<xsl:template match="h1" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:value-of select="@t" /><br/>
		</a>
	</xsl:template>
	<xsl:template match="a1" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:value-of select="@t" /><br/>
		</a>
	</xsl:template>
	<xsl:template match="process-reviewers" mode="toc">
		<br/>
		<xsl:apply-templates select="/doc/title/reviewers" />
	</xsl:template>
	<xsl:template match="reviewers">
		<div>Reviewers</div>
		<table>
		<xsl:apply-templates />
		</table>
	</xsl:template>
	<xsl:template match="reviewer">
		<tr>
		<td>
		<a>
			<xsl:attribute name="href">
				<xsl:text>mailto:</xsl:text>
				<xsl:value-of select="@email" />
			</xsl:attribute>
			<xsl:attribute name="title">
				<xsl:value-of select="@fullName" />
			</xsl:attribute>
			<xsl:value-of select="@name" />
		</a>
		</td>
		<td>
		<input TYPE="CHECKBOX">
			<xsl:if test="'true'=@complete">
		 		<xsl:attribute name="checked">
		 	        	<xsl:text>true</xsl:text>
				</xsl:attribute>
			</xsl:if>
	 		<xsl:attribute name="disabled">
	 	        	<xsl:text>true</xsl:text>
			</xsl:attribute>
		</input>
		</td>
		</tr>
	</xsl:template>
	<xsl:template match="process-log" mode="toc">
		<a href="#reference.ChangeLog">Change Log</a><br/>
	</xsl:template>
	<xsl:template match="process-references" mode="toc">
		<a href="#reference.References">References</a><br/>
	</xsl:template>
	<xsl:template match="h2" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
		<xsl:apply-templates select="h3" mode="toc"/>
	</xsl:template>
	<xsl:template match="h3" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
	</xsl:template>

	<xsl:template match="h4" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
		<xsl:apply-templates select="h5" mode="toc"/>
	</xsl:template>

	<xsl:template match="h5" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
	</xsl:template>

	<xsl:template match="a2" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
		<xsl:apply-templates select="a3" mode="toc"/>
	</xsl:template>
	<xsl:template match="a3" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
	</xsl:template>

	<xsl:template match="a4" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
		<xsl:apply-templates select="a5" mode="toc"/>
	</xsl:template>

	<xsl:template match="a5" mode="toc">
		<a>
	 		<xsl:attribute name="href">
	 	        	<xsl:value-of select="concat('#reference.',@t)"/>
			</xsl:attribute>
			<xsl:text>  </xsl:text>
			<xsl:value-of select="@t" /><br/>
		</a>
	</xsl:template>

	<xsl:template match="body">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="h1">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:value-of select="concat('reference.',@t)"/>
			</xsl:attribute>
		<h1>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h1>
		</a>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="a1">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:value-of select="concat('reference.',@t)"/>
			</xsl:attribute>
		<h1>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h1>
		</a>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="h2">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:value-of select="concat('reference.',@t)"/>
			</xsl:attribute>
		<h2>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h2>
		</a>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="h3">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:value-of select="concat('reference.',@t)"/>
			</xsl:attribute>
		<h3>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h3>
		</a>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="h4">
		<h4>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h4>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="h5">
		<h5>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h5>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="a2">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:value-of select="concat('reference.',@t)"/>
			</xsl:attribute>
		<h2>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h2>
		</a>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="a3">
		<a>
	 		<xsl:attribute name="name">
	 	        	<xsl:value-of select="concat('reference.',@t)"/>
			</xsl:attribute>
		<h3>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h3>
		</a>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="a4">
		<h4>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h4>
		<xsl:apply-templates />
	</xsl:template>
	<xsl:template match="a5">
		<h5>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
			<xsl:text> </xsl:text>
			<xsl:value-of select="@t" />
		</h5>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="list">
		<table class="list">
			<xsl:apply-templates />
		</table>
	</xsl:template>
	<xsl:template match="le">
		<tr>
			<td>&#8226;</td>
			<!--  <td>&#1645;</td>-->
			<td>
				<xsl:apply-templates />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="ln">
		<tr>
			<td><bold><xsl:number format="1" level="any" from="list" count="ln"/></bold></td>
			<td>
				<xsl:apply-templates />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="ll">
		<tr>
			<td><bold><xsl:value-of select="@name" /></bold></td>
			<td>
				<xsl:apply-templates />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="p">
		<p>
			<xsl:apply-templates />
		</p>
	</xsl:template>
	<xsl:template match="emph">
		<emph><xsl:apply-templates /></emph>
	</xsl:template>
	<xsl:template match="footnote">
		<a href="#" class="footnote">
		<xsl:number format="1" level="any" from="/" count="footnote"/>
		<span class="footnotebody"><xsl:apply-templates /></span>
		</a>
	</xsl:template>
	<xsl:template match="table">
		<table border="1" class="xtable">
		<xsl:apply-templates />
		</table>
		<xsl:if test="string-length(@t) &gt; 0">
			<a  name="{concat('table.',@t)}">
				<div style="text-align: left;">
				<xsl:text>Table </xsl:text>
				<xsl:number format="[1]" level="any" from="/" count="table[string-length(@t) &gt; 0]"/>
				<xsl:text> </xsl:text>
				<xsl:value-of select="@t"/>
				</div>
			</a>
		</xsl:if>
	</xsl:template>
	<xsl:template match="tr">
		<tr><xsl:apply-templates /></tr>
	</xsl:template>
	<xsl:template match="tw">
		<!-- do nothing -->
	</xsl:template>
	<xsl:template match="th">
		<th><xsl:apply-templates /></th>
	</xsl:template>
	<xsl:template match="td">
		<td><xsl:apply-templates /></td>
	</xsl:template>
	<xsl:template match="bold">
		<bold><xsl:apply-templates /></bold>
	</xsl:template>
	<xsl:template match="courier">
		<span class="courier"><xsl:apply-templates /></span>
	</xsl:template>
	<xsl:template match="comment">
		<div class="comment">
			<xsl:value-of select="@who"/>
			<xsl:text>:</xsl:text>
			<xsl:value-of select="@when"/>
			<xsl:text> </xsl:text>
			<xsl:apply-templates />
		</div>
	</xsl:template>
	<xsl:template match="code">
		<pre class="code"><xsl:apply-templates /></pre>
	</xsl:template>
	<xsl:template match="rant">
		<div class="rant">
			<div style="float: left;">
			<img src="images/rant.gif" height="30px"/>
			</div>
			<xsl:apply-templates />
		</div>
	</xsl:template>
	<xsl:template match="note">
		<div class="note">
			<div style="float: left;">
			<img src="images/note.png" height="30px"/>
			</div>
			<xsl:apply-templates />
		</div>
	</xsl:template>
	<xsl:template match="warning">
		<div class="warning">
			<div style="float: left;">
			<img src="images/warning.gif" height="40px"/>
			</div>
			<xsl:apply-templates />
		</div>
	</xsl:template>
	<xsl:template match="Year">
	   <xsl:value-of select="$Year"/>
	</xsl:template>
	<xsl:template match="ProductVersion">
	   <xsl:value-of select="$ProductVersion"/>
	</xsl:template>

	<xsl:template match="references">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="reference">
		<p>
			<xsl:number format="[1]" level="any" from="/"
				count="reference" />
			<xsl:text> </xsl:text>
				<a>
					<xsl:if test="string-length(@url) &gt; 0">
					<xsl:attribute name="href">
		            		<xsl:value-of select="@url" />
		            	</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="@t" />
				</a>
		</p>
	</xsl:template>

      <xsl:template match="referenceLink">
          	<xsl:call-template name="referenceLinkRef">
            		<xsl:with-param name="content" select="@t"/>
            	</xsl:call-template>
       </xsl:template>
        
         <xsl:template name="referenceLinkRef">
          	<xsl:param name="content">2007</xsl:param>
          	<xsl:for-each select="key('references',$content)">
            	<xsl:if test="string-length(@url) &gt; 0">
			<a>
				<xsl:attribute name="href">
					<xsl:value-of select="@url" />
				</xsl:attribute>
				<xsl:number format="[1]" level="any" from="/" count="reference"/>
			</a>
		</xsl:if>
            	<xsl:if test="string-length(@url) &lt;= 0">
			<a>
				<xsl:attribute name="href">
					<xsl:value-of select="concat('#reference.',$content)" />
				</xsl:attribute>
				<xsl:number format="[1]" level="any" from="/" count="reference"/>
			</a>
		</xsl:if>
		<xsl:text> </xsl:text>
		</xsl:for-each>
          </xsl:template>

         <xsl:template match="sectionLink">
	         	<a>
	 	          	<xsl:attribute name="href">
	 	          		<xsl:value-of select="concat('#reference.',@t)"/>
				</xsl:attribute>
	            	<xsl:call-template name="sectionLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
	         	</a>
          </xsl:template>

         <xsl:template name="sectionLinkRef">
          	<xsl:param name="content">2007</xsl:param>
          		<xsl:for-each select="key('sections',$content)">
  			      	<xsl:choose>
			          <xsl:when test="name() = 'h1'">
				        	<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
			          </xsl:when>
			          <xsl:when test="name() = 'h2'">
			        	
				        	<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
							<xsl:number format="1" level="any" from="h1|process-references|process-log" count="h2"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'h3'">
			        	
				        	<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
							<xsl:number format="1." level="any" from="h1|process-references|process-log" count="h2"/>
							<xsl:number format="1" level="any" from="h2" count="h3"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'h4'">
			        	
				        	<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
							<xsl:number format="1." level="any" from="h1|process-references|process-log" count="h2"/>
							<xsl:number format="1." level="any" from="h2" count="h3"/>
							<xsl:number format="1" level="any" from="h3" count="h4"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'h5'">
			        	
				        	<xsl:number format="1." level="any" from="/" count="h1|process-references|process-log"/>
							<xsl:number format="1." level="any" from="h1|process-references|process-log" count="h2"/>
							<xsl:number format="1." level="any" from="h2" count="h3"/>
							<xsl:number format="1." level="any" from="h3" count="h4"/>
							<xsl:number format="1" level="any" from="h4" count="h5"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'a1'">
				        	<xsl:number format="A." level="any" from="/" count="a1"/>
			          </xsl:when>
			          <xsl:when test="name() = 'a2'">
			        	
				        	<xsl:number format="A." level="any" from="/" count="a1"/>
							<xsl:number format="1" level="any" from="a1" count="a2"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'a3'">
			        	
				        	<xsl:number format="A." level="any" from="/" count="a1"/>
							<xsl:number format="1" level="any" from="a1" count="a2"/>
							<xsl:number format="1" level="any" from="a2" count="a3"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'a4'">
			        	
				        	<xsl:number format="A." level="any" from="/" count="a1"/>
							<xsl:number format="1" level="any" from="a1" count="a2"/>
							<xsl:number format="1" level="any" from="a2" count="a3"/>
							<xsl:number format="1" level="any" from="a3" count="a4"/>
						
			          </xsl:when>
			          <xsl:when test="name() = 'a5'">
			        	
				        	<xsl:number format="A." level="any" from="/" count="a1"/>
							<xsl:number format="1" level="any" from="a1" count="a2"/>
							<xsl:number format="1" level="any" from="a2" count="a3"/>
							<xsl:number format="1" level="any" from="a3" count="a4"/>
							<xsl:number format="1" level="any" from="a4" count="a5"/>
						
			          </xsl:when>
			        </xsl:choose>
          			
			  </xsl:for-each>
          </xsl:template>
	 
	<xsl:template match="img">
		<img>
         	<xsl:attribute name="src">
         		<xsl:value-of select="@href"/>
         	</xsl:attribute>
         	<!-- 
         	<xsl:if test="string-length(@width) &gt; 0">
	         	<xsl:attribute name="width">
	         		<xsl:value-of select="@width"/>
	         	</xsl:attribute>
         	</xsl:if>
         	<xsl:if test="string-length(@height) &gt; 0">
	         	<xsl:attribute name="height">
	         		<xsl:value-of select="@height"/>
	         	</xsl:attribute>
         	</xsl:if>
         	 -->
         	<xsl:attribute name="onclick">
         		<xsl:text>javascript:window.open("</xsl:text>
         		<xsl:value-of select="@href"/>
         		<xsl:text>");</xsl:text>
         	</xsl:attribute>
		</img>
		<a  name="{concat('figure.',text())}">
			<div style="text-align: center;">
			<xsl:text>Figure </xsl:text>
			<xsl:number format="[1]" level="any" from="/" count="img"/>
			<xsl:text> </xsl:text>
			<xsl:value-of select="text()"/>
			</div>
		</a>
	</xsl:template>
 
      <xsl:template match="figureLink">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="concat('#figure.',@t)" />
			</xsl:attribute>
			<xsl:text>Figure </xsl:text>
	            	<xsl:call-template name="figureLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
		</a>
       </xsl:template>
        
         <xsl:template name="figureLinkRef">
          	<xsl:param name="content">2007</xsl:param>
          	<xsl:for-each select="key('figures',$content)">
			<xsl:number level="any" from="/" count="img" format="(1)"/>
		</xsl:for-each>
          </xsl:template>

      <xsl:template match="tableLink">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="concat('#table.',@t)" />
			</xsl:attribute>
			<xsl:text>Table </xsl:text>
	            	<xsl:call-template name="tableLinkRef">
	            		<xsl:with-param name="content" select="@t"/>
	            	</xsl:call-template>
		</a>
       </xsl:template>
        
         <xsl:template name="tableLinkRef">
          	<xsl:param name="content">2007</xsl:param>
          	<xsl:for-each select="key('tables',$content)">
          		<xsl:number format="[1]" level="any" from="/" count="table[string-length(@t) &gt; 0]"/>
		</xsl:for-each>
          </xsl:template>

</xsl:stylesheet>
