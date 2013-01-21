<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:ns="http://www.pietons.com/ITEA" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output
	method="html"
	omit-xml-declaration="yes"
	doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
	indent="yes"/>
<xsl:template match="/ns:database">
  <html>
  <head>
  	<title>Database</title>
 	<style type="text/css">
  		table.table{ border:1px solid black;}
  		table.table tr:first-child{ background-color:#000; color:#fc0; }
  		table.table th{ border-bottom:1px solid black; }
  		table.table td{ padding:0 10px 0 10px; }
  		table.table td:first-child{ color:#777; font-weight:bold; }
  		table.table td:first-child+td{ font-style:italic; }
  		table.table td:first-child+td+td{ color:#777; }
  		span.default{ color:#fc0; font-weight:bold; }
  		a:link, a:active, a:hover, a:visited{ color:blue; text-decoration:underline; }
  		table.table td:first-child+td+td span{ display:none; }
  		table.table td:first-child+td+td:hover span{ 
  			display:inline; background-color:#fc0; color:black; border:solid 3px black; 
  			position:absolute; left:30%;
  			padding:1em;
  		}

		table.legend th{ background:#fc0; }
		table.legend td:first-child{ font-weight:bold; }
		table.legend td:first-child+td{ font-size:0.8em; }
		table.legend td[colspan="2"]{ font-size:0.8em; text-align:center; text-decoration:underline; padding-top:0.5em; }
  		
		span.xml{ letter-spacing:1px; line-height:20px; }
  		span.xml_root{ font-weight:bold; }
  		span.xml_tree_child{ padding-left:0.5em; border-left:1px solid black; border-bottom:1px solid black; color:#777; }
  		span.xml_tree_continue{ padding-right:0.5em; border-left:1px solid black; color:white; }
  		span.xml_tree_last{ padding-right:0.5em; padding-left:1px; color:white; }
  		span.xml_content{ font-size:0.8em; background-color:#fc0; border:dotted 1px black;  }
  	</style>
  </head>
  <body>
  	<table width="100%">
  		<tr>
  			<td width="37%">
				<xsl:apply-templates select="ns:table" />
			</td>
			<td valign="top">
				<table class="legend">
					<tr>
						<th colspan="2">Legend</th>
					</tr>
					<tr>
						<td colspan="2">Relational Tables</td>
					</tr>
					<tr>
						<td>Constraints</td>
						<td>Bring mouse over data types mentioned in the<br />
						 tables (left) to view constraints (if any)</td>
					</tr>
					<tr>
						<td>pk,fk</td>
						<td>Primary Key, Foreign Key etc.</td>
					</tr>
					<tr>
						<td>Default</td>
						<td>The default value of the column. Generally<br />
						 inserted using a trigger.</td>
					</tr>
					<tr>
						<td colspan="2">XML</td>
					</tr>
					<tr>
						<td>1</td>
						<td>Only one occurrence</td>
					</tr>
					<tr>
						<td>*</td>
						<td>Multiple occurrence</td>
					</tr>
					<tr>
						<td>Has Content</td>
						<td>The xml element has text content inside it.<br />
						 For example, fname contains the first name.</td>
					</tr>
					<tr>
						<td>Attributes</td>
						<td>Mentioned in parenthesis next to each element<br />
						 name, if it has any attributes.</td>
					</tr>
				</table>
			</td>
			<td valign="top">
				<span class="xml">
					<xsl:apply-templates select="ns:xml" />
				</span>
			</td>
		</tr>
	</table>			
  </body>
  </html>
</xsl:template>

<xsl:template match="ns:table">
<table class="table">
	<tr>
		<th colspan="3"><xsl:value-of select="@name" /></th>
	</tr>
	<xsl:apply-templates select="ns:field" />
</table>
<br />
</xsl:template>

<xsl:template match="ns:field">
<tr>
	<td><xsl:value-of select="@type" /></td>
	<td>
		<xsl:value-of select="." />
		<xsl:if test="string(@default)">
			<xsl:text> </xsl:text>
			<span class="default">
				<xsl:text>(default: </xsl:text>
				<xsl:value-of select="@default" />
				<xsl:text>)</xsl:text>
 			</span>
		</xsl:if>
	</td>
	<td>
		<xsl:value-of select="@data" />
		<xsl:if test="string(@constraint)">
			<span><b>Constraints: </b><xsl:value-of select="@constraint" /></span>
		</xsl:if>
	</td>
</tr>
</xsl:template>

<xsl:template match="ns:xml">
	<br /><br />
	<span class="xml_root"><xsl:value-of select="@name" /></span>
	<xsl:for-each select="ns:element">
		<br />
		<xsl:call-template name="elementTemplate" />
	</xsl:for-each>
	<br />
</xsl:template>

<xsl:template name="elementTemplate">
	<span class="xml_tree_child"><xsl:value-of select="@cardinality" /></span>
	<xsl:value-of select="@name" />
	<xsl:if test="string(@attribute)">
		<xsl:text>(</xsl:text><xsl:value-of select="@attribute" /><xsl:text>)</xsl:text>
	</xsl:if>
	<xsl:if test="string(@content)">
		<xsl:text>--</xsl:text><span class="xml_content">Has Content</span>
	</xsl:if>
	<xsl:for-each select="ns:element">
		<br />
		<xsl:call-template name="shifter" /><xsl:call-template name="elementTemplate" />
	</xsl:for-each>
</xsl:template>

<xsl:template name="shifter">
	<xsl:if test="string(../../ns:element[last()]/@name) != string(../@name)">
		<span class="xml_tree_continue">1</span>
		<!--  Account for three levels (max possible currently) -->
		<xsl:if test="string(../../@cardinality)">
			<span class="xml_tree_continue">1</span>
		</xsl:if>
	</xsl:if>
	<!--  If last element, don't continue the tree formatting -->
	<xsl:if test="string(../../ns:element[last()]/@name) = string(../@name)">
		<span class="xml_tree_last">1</span>
		<!--  Account for three levels (max possible currently) -->
		<xsl:if test="string(../../@cardinality)">
			<span class="xml_tree_last">1</span>
		</xsl:if>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>