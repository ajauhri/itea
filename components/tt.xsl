<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output
	method="html"
	omit-xml-declaration="yes"
	doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
	indent="yes"/>
	
<!--	schedule( *day @d_id (*slot @no (subject_name, teacher_name, infra_name)))  -->
	
<xsl:template match="/schedule">
  <html>
  <head>
  	<title>Time-Table</title>
 	<style type="text/css">
	div.col{ width:125px; position:relative; float:left; top:0px; }
	div.head{ background-color:black; color:#fc0; text-align:center; font:11px Arial,Helvetica,sans-serif; font-weight:bold; height:15px; }
	div.slot{ padding-top:5px; text-align:center; height:45px; border:1px solid black; font:11px bold Arial,Helvetica,sans-serif; }
  	</style>
	</head>
  <body>
	<div class="col">
		<div class="head">
			Timings
		</div>
		<div class="slot">
			Slot 1<br />
			8:00am to 9:00am
		</div>
		<div class="slot">
			Slot 2<br />
			9:00am to 10:00am
		</div>
		<div class="slot">
			Slot 3<br />
			10:00am to 11:00am
		</div>
		<div class="slot">
			Slot 4<br />
			11:00am to 12:00pm
		</div>
		<div class="slot">
			Slot 5<br />
			12:00pm to 1:00pm
		</div>
		<div class="slot">
			Slot 6<br />
			1:00pm to 2:00pm
		</div>
		<div class="slot">
			Slot 7<br />
			2:00pm to 3:00pm
		</div>
		<div class="slot">
			Slot 8<br />
			3:00pm to 4:00pm
		</div>
		<div class="slot">
			Slot 9<br />
			4:00pm to 5:00pm
		</div>
	</div>
	<xsl:apply-templates select="day" />
  </body>
  </html>
</xsl:template>

<xsl:template match="day">
	<div class="col">
	<div class="head">
		<xsl:choose>
			<xsl:when test="@d_id='m'">
				<xsl:text>Monday</xsl:text>
			</xsl:when>
			<xsl:when test="@d_id='tu'">
				<xsl:text>Tuesday</xsl:text>
			</xsl:when>
			<xsl:when test="@d_id='w'">
				<xsl:text>Wednesday</xsl:text>
			</xsl:when>
			<xsl:when test="@d_id='th'">
				<xsl:text>Thursday</xsl:text>
			</xsl:when>
			<xsl:when test="@d_id='f'">
				<xsl:text>Friday</xsl:text>
			</xsl:when>
			<xsl:when test="@d_id='s'">
				<xsl:text>Saturday</xsl:text>
			</xsl:when>
		</xsl:choose>
	</div>
	<div class="slots">
		<xsl:choose>
			<xsl:when test="slot[@no='1']">
				<xsl:apply-templates select="slot[@no='1']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='2']">
				<xsl:apply-templates select="slot[@no='2']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='3']">
				<xsl:apply-templates select="slot[@no='3']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='4']">
				<xsl:apply-templates select="slot[@no='4']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='5']">
				<xsl:apply-templates select="slot[@no='5']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='6']">
				<xsl:apply-templates select="slot[@no='6']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='7']">
				<xsl:apply-templates select="slot[@no='7']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='8']">
				<xsl:apply-templates select="slot[@no='8']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="slot[@no='9']">
				<xsl:apply-templates select="slot[@no='9']" />
			</xsl:when>
			<xsl:otherwise>
				<div class="slot" />
			</xsl:otherwise>
		</xsl:choose>
	</div>
	</div>
</xsl:template>

<xsl:template match="slot[@no='1']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='2']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='3']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='4']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='5']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='6']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='7']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='8']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template match="slot[@no='9']">
	<xsl:call-template name="slot" />
</xsl:template>
<xsl:template name="slot">
	<div class="slot">
		<xsl:value-of select="subject_name" />
		<br />
		<xsl:value-of select="teacher_name" />
		<br />
		<xsl:value-of select="infra_name" />
	</div>
</xsl:template>

</xsl:stylesheet>