<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="sort-table table table-bordered table-hover" data-async="true">

	<es:showMessage />
	<div class="row-fluid tool ui-toolbar">
		<div class="span4">
			<a href="<es:BackURL/>"> <i class="icon-reply"></i> 返回
			</a>
		</div>
	</div>
</div>
<div class="row-fluid">
	<!-- start of main -->
    <section id="m-main" class="span10" style="float:center">

      <form id="xf-form" method="post" action="" class="xf-form">
		<div id="xf-form-table"></div>
		<br>
		
		<div style="text-align:center;">
		  <button id="button0" type="button">发起流程</button>
		</div>
	  </form>

    </section>
	<!-- end of main -->

    <form:form id="f" commandName="m" method="post" style="display:none;">
	  <textarea id="__gef_content__" name="content">${m.content}</textarea>
	</form:form>
</div>
<es:contentFooter/>
<link href="${ctx}/widgets/xform/styles/xform.css" rel="stylesheet">
<script src="${ctx}/widgets/xform/xform-all.js" type="text/javascript"></script>
<script type="text/javascript">
document.onmousedown = function(e) {};
document.onmousemove = function(e) {};
document.onmouseup = function(e) {};
document.ondblclick = function(e) {};

var xform;

$(function() {
	xform = new xf.Xform('xf-form-table');
	xform.render();
	var url = '${ctx}/xform/process/start?processId=${processId}&formid=${formid}';
/* 	
	if('${dataid}' != null && '${dataid}' != ''){
		url = '${ctx}/xform/render/${formid}/${dataid}/update';
	}
 */
	if ($('#__gef_content__').val() != '') {
		xform.doImport($('#__gef_content__').val());
	} 
	
	if ('${json}' != '') {
		xform.setValue(${json});
	}
	
	$(document).delegate('#button0', 'click', function(e) {
		$('#xf-form').attr('action', url);
		$('#xf-form').submit();
	});

	$('.xf-cursor,.xf-handler').addClass('xf-cursor-auto');
});
</script>